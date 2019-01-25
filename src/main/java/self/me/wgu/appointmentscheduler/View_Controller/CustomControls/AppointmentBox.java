/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import self.me.wgu.appointmentscheduler.Model.Appointment;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;

/**
 *
 * @author tomas
 */
public class AppointmentBox extends VBox
{
    private Label label;
    private Appointment appointment;
    
    public AppointmentBox()
    {
        super();
        
        // Set box click behavior
        this.setOnMouseClicked(e -> {
            SceneManager.getInstance().showAppointmentDialog(e, appointment);
        });
    }
    public AppointmentBox(Appointment appointment)
    {
        this();
        this.appointment = appointment;
        // Title label
        Label title = new Label( appointment.getTitle() );
        title.getStyleClass().add("appointmentBoxTitle");
        
        this.getChildren().add(title);
        this.getChildren().add(new Label( appointment.getDescription()));
        
        // Setup box dimensions
        double height = SettingsManager.CALENDAR_HOUR_HEIGHT * appointment.getDuration().toHours();
        this.setMinHeight(height );
        this.setPrefHeight(height );
        this.setMaxHeight(height);
        
        // **************** FOR TYPE ******************** \\
        switch( appointment.getTitle().toUpperCase() ) 
        {
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
