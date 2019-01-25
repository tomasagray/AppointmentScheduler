/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import javafx.scene.control.Hyperlink;
import self.me.wgu.appointmentscheduler.Model.Appointment;
import self.me.wgu.appointmentscheduler.SceneManager;

/**
 *
 * @author tomas
 */
public class AppointmentLabel extends Hyperlink
{
    private Appointment appointment;
    
    public AppointmentLabel(Appointment appt)
    {
        super();
        this.appointment = appt;
        this.setText(this.appointment.getTitle());
        // Default behavior
        this.setOnMouseClicked( e -> {
            SceneManager.getInstance().showAppointmentDialog(e, appt);
        });
    }
}
