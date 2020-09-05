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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import self.me.wgu.appointmentscheduler.MySQLConnection;
import self.me.wgu.appointmentscheduler.Resettable;
import self.me.wgu.appointmentscheduler.model.Appointment;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class MonthCalendarController implements Initializable, Resettable {

  private LocalDate currentDate;

  @FXML
  private GridPane monthCalendar;


  public void setDate(LocalDate date) {
    this.currentDate = date;
    populateMonthCalendar();
  }

  public void populateMonthCalendar() {

    LocalDate ld = getCalendarStart();

    // Get appointments for 35 days
    List<Appointment> appointments = MySQLConnection.getInstance()
        .getAppointments(ld, currentDate.plusDays(35));

    // Clear previous calendar entries
    monthCalendar.getChildren().clear();

    for (int i = 0; i < 5; i++)  // weeks
    {
      for (int j = 0; j < 7; j++)  // days
      {
        DayOfMonth day = new DayOfMonth(ld);

        // Set appropriate styles
          if (!(ld.getMonth().toString().equals(currentDate.getMonth().toString())))
          // not of this month
          {
              day.addStyle("-fx-background-color: cyan;");
          } else if (ld.equals(LocalDate.now()))   // the current day
          {
              day.getStyleClass().add("currentDay");
          }

        // Add appointments for this day, sorted by start time
        appointments.stream()
            .filter((appointment) -> appointment.getStartDate().isEqual(day.getDate()))
            .sorted(Comparator.comparing(Appointment::getStart))
            .forEach(day::addAppointment);

        // Add day to calendar
        monthCalendar.add(day, j, i);
        ld = ld.plusDays(1);
      }
    }

  }

  private LocalDate getCalendarStart() {
    // Get a LocalDate object of the currently displayed calendar
    LocalDate ld = LocalDate.of(
        currentDate.getYear(),
        currentDate.getMonth(),
        1
    );

    // Walk date back until the most recent past Sunday
      while (!ld.getDayOfWeek().toString().equals("SUNDAY")) {
          ld = ld.minusDays(1);
      }

    return ld;
  }

  @Override
  public void reset() {
    monthCalendar.getChildren().clear();
  }

  /**
   * Initializes the controller class.
   *
   * @param url URL
   * @param rb  ResourceBundle
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: MonthCalendar");
  }
}
