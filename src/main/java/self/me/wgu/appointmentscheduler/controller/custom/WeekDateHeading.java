/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import java.time.LocalDate;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author tomas
 */
public class WeekDateHeading extends AnchorPane 
{

  public WeekDateHeading(LocalDate date)
    {
        super();
      // Add current day styles, where appropriate
        if( date.equals(LocalDate.now()) )
            this.getStyleClass().add("currentDay");
        else
            this.setStyle("-fx-border-color: cyan");
        
        DateLabel dl = new DateLabel(date.getDayOfMonth());
        AnchorPane.setTopAnchor(dl, 10.0);
        AnchorPane.setLeftAnchor( dl, 5.0);
        this.getChildren().add( dl );
    }
}
