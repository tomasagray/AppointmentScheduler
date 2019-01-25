/**
 * Appointment Scheduler
 * -----------------------------------------------------------------------------
 * 
 * A multi-user calendar and appointment tracking software solution. Supports 
 * several languages and configuration options.
 * 
 * Developed for WGU course C195 - Advanced Java Concepts
 * 
 * @author tomas
 */


package self.me.wgu.appointmentscheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class AppointmentScheduler extends Application 
{
    @Override
    public void start(Stage stage) throws Exception 
    {
        // For Locale testing
        // ---------------------------------------------------------------------
//        Locale.setDefault(new Locale("es"));
//        Locale.setDefault(new Locale("ro"));
        
        try {
        
        // Load settings
        // ---------------------------------------------------------------------
        SettingsManager.getInstance().loadSettingsData();
        
        // Setup SceneManager
        // ---------------------------------------------------------------------
        SceneManager sm = SceneManager.getInstance();
        sm.setStage(stage);
        // Setup application title bar
        sm.getStage().setTitle("Appointment Scheduler");
        sm.getStage().getIcons().add(new Image("/View_Controller/res/icon.png") );
        
        // Load Customers screen sub-panels 
        // ---------------------------------------------------------------------
        sm.addScene(
                "NewCustomerPanel", "/View_Controller/NewCustomerPanel.fxml"
        );
        sm.addScene(
                "CustomerDetailsPanel", "/View_Controller/CustomerDetailsPanel.fxml"
        );
        sm.addScene(
                "EditCustomerPanel", "/View_Controller/EditCustomerPanel.fxml"
        );
        
        // Load Calendar views
        // ---------------------------------------------------------------------
        sm.addScene("MonthCalendar", "/View_Controller/CustomControls/MonthCalendar.fxml");
        sm.addScene("WeekCalendar", "/View_Controller/CustomControls/WeekCalendar.fxml");
        
        // Add main views to the SceneManager
        // ---------------------------------------------------------------------
        sm.addScene("Login", "/View_Controller/LoginForm.fxml" );
        sm.addScene("UIContainer", "/View_Controller/UIContainer.fxml");
        sm.addScene("Customers", "/View_Controller/Customers.fxml" );
        sm.addScene("Calendar", "/View_Controller/Calendar.fxml" );
        sm.addScene("Reports", "/View_Controller/Reports.fxml" );
        sm.addScene("Settings", "/View_Controller/Settings.fxml" );

        // Display login 
        // ---------------------------------------------------------------------
        sm.initializeLoginForm();
        sm.getStage().show();
        
        } catch( RuntimeException e ) {
            System.out.println("FATAL ERROR: Unable to load required application resources.");
            System.out.println( e.getMessage() );
            System.out.println("Exiting...");
            
            System.exit(1);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    /**
     * Helper method to transform an integer hour into
     * a more recognizable String, e.g.,
     *      14  -> "2:00 PM"
     * 
     * @param hour
     * @return 
     */
    public static String formatTime(int hour)
    {
        if(hour == 0)   // midnight
            return "12:00 AM";
        else if(hour < 12)   // morning,
            return hour + ":00 AM";
        else if(hour == 12) // noon,
            return "12:00 PM";
        else            // and night
            return (hour - 12) + ":00 PM";
    }
    
}
