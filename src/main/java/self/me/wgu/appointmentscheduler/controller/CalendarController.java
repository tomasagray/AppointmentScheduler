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
package self.me.wgu.appointmentscheduler.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import self.me.wgu.appointmentscheduler.Resettable;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SceneManager.ViewMode;
import self.me.wgu.appointmentscheduler.controller.custom.DayOfMonth;
import self.me.wgu.appointmentscheduler.controller.custom.MonthCalendarController;
import self.me.wgu.appointmentscheduler.controller.custom.WeekCalendarController;

/**
 * FXML Controller class for the Calendar wrapper. This controller delegates functionality to either
 * the MonthCalendarController or WeekCalendarController, depending upon which ViewMode is
 * selected.
 *
 * @author tomas
 */

public class CalendarController implements Initializable, Resettable {

  private final List<DayOfMonth> calendarDays = new ArrayList<>(35);
  private LocalDate currentDate;
  // Default view
  private ViewMode viewMode = ViewMode.MONTH;

  @FXML
  private Label monthYearLabel;
  @FXML
  private HBox calendarContainer;

  @FXML
  public void handleCalendarPast() {
    if (viewMode == ViewMode.MONTH) {
      currentDate = currentDate.minusMonths(1);
      showMonthCalendar();
    } else {
      currentDate = currentDate.minusWeeks(1);
      showWeekCalendar();
    }
    updateMonthYearLabel();
  }

  @FXML
  public void handleCalendarFuture() {
    if (viewMode == ViewMode.MONTH) {
      currentDate = currentDate.plusMonths(1);
      showMonthCalendar();
    } else {
      currentDate = currentDate.plusWeeks(1);
      showWeekCalendar();
    }

    updateMonthYearLabel();
  }

  @FXML
  public void handleCalendarToday() {
    currentDate = LocalDate.now();

      if (viewMode == ViewMode.MONTH) {
          showMonthCalendar();
      } else {
          showWeekCalendar();
      }

    updateMonthYearLabel();
  }

  @FXML
  public void handleCalendarWeek() {
    viewMode = ViewMode.WEEK;
    showWeekCalendar();
  }

  @FXML
  public void handleCalendarMonth() {
    viewMode = ViewMode.MONTH;
    showMonthCalendar();
  }

  public void showWeekCalendar() {
    calendarContainer.getChildren().clear();
    Parent node = SceneManager.getInstance().getScene("WeekCalendar");
    WeekCalendarController wcc = SceneManager.getInstance()
        .getLoader("WeekCalendar")
        .getController();
    wcc.setDate(currentDate);
    calendarContainer.getChildren().add(node);
  }

  public void showMonthCalendar() {
    calendarContainer.getChildren().clear();
    Parent node = SceneManager.getInstance().getScene("MonthCalendar");
    MonthCalendarController mcc = SceneManager.getInstance()
        .getLoader("MonthCalendar")
        .getController();
    mcc.setDate(currentDate);
    calendarContainer.getChildren().add(node);
  }

  public void drawCalendar() {
      if (viewMode == ViewMode.MONTH) {
          showMonthCalendar();
      } else {
          showWeekCalendar();
      }
  }

  protected void updateMonthYearLabel() {
    monthYearLabel.setText(currentDate.getMonth().toString() +
        ", " + currentDate.getYear());
  }


  @Override
  public void reset() {
    handleCalendarMonth();
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: Calendar");

    // Set to current date
    currentDate = LocalDate.now();
    updateMonthYearLabel();

    // Populate calendarContainer
    drawCalendar();
  }

}
