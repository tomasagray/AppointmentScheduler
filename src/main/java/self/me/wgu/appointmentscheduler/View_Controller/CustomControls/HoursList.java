/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author tomas
 */
public class HoursList extends VBox
{
    public HoursList()
    {
        super();
        for(int i=0; i<=23; i++)
            this.getChildren().add( new Hour(i));
    }
    
    private final class Hour extends VBox
    {
        private final String hour;
        private Hour(int hour)
        {
            super();
            
            if(hour == 12)
                this.hour = "12p";
            else if(hour == 0)
                this.hour = "12a";
            else if(hour > 12)
                this.hour = (hour - 12) + "p";
            else
                this.hour = hour + "a";
            
            this.setMinHeight(75);
            this.setPrefHeight(75);
            this.setStyle("-fx-border-color: cyan");
            this.getChildren().add( new Label(this.hour));
        }
    }
}
