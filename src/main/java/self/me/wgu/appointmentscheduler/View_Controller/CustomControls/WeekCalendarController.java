/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import self.me.wgu.appointmentscheduler.Resettable;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import self.me.wgu.appointmentscheduler.Model.Appointment;
import self.me.wgu.appointmentscheduler.MySQLConnection;

/**
 *
 * @author tomas
 */
public class WeekCalendarController implements Initializable, Resettable
{
    
    private LocalDate date;
    
    @FXML
    private HBox weekCalOuter;
    @FXML
    private GridPane weekCalendar;
    @FXML
    private GridPane weekDates;
    @FXML
    private ScrollPane weekCalContainer;
    
    
    public void setDate(LocalDate ld)
    {
        this.date = ld;
        populateWeekCalendar();
    }
    
    public void populateWeekCalendar()
    {
        MySQLConnection msql = MySQLConnection.getInstance();
        LocalDate ld = getCalendarStart();
        
        // Reset screen
        weekDates.getChildren().clear();
        weekCalendar.getChildren().clear();
        
        // Get appointments for the next week
        List<Appointment> appointments = msql.getAppointments(ld, ld.plusWeeks(1).plusDays(1));

        for(int i=0; i<7; i++)
        {
            CalDayOfWeek day = new CalDayOfWeek(ld);
            // Add appointments 
            appointments.stream().filter(e -> {
               return e.getStartDate().isEqual(day.getDate());
            }).forEach(e -> {
                day.addAppointment(e);
            });

            weekCalendar.add(day,i,0);
            weekDates.add(new WeekDateHeading(ld), i, 0);
            ld = ld.plusDays(1);
        }
    }
    
    private LocalDate getCalendarStart()
    {
        LocalDate ld = this.date;
        
        // Walk date back until the most recent past Sunday
        while( !ld.getDayOfWeek().toString().equals("SUNDAY") )
            ld = ld.minusDays(1);
        
        return ld;
    }
    
    @Override
    public void reset()
    {
        weekCalendar.getChildren().clear();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    { 
        System.out.println("Initializing: WeekCalendar");
        
        this.date = LocalDate.now();
        
        // Ensure week calendar GridPane fills window
        weekCalOuter.prefWidthProperty().bind(weekCalContainer.widthProperty());
        weekCalendar.prefWidthProperty().bind( weekCalOuter.widthProperty() );
        // Scroll to working hours
        weekCalContainer.setVvalue(0.5);
        populateWeekCalendar();
    }  
}
