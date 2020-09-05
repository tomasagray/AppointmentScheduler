/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Appointment Scheduler
 * -----------------------------------------------------------------------------
 *
 * A multi-user calendar and appointment tracking software solution. Supports
 * several languages and configuration options.
 *
 * Developed for WGU course C195 - Advanced Java Concepts
 *
 * @author tomas
 */


package self.me.wgu.appointmentscheduler;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class AppointmentScheduler extends Application {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Helper method to transform an integer hour into a more recognizable String, e.g., 14  -> "2:00
   * PM"
   *
   * @param hour Hour of the day
   * @return Formatted time String
   */
  public static String formatTime(int hour) {
      if (hour == 0)   // midnight
      {
          return "12:00 AM";
      } else if (hour < 12)   // morning,
      {
          return hour + ":00 AM";
      } else if (hour == 12) // noon,
      {
          return "12:00 PM";
      } else            // and night
      {
          return (hour - 12) + ":00 PM";
      }
  }

  @Override
  public void start(Stage stage) {

    try {
      // Load settings
      // ---------------------------------------------------------------------
      SettingsManager.getInstance().loadSettingsData();

      // Setup SceneManager
      // ---------------------------------------------------------------------
      SceneManager sm = SceneManager.getInstance();
      sm.setStage(stage);
      // Setup application title bar
      sm.getStage().setTitle("Appointment Scheduler");
      final String url = getClass().getResource("/img/icon.png").toString();
      sm.getStage().getIcons().add(new Image(url));

      // Load Customers screen sub-panels
      // ---------------------------------------------------------------
      sm.addScene(
          "NewCustomerPanel", "/fxml/NewCustomerPanel.fxml"
      );
      sm.addScene(
          "CustomerDetailsPanel", "/fxml/CustomerDetailsPanel.fxml"
      );
      sm.addScene(
          "EditCustomerPanel", "/fxml/EditCustomerPanel.fxml"
      );

      // Load Calendar views
      // ---------------------------------------------------------------------
      sm.addScene("MonthCalendar", "/fxml/MonthCalendar.fxml");
      sm.addScene("WeekCalendar", "/fxml/WeekCalendar.fxml");

      // Add main views to the SceneManager
      // ---------------------------------------------------------------------
      sm.addScene("Login", "/fxml/LoginForm.fxml");
      sm.addScene("UIContainer", "/fxml/UIContainer.fxml");
      sm.addScene("Customers", "/fxml/Customers.fxml");
      sm.addScene("Calendar", "/fxml/Calendar.fxml");
      sm.addScene("Reports", "/fxml/Reports.fxml");
      sm.addScene("Settings", "/fxml/Settings.fxml");

      // Display login
      // ---------------------------------------------------------------------
      sm.initializeLoginForm();
      sm.getStage().show();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
