/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import java.time.*;
import javafx.scene.layout.GridPane;
import self.me.wgu.appointmentscheduler.model.Appointment;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;

/**
 *
 * @author tomas
 */
public class DayOfMonth extends GridPane
{
    private ZonedDateTime datetime;
    private int colCounter;
    private int rowCounter;
    
    public DayOfMonth( LocalDate ld )
    {
        super();
        this.datetime = ZonedDateTime.of(
                ld.atTime(9,0),     // Default time of 9:00AM
                SettingsManager.getInstance()
                        .getSettingsData().getOffice().getZoneId()
        );
        
        this.add( new DateLabel( ld.getDayOfMonth() ), 0, 0 );  // Day of month label
        rowCounter++;
        
        // Set custom styling
        this.addStyle( "-fx-padding: 10px;" );
        this.setMaxHeight(200);
        // Show add appointment dialog when clicked
        this.setOnMouseClicked( e -> {
            // Create default (empty) appointment, starting at this cell's
            // LocalDateTime value, and ending one hour later.
            Appointment a = new Appointment.AppointmentBuilder()
                                            .setStart(this.datetime)
                                            .setEnd(this.datetime.plusHours(1))
                                            .build();
            SceneManager.getInstance().showAppointmentDialog(e, a);
        });
    }
    
    public void setDate( LocalDate ld )
    {
        this.datetime = ZonedDateTime.of(
                ld.atTime(9, 0),
                SettingsManager.getInstance()
                        .getSettingsData().getOffice().getZoneId()
        );
    }
    public ZonedDateTime getDateTime()
    {
        return this.datetime;
    }
    public LocalDate getDate()
    {
        return this.datetime.toLocalDate();
    }
    
    public void addAppointment(Appointment appt)
    {
        this.add(new AppointmentLabel(appt), 0, rowCounter);
        rowCounter++;
    }
    
    public void addStyle(String style)
    {
        this.setStyle( this.getStyle() + " " + style);
    }
    
}
