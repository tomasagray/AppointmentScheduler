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
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import self.me.wgu.appointmentscheduler.MySQLConnection;
import self.me.wgu.appointmentscheduler.Resettable;
import self.me.wgu.appointmentscheduler.model.Appointment;

/**
 * @author tomas
 */
public class WeekCalendarController implements Initializable, Resettable {

  private LocalDate date;

  @FXML
  private HBox weekCalOuter;
  @FXML
  private GridPane weekCalendar;
  @FXML
  private GridPane weekDates;
  @FXML
  private ScrollPane weekCalContainer;


  public void setDate(LocalDate ld) {
    this.date = ld;
    populateWeekCalendar();
  }

  public void populateWeekCalendar() {
    MySQLConnection msql = MySQLConnection.getInstance();
    LocalDate ld = getCalendarStart();

    // Reset screen
    weekDates.getChildren().clear();
    weekCalendar.getChildren().clear();

    // Get appointments for the next week
    List<Appointment> appointments = msql.getAppointments(ld, ld.plusWeeks(1).plusDays(1));

    for (int i = 0; i < 7; i++) {
      CalDayOfWeek day = new CalDayOfWeek(ld);
      // Add appointments
      appointments.stream().filter(e -> e.getStartDate().isEqual(day.getDate()))
          .forEach(day::addAppointment);

      weekCalendar.add(day, i, 0);
      weekDates.add(new WeekDateHeading(ld), i, 0);
      ld = ld.plusDays(1);
    }
  }

  private LocalDate getCalendarStart() {
    LocalDate ld = this.date;

    // Walk date back until the most recent past Sunday
      while (!ld.getDayOfWeek().toString().equals("SUNDAY")) {
          ld = ld.minusDays(1);
      }

    return ld;
  }

  @Override
  public void reset() {
    weekCalendar.getChildren().clear();
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: WeekCalendar");

    this.date = LocalDate.now();

    // Ensure week calendar GridPane fills window
    weekCalOuter.prefWidthProperty().bind(weekCalContainer.widthProperty());
    weekCalendar.prefWidthProperty().bind(weekCalOuter.widthProperty());
    // Scroll to working hours
    weekCalContainer.setVvalue(0.5);
    populateWeekCalendar();
  }
}
