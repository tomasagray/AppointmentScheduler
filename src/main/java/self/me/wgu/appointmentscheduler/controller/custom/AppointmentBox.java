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

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;
import self.me.wgu.appointmentscheduler.model.Appointment;

/**
 * @author tomas
 */
public class AppointmentBox extends VBox {

  private Label label;
  private Appointment appointment;

  public AppointmentBox() {
    super();

    // Set box click behavior
    this.setOnMouseClicked(e -> SceneManager.getInstance().showAppointmentDialog(e, appointment));
  }

  public AppointmentBox(Appointment appointment) {
    this();
    this.appointment = appointment;
    // Title label
    Label title = new Label(appointment.getTitle());
    title.getStyleClass().add("appointmentBoxTitle");

    this.getChildren().add(title);
    this.getChildren().add(new Label(appointment.getDescription()));

    // Setup box dimensions
    double height = SettingsManager.CALENDAR_HOUR_HEIGHT * appointment.getDuration().toHours();
    this.setMinHeight(height);
    this.setPrefHeight(height);
    this.setMaxHeight(height);

    // **************** FOR TYPE ******************** \\
    switch (appointment.getTitle().toUpperCase()) {
      case "EXECUTIVE MEETING":
        this.setStyle("-fx-background-color: #FFDF00");
        break;
      case "MIND MEETING":
        this.setStyle("-fx-background-color: #133046");
        break;
      case "SALES MEETING":
        this.setStyle("-fx-background-color: green");
        break;
      case "BORING MEETING":
        this.setStyle("-fx-background-color: #666666");
        break;
      case "EXCITING MEETING":
        this.setStyle("-fx-background-color: red");
        break;
    }
  }
}
