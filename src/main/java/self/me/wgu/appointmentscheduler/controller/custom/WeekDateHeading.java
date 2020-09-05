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
import javafx.scene.layout.AnchorPane;

/**
 * @author tomas
 */
public class WeekDateHeading extends AnchorPane {

  public WeekDateHeading(LocalDate date) {
    super();
    // Add current day styles, where appropriate
    if (date.equals(LocalDate.now())) {
      this.getStyleClass().add("currentDay");
    } else {
      this.setStyle("-fx-border-color: cyan");
    }

    DateLabel dl = new DateLabel(date.getDayOfMonth());
    AnchorPane.setTopAnchor(dl, 10.0);
    AnchorPane.setLeftAnchor(dl, 5.0);
    this.getChildren().add(dl);
  }
}
