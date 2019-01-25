/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller;

import self.me.wgu.appointmentscheduler.Resettable;
import java.net.URL;
import java.time.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SceneManager.ViewMode;
import self.me.wgu.appointmentscheduler.View_Controller.CustomControls.DayOfMonth;
import self.me.wgu.appointmentscheduler.View_Controller.CustomControls.MonthCalendarController;
import self.me.wgu.appointmentscheduler.View_Controller.CustomControls.WeekCalendarController;

/**
 * FXML Controller class for the Calendar wrapper. This controller
 * delegates functionality to either the MonthCalendarController or
 * WeekCalendarController, depending upon which ViewMode is selected.
 *
 * @author tomas
 */

public class CalendarController implements Initializable, Resettable 
{
    private List<DayOfMonth> calendarDays = new ArrayList<>(35);
    private LocalDate currentDate;
    // Default view
    private ViewMode viewMode = ViewMode.MONTH;
    
    @FXML
    private Label monthYearLabel;
    @FXML
    private HBox calendarContainer;

    @FXML
    public void handleCalendarPast()
    {
        if(viewMode == ViewMode.MONTH)
        {
            currentDate = currentDate.minusMonths(1);
            showMonthCalendar( );
        } else {
            currentDate = currentDate.minusWeeks(1);
            showWeekCalendar();
        }
        updateMonthYearLabel();
    }
    @FXML
    public void handleCalendarFuture()
    {
        if(viewMode == ViewMode.MONTH)
        {
            currentDate = currentDate.plusMonths(1);
            showMonthCalendar();
        } else {
            currentDate = currentDate.plusWeeks(1);
            showWeekCalendar();
        }
        
        updateMonthYearLabel();
    }
    @FXML
    public void handleCalendarToday()
    {
        currentDate = LocalDate.now();
        
        if(viewMode == ViewMode.MONTH)
            showMonthCalendar();
        else
            showWeekCalendar();
        
        updateMonthYearLabel();   
    }
    @FXML
    public void handleCalendarWeek()
    {
        viewMode = ViewMode.WEEK;
        showWeekCalendar();
    }
    @FXML
    public void handleCalendarMonth()
    {
        viewMode = ViewMode.MONTH;
        showMonthCalendar();
    }
 
    public void showWeekCalendar()
    {  
        calendarContainer.getChildren().clear();
        Parent node = SceneManager.getInstance().getScene("WeekCalendar");
        WeekCalendarController wcc = SceneManager.getInstance()
                                            .getLoader("WeekCalendar")
                                            .getController();
        wcc.setDate(currentDate);
        calendarContainer.getChildren().add(node);  
    }
    
    public void showMonthCalendar()
    {
        calendarContainer.getChildren().clear();
        Parent node = SceneManager.getInstance().getScene("MonthCalendar");
        MonthCalendarController mcc = SceneManager.getInstance()
                                                .getLoader("MonthCalendar")
                                                .getController();
        mcc.setDate(currentDate);
        calendarContainer.getChildren().add(node);
    }
    
    public void drawCalendar()
    {
        if(viewMode == ViewMode.MONTH)
            showMonthCalendar();
        else
            showWeekCalendar();
    }
    
    protected void updateMonthYearLabel()
    { 
        monthYearLabel.setText( currentDate.getMonth().toString() +
                                ", " + currentDate.getYear() );
    }

    
    @Override
    public void reset()
    {
        handleCalendarMonth();
    }
       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        System.out.println("Initializing: Calendar");
        
        // Set to current date
        currentDate = LocalDate.now();
        updateMonthYearLabel();
        
        // Populate calendarContainer
        drawCalendar();
    }    
    
}
