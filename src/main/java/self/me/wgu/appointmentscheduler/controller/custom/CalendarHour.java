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

import java.time.ZonedDateTime;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;
import self.me.wgu.appointmentscheduler.model.Appointment;

/**
 * @author tomas
 */
public class CalendarHour extends VBox {

  private final ZonedDateTime datetime;

  public CalendarHour(ZonedDateTime datetime) {
    super();
    this.datetime = datetime;
    this.setMinHeight(75);
    this.setPrefHeight(75);
    // Outside normal business hours
    int startOfBusiness = SettingsManager.getInstance().
        getSettingsData().getStartOfBusiness();
    int endOfBusiness = SettingsManager.getInstance()
        .getSettingsData().getEndOfBusiness();

    // If this CalendarHour is outside business hours...
    if (startOfBusiness > datetime.getHour()
        || endOfBusiness < datetime.getHour()) {
      // ... change the background color, and...
      this.setStyle("-fx-background-color: #f9f9f9");
      // ...show user a warning if they click it.
      this.setOnMouseClicked(e -> {
        Alert alert = new Alert(
            AlertType.WARNING,
            "Cannot set an appointment outside of business hours.\n"
                + "You can configure business hours under Settings."
        );
        alert.showAndWait();
      });
    } else {
      this.setOnMouseClicked(e -> {
        Appointment a = new Appointment.AppointmentBuilder()
            .setStart(datetime)
            .setEnd(datetime.plusHours(1))
            .build();
        SceneManager.getInstance().showAppointmentDialog(e, a);
      });
    }
  }

  public ZonedDateTime getDateTime() {
    return datetime;
  }
}
