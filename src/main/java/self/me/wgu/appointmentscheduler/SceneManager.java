/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler;


import java.io.IOException;
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import self.me.wgu.appointmentscheduler.model.Appointment;
import self.me.wgu.appointmentscheduler.controller.CalendarController;
import self.me.wgu.appointmentscheduler.controller.UIContainerController;

/**
 * Singleton class to manage the different scenegraphs 
 * of the application, and to facilitate communication 
 * between their controllers.
 * 
 * @author tomas
 */
public final class SceneManager 
{
    /**
     * Nested class to contain associated FXMLLoader objects
     * and their data (as Parent objects).
     */
    public static class SceneNode
    {
        private final FXMLLoader loader;
        private final Parent parent;
        private final Scene scene;
        
        public SceneNode(FXMLLoader loader, Parent parent)
        {
            this.loader = loader;
            this.parent = parent;
            this.scene = new Scene(parent);
        }
        public Parent getParent() {
            return this.parent;
        }
        public FXMLLoader getLoader() {
            return this.loader;
        }
        public Scene getScene()
        {
            return this.scene;
        }
    }
    
    // Singleton instance
    private static final SceneManager instance = new SceneManager();
    
    // Fields
    private final Map<String, SceneNode> scenes = new HashMap<>();
    private Stage main;
    private final Stage appointmentDlg = new Stage(StageStyle.UNDECORATED);
//    private final AddAppointmentDialogController appointmentDlgController=null;
    
    // View mode definition
    public enum ViewMode { WEEK, MONTH }

    private SceneManager()
    {
        
        // Setup special add appointment modal dialog
        appointmentDlg.initOwner(main);
        appointmentDlg.initModality(Modality.NONE); 
        // Default; LoginManager will change when user logs in
        appointmentDlg.setResizable(false);
        // Close dialog if user clicks outside of it
        appointmentDlg.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (! isNowFocused) {
                    this.closeAppointmentDialog();
                }
            });
    }
    
    
    public static SceneManager getInstance() 
    {
        return instance;
    }
    
    public synchronized void setStage( Stage stage )
    {
        main = stage;
    }   
    
    public synchronized void setRootScene( String name )
    {
        SceneNode node = scenes.get( name );
        System.out.println("Activating " + name);
        
        if( node != null )
        {
                main.setScene(node.getScene());
        }
    }
    
    public synchronized Stage getStage()
    {
        return main;
    }
    
    public synchronized Parent getScene( String name )
    { 
        return scenes.get(name).getParent();
    }
    public synchronized Map<String, SceneNode> getScenes()
    {
        return this.scenes;
    }
    
    public synchronized FXMLLoader getLoader( String name )
    {
        return scenes.get(name).getLoader();
    }
    
    public synchronized void addScene( String name, String url ) throws IOException {

        System.out.println("Class: " + getClass());
        System.out.println("Resource: " + getClass().getResource(url));
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            Parent p = loader.load();
            SceneNode node = new SceneNode(loader, p);
            scenes.put( name, node );

    }
    
    public synchronized boolean removeScene( String name )
    {
        SceneNode node = scenes.get( name );
        if( node != null ) 
            return scenes.remove(name, node );
        
        // Nothing was found
        return false;
    }

     public final void showAppointmentDialog(MouseEvent e, Appointment a)
    {
        
        // Get dimensions
        double dlgWidth = appointmentDlg.getWidth();
        double dlgHeight = appointmentDlg.getHeight();
        double eventX = e.getScreenX();
        double eventY = e.getScreenY();
        double widthBound = main.getX() + main.getWidth();
        double heightBound = main.getY() + main.getHeight();
        
        // Prevent dialog from overflowing main window;
        // if it will, reflect it across mouse pointer
        double X = dlgWidth + eventX > widthBound ? eventX - dlgWidth : eventX;
        double Y = dlgHeight + eventY > heightBound ? eventY - dlgHeight : eventY;
        
        // Position and display add appointment dialog
        appointmentDlg.setX(X);
        appointmentDlg.setY(Y);
//        appointmentDlgController.setAppointment(a);
//        appointmentDlgController.populateAppointmentDialog();
        appointmentDlg.showAndWait();
    }
     
    public final void closeAppointmentDialog()
    {
        // Reset and close modal
//        appointmentDlgController.reset();
        appointmentDlg.close();
        
        // Re-draw calendar
        CalendarController cc = getLoader("Calendar").getController();
        cc.drawCalendar();
    }
    
    public void initializeUIContainer()
    {
        
        setRootScene("UIContainer");
        
        recenterWindow(
                SettingsManager.WINDOW_HEIGHT,
                SettingsManager.WINDOW_WIDTH
        );
        getStage().setResizable(true);

        // Load the default view: Month Calendar
        UIContainerController uiCC = scenes.get("UIContainer")
                                                .getLoader().getController();
        uiCC.handleCalendarButton();  
    }
    
    public void initializeLoginForm()
    {
        // Return to login screen
        setRootScene("Login");
        recenterWindow(
                SettingsManager.LOGIN_WINDOW_HEIGHT,
                SettingsManager.LOGIN_WINDOW_WIDTH
        );
        getStage().setResizable(false);

    }
     
    public void recenterWindow(double prefHeight, double prefWidth)
    {
       
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        double horizontalCenter = screenSize.getWidth()/2;
        double verticalCenter = screenSize.getHeight()/2;
        double windowHCenter = prefWidth / 2; 
        double windowVCenter = prefHeight / 2; 
        
        double centerH = horizontalCenter - windowHCenter;
        double centerV = verticalCenter - windowVCenter;
        
        // Reposition window
        main.setHeight(prefHeight);
        main.setWidth(prefWidth);
        main.setX(centerH);
        main.setY(centerV);
    }

}
