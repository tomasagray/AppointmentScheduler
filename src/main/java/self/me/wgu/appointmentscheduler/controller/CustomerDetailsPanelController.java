/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.model.Customer;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class CustomerDetailsPanelController implements Initializable {

  private Customer customer;

  @FXML
  private Label customerName;
  @FXML
  private Label address;
  @FXML
  private Label customerActive;
  @FXML
  private Label phone;

  @FXML
  private void handleEditButton() {
    CustomersController cc = SceneManager.getInstance()
        .getLoader("Customers")
        .getController();
    cc.handleEditCustomer(customer);
  }

  public void populateDetailsPanel(Customer customer) {
    // Save Customer object,
    // associate it with this panel
    this.customer = customer;

    // Populate TextFields
    customerName.setText(customer.getCustomerName());
    customerActive.setText(customer.isActiveString());
    address.setText(
        customer.getAddress().toMultiLineString()
    );
    phone.setText(customer.getAddress().getPhone());
  }

  /**
   * Initializes the controller class.
   *
   * @param url URL
   * @param rb  ResourceBundle
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: CustomerDetailsPanel");
  }

}
