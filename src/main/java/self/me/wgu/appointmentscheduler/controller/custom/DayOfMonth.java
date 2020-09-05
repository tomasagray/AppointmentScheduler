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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javafx.scene.layout.GridPane;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;
import self.me.wgu.appointmentscheduler.model.Appointment;

/**
 * @author tomas
 */
public class DayOfMonth extends GridPane {

  private ZonedDateTime datetime;
  private int colCounter;
  private int rowCounter;

  public DayOfMonth(LocalDate ld) {
    super();
    this.datetime = ZonedDateTime.of(
        ld.atTime(9, 0),     // Default time of 9:00AM
        SettingsManager.getInstance()
            .getSettingsData().getOffice().getZoneId()
    );

    this.add(new DateLabel(ld.getDayOfMonth()), 0, 0);  // Day of month label
    rowCounter++;

    // Set custom styling
    this.addStyle("-fx-padding: 10px;");
    this.setMaxHeight(200);
    // Show add appointment dialog when clicked
    this.setOnMouseClicked(e -> {
      // Create default (empty) appointment, starting at this cell's
      // LocalDateTime value, and ending one hour later.
      Appointment a = new Appointment.AppointmentBuilder()
          .setStart(this.datetime)
          .setEnd(this.datetime.plusHours(1))
          .build();
      SceneManager.getInstance().showAppointmentDialog(e, a);
    });
  }

  public ZonedDateTime getDateTime() {
    return this.datetime;
  }

  public LocalDate getDate() {
    return this.datetime.toLocalDate();
  }

  public void setDate(LocalDate ld) {
    this.datetime = ZonedDateTime.of(
        ld.atTime(9, 0),
        SettingsManager.getInstance()
            .getSettingsData().getOffice().getZoneId()
    );
  }

  public void addAppointment(Appointment appt) {
    this.add(new AppointmentLabel(appt), 0, rowCounter);
    rowCounter++;
  }

  public void addStyle(String style) {
    this.setStyle(this.getStyle() + " " + style);
  }

}
