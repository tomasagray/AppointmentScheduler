/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler;

/**
 *
 * @author tomas
 */
public class DoubleBookedAppointmentException extends RuntimeException
{
    @Override
    public String getMessage()
    {
        return "There is already an appointment during this time.";
    }
    
}