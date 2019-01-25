/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import java.time.LocalDate;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author tomas
 */
public class WeekDateHeading extends AnchorPane 
{
    private LocalDate date;
    
    public WeekDateHeading(LocalDate date)
    {
        super();
        this.date = date;
        // Add current day styles, where appropriate
        if( this.date.equals(LocalDate.now()) )
            this.getStyleClass().add("currentDay");
        else
            this.setStyle("-fx-border-color: cyan");
        
        DateLabel dl = new DateLabel(this.date.getDayOfMonth());
        AnchorPane.setTopAnchor(dl, 10.0);
        AnchorPane.setLeftAnchor( dl, 5.0);
        this.getChildren().add( dl );
    }
}
