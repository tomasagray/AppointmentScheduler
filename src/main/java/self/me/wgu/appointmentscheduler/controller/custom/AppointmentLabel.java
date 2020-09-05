/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import javafx.scene.control.Hyperlink;
import self.me.wgu.appointmentscheduler.model.Appointment;
import self.me.wgu.appointmentscheduler.SceneManager;

/**
 *
 * @author tomas
 */
public class AppointmentLabel extends Hyperlink
{

    public AppointmentLabel(Appointment appt)
    {
        super();
        this.setText(appt.getTitle());
        // Default behavior
        this.setOnMouseClicked( e -> SceneManager.getInstance().showAppointmentDialog(e, appt));
    }
}
