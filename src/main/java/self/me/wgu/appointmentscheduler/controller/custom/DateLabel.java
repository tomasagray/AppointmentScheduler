/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import javafx.scene.control.Label;

/**
 * @author tomas
 */
public class DateLabel extends Label {

  public DateLabel(int date) {
    super();
    this.setText(date + "");
  }
}
