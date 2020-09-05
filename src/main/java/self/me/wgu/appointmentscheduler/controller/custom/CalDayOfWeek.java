/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import self.me.wgu.appointmentscheduler.SettingsManager;
import self.me.wgu.appointmentscheduler.model.Appointment;

/**
 * Derived from StackPane, this layout control also wraps a VBox, then adds CalendarHours to it.
 *
 * @author tomas
 */
public class CalDayOfWeek extends StackPane {

  private LocalDate date;

  public CalDayOfWeek(LocalDate ld) {
    // Setup StackPane wrapper
    super();
    this.date = ld;
    this.setAlignment(Pos.TOP_CENTER);
    // Create inner VBox to contain calendar day
    VBox vbox = new VBox();
    this.getChildren().add(vbox);

    // Add hours to the day
    int i = 0;
    do {
      ZonedDateTime zdt = ZonedDateTime.of(
          date.atTime(i, 0),
          SettingsManager.getInstance()
              .getSettingsData().getOffice().getZoneId()
      );
      vbox.getChildren().add(new CalendarHour(zdt));
      i++;
    } while (i <= 23);
  }

  public LocalDate getDate() {
    return this.date;
  }

  public void setDate(LocalDate ld) {
    this.date = ld;
  }

  public void addAppointment(Appointment appt) {
    // Create an AppointmentBox to represent this appointment
    AppointmentBox appointmentBox = new AppointmentBox(appt);
    // Reposition to the correct hour in this day column
    appointmentBox.setTranslateY(SettingsManager.CALENDAR_HOUR_HEIGHT
        * appt.getStart().getHour());
    // Add the AppointmentBox to the scene
    this.getChildren().add(appointmentBox);
  }
}
