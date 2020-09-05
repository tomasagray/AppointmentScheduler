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
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import self.me.wgu.appointmentscheduler.EmptyFieldException;
import self.me.wgu.appointmentscheduler.MySQLConnection;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.model.Address;
import self.me.wgu.appointmentscheduler.model.City;
import self.me.wgu.appointmentscheduler.model.Country;
import self.me.wgu.appointmentscheduler.model.Customer;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class EditCustomerPanelController implements Initializable {

  // Customer object represented by this panel
  private Customer customer;

  @FXML
  private TextField customerNameField;
  @FXML
  private TextField address;
  @FXML
  private TextField address2;
  @FXML
  private ComboBox city;
  @FXML
  private ComboBox country;
  @FXML
  private TextField postalCode;
  @FXML
  private TextField customerPhone;
  @FXML
  private CheckBox customerIsActive;

  @FXML
  private Label errorMessage;

  @FXML
  private ImageView editPencil;
  @FXML
  private Button deleteCustomerButton;

  @FXML
  private void handleDeleteCustomer() {
    System.out.println("Delete customer: " + customer);
    MySQLConnection.getInstance().deleteCustomer(this.customer);

    CustomersController cc = SceneManager.getInstance()
        .getLoader("Customers")
        .getController();
    // Re-populate Customers list
    cc.populateCustomerList();
    // Return to default subpanel
    cc.handleDefault();
  }

  @FXML
  private void handleSaveEdit() {
    if (validateEditCustomer()) {
      // Update customer record
      MySQLConnection.getInstance().updateCustomer(this.customer);
      // Reset error messages
      errorMessage.setText("");

      // Return to customer details panel
      CustomersController cc = SceneManager.getInstance()
          .getLoader("Customers")
          .getController();
      // Re-populate customer list with updated data
      cc.populateCustomerList();
      cc.handleCustomerDetails(this.customer);
    }
  }

  @FXML
  private void handleCancelEdit() {
    // Clear error messages
    errorMessage.setText("");

    // Return to customer details panel
    CustomersController cc = SceneManager.getInstance()
        .getLoader("Customers")
        .getController();
    cc.handleCustomerDetails(this.customer);
  }

  public void populateEditCustomer(Customer customer) {
    // Associate Customer object with this sub-panel
    this.customer = customer;

    // Populate form TextFields
    customerNameField.setText(customer.getCustomerName());
    address.setText(customer.getAddress().getAddress());
    address2.setText(customer.getAddress().getAddress2());
    postalCode.setText(customer.getAddress().getPostalCode());
    customerPhone.setText(customer.getAddress().getPhone());

    // Set checkbox
    customerIsActive.setSelected(customer.isActive());

    // Populate ComboBoxes
    city.getSelectionModel().select(customer.getAddress().getCity());
    country.getSelectionModel().select(customer.getAddress().getCountry());

  }

  public boolean validateEditCustomer() {
    try {

      if (customerNameField.getText().equals("")
          || address.getText().equals("")
          || postalCode.getText().equals("")
          || customerPhone.getText().equals("")
          // Validate ComboBoxes
          || city.getSelectionModel().getSelectedItem() == null
          || country.getSelectionModel().getSelectedItem() == null) {
        throw new EmptyFieldException();
      }

      // We made it here; everything looks good!
      updateLocalCustomer();
      return true;
    } catch (RuntimeException e) {
      errorMessage.setText(e.getMessage());
    }

    // We made it here; something went wrong!
    return false;
  }

  /**
   * Extract data from the edit customer panel form and store it in the local instance variable.
   */
  public void updateLocalCustomer() {
    // Create a temporary address
    Address tmpAddress = this.customer.getAddress();
    tmpAddress.setAddress(address.getText());
    tmpAddress.setAddress2(address2.getText());
    tmpAddress.setCity((City) city.getSelectionModel().getSelectedItem());
    tmpAddress.setCountry((Country) country.getSelectionModel().getSelectedItem());
    tmpAddress.setPostalCode(postalCode.getText());
    tmpAddress.setPhone(customerPhone.getText());

    this.customer.setCustomerName(customerNameField.getText());
    this.customer.setAddress(tmpAddress);
    this.customer.setActive(customerIsActive.isSelected());
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: EditCustomerPanel");

    // Setup ComboBox cell factories
    // ---------------------------------------------------------------------
    city.setCellFactory((combobox) -> new ListCell<City>() {
      @Override
      protected void updateItem(City item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null) {
              this.setText("");
          } else {
              this.setText(item.getCityName());
          }
      }
    });
    country.setCellFactory((combobox) -> new ListCell<Country>() {
      @Override
      protected void updateItem(Country item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null) {
              this.setText("");
          } else {
              this.setText(item.getCountryName());
          }
      }
    });
    // ---------------------------------------------------------------------

    // Populate ComboBoxes
    // ---------------------------------------------------------------------
    List<City> cities = MySQLConnection.getInstance().getCities();
    List<Country> countries = MySQLConnection.getInstance().getCountries();
    cities.forEach(c -> city.getItems().add(c));
    countries.forEach(C -> country.getItems().add(C));
  }

}
