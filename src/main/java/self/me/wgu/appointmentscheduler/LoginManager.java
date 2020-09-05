/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import self.me.wgu.appointmentscheduler.model.Appointment;
import self.me.wgu.appointmentscheduler.model.User;

/**
 * @author tomas
 */
public class LoginManager {

  private final static LoginManager instance = new LoginManager();
  private User user;

  private LoginManager() {
    user = new User("_default");    // Ensure user is initialized
  }

  public static LoginManager getInstance() {
    return instance;
  }

  public User getUser() {
    return this.user;
  }

  public boolean login(User user) {
    // Guilty until proved innocent...
    boolean loginSuccess = false;

    // Attempt to connect to DB
    MySQLConnection.getInstance().connect();

    // Attempt to retrieve user credentials from DB
    Optional<User> dbUser = MySQLConnection.getInstance()
        .getUserCredentials(user.getUserName());
    if (dbUser.isPresent()
        && user.getUserName().equals(dbUser.get().getUserName())
        && user.getPassword().equals(dbUser.get().getPassword())) {
      // Copy retrieved credentials to the supplied User object
      user.setUserID(dbUser.get().getUserID());
      user.setActive(true);
      this.user = user;

      System.out.println("Logged in: " + user.getUserName());

      // Set user active in DB
      MySQLConnection.getInstance().setUserActive(user);

      // Load most current settings
      SettingsManager.getInstance().loadSettingsData();

      // User authenticated
      loginSuccess = true;

      // Check for appointments in the next 15 minutes
      checkUpcomingAppointments();
    }

    // Record login attempt
    Path path = Paths.get(SettingsManager.LOG_FILE);
    try (BufferedWriter os =
        Files.newBufferedWriter(
            path,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND)
    ) {
      os.append(user.getUserName()).append(";")
          .append(user.getPassword()).append(";")
          .append(String.valueOf(Timestamp.from(Instant.now()))).append(";")
          .append(String.valueOf(loginSuccess)).append("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return loginSuccess;
  }

  public void logout(@NotNull User user) {
    // Reset all views
    SceneManager.getInstance().getScenes()
        // Re-initialize each scene
        .forEach((String s, SceneManager.SceneNode sn) ->
        {
          if (sn.getLoader().getController() instanceof Resettable) {
            Resettable c = sn.getLoader().getController();
            c.reset();
          }
        });

    System.out.println("Logged out: " + user.getUserName());

    // Set user to inactive in database
    MySQLConnection.getInstance().setUserInactive(user);
    // Kick the user out to the login form
    SceneManager.getInstance().initializeLoginForm();
  }


  public void checkUpcomingAppointments() {
    // Get today's appointments
    List<Appointment> todayAppointments = MySQLConnection
        .getInstance().getAppointments(
            ZonedDateTime.now().toLocalDate(),
            ZonedDateTime.now().plusDays(2).toLocalDate()
        );
    // Check for any in next 15 minutes
    todayAppointments.stream().anyMatch((Appointment appointment) -> {

      ZonedDateTime now = ZonedDateTime.now();
      if (appointment.getStart().isAfter(now)
          &&
          appointment.getStart().isBefore(now.plusMinutes(15))
      ) {
        String alertStr = "You have an appointment"
            + " with " + appointment.getCustomer().getCustomerName()
            + " at " + AppointmentScheduler.formatTime(appointment.getStart().getHour())
            + ".\nDon't be late!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, alertStr);
        alert.showAndWait();

        return true;
      }
      // No appointments in next 15 minutes
      return false;
    });
  }
}
