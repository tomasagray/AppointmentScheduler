/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

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
            this.getChildren().add(new Hour(i));
    }
    
    private static final class Hour extends VBox
    {

      private Hour(int hour)
        {
            super();

          String hour1;
          if(hour == 12)
                hour1 = "12p";
            else if(hour == 0)
                hour1 = "12a";
            else if(hour > 12)
                hour1 = (hour - 12) + "p";
            else
                hour1 = hour + "a";
            
            this.setMinHeight(75);
            this.setPrefHeight(75);
            this.setStyle("-fx-border-color: cyan");
            this.getChildren().add( new Label(hour1));
        }
    }
}
