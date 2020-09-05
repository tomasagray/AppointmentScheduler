/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class ContactAnchor extends Button {

  public ContactAnchor(String label) {
    super();
    this.setText(label);
    this.getStyleClass().add("contactAnchor");
    this.setOnMouseClicked((EventHandler) e -> {
      Button btn = (Button) e.getSource();
    });
  }

  public ContactAnchor(Character c) {
    this(c.toString());
  }

}
