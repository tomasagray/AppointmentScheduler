/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import javafx.scene.control.Label;

/**
 *
 * @author tomas
 */
public class DateLabel extends Label
{
    private int date;
    
    public DateLabel( int date )
    {
        super();
        this.date = date;
        this.setText( date +"" );
    }
}
