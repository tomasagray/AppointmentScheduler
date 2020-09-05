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

/**
 * @author tomas
 */
public class HoursList extends VBox {

  public HoursList() {
    super();
    for (int i = 0; i <= 23; i++) {
      this.getChildren().add(new Hour(i));
    }
  }

  private static final class Hour extends VBox {

    private Hour(int hour) {
      super();

      String hour1;
      if (hour == 12) {
        hour1 = "12p";
      } else if (hour == 0) {
        hour1 = "12a";
      } else if (hour > 12) {
        hour1 = (hour - 12) + "p";
      } else {
        hour1 = hour + "a";
      }

      this.setMinHeight(75);
      this.setPrefHeight(75);
      this.setStyle("-fx-border-color: cyan");
      this.getChildren().add(new Label(hour1));
    }
  }
}
