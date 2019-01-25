/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller.CustomControls;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class ContactAnchor extends Button
{
    
    public ContactAnchor(String label)
    { 
        super();
        this.setText(label);
        this.getStyleClass().add("contactAnchor");
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent e) {
                Button btn = (Button)e.getSource();
            }
        });
    }
    public ContactAnchor(Character c)
    {
        this(c.toString());
    }
    
}
