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
package self.me.wgu.appointmentscheduler.controller.custom;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import self.me.wgu.appointmentscheduler.model.Customer;

/**
 * @author tomas
 */
public class ContactListing extends GridPane {

  private Customer customer;

  public ContactListing() {
    super();
    this.setPadding(new Insets(20));

    // Setup column spacing
    ColumnConstraints c1 = new ColumnConstraints();
    c1.setPercentWidth(40);
    c1.setFillWidth(true);
    ColumnConstraints c2 = new ColumnConstraints();
    c2.setPercentWidth(40);
    c2.setFillWidth(true);
    ColumnConstraints c3 = new ColumnConstraints();
    c3.setPercentWidth(20);
    c3.setFillWidth(true);
    this.getColumnConstraints().addAll(c1, c2, c3);
  }

  public ContactListing(Customer customer) {
    this();
    this.setCustomer(customer);
    this.setupContact(
        customer.getCustomerName(),
        customer.getAddress().toString(),
        customer.getAddress().getPhone()
    );
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  /**
   * Allows for custom repurposing of ContactListing
   *
   * @param name    Contact name
   * @param address Contact address
   * @param phone   Contact phone #
   */
  public void setupContact(String name, String address, String phone) {
    // Setup labels
    Label nameLabel = new Label(name);
    nameLabel.setPadding(new Insets(0, 10, 0, 10));
    Label addressLabel = new Label(address);
    addressLabel.setPadding(new Insets(0, 10, 0, 10));
    Label phoneLabel = new Label(phone);
    phoneLabel.setPadding(new Insets(0, 10, 0, 10));

    // Add data
    this.add(nameLabel, 0, 0);
    this.add(addressLabel, 1, 0);
    this.add(phoneLabel, 2, 0);
  }
}
