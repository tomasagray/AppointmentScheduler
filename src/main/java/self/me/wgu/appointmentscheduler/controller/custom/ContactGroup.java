/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import java.util.List;
import javafx.scene.layout.VBox;

/**
 * @author tomas
 */
public class ContactGroup extends VBox {

  public ContactGroup(Character ch, List<ContactListing> contacts) {
    super();
    this.getChildren().add(new ContactAnchor(ch));
    contacts.forEach(c -> this.getChildren().add(c));
  }
}
