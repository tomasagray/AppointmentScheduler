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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
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
public class NewCustomerPanelController implements Initializable {

  // The customer represented by this form
  private final Customer customer = new Customer();

  @FXML
  private TextField customerName;
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
  public void handleSaveCustomer() {
    if (validateNewCustomer()) {
      // Add new customer record to DB
      MySQLConnection.getInstance().addCustomers(this.customer);
      // Reset error messages
      errorMessage.setText("");

      // Return to customer details panel
      CustomersController cc = SceneManager.getInstance()
          .getLoader("Customers")
          .getController();
      // Re-populate customer list with updated data
      cc.populateCustomerList();
      cc.handleCustomerDetails(this.customer);
    } else {
      System.out.println("CUSTOMER NOT VALIDATED");
    }
  }

  @FXML
  public void handleCancel() {
    System.out.println("CANCEL");
    CustomersController cc = SceneManager.getInstance()
        .getLoader("Customers")
        .getController();
    cc.handleDefault();
  }

  public boolean validateNewCustomer() {
    try {

      if ( // Validate TextFields
          customerName.getText().equals("")
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
   * Extract data from the new customer panel form and store it in the local instance variable.
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

    this.customer.setCustomerName(customerName.getText());
    this.customer.setAddress(tmpAddress);
    this.customer.setActive(customerIsActive.isSelected());
  }

  /**
   * Resets the form to empty
   */
  public void reset() {
    // Reset TextFields
    customerName.clear();
    address.clear();
    address2.clear();
    postalCode.clear();
    customerPhone.clear();
    customerPhone.clear();

    // Reset ComboBoxes
    city.getSelectionModel().select(-1);
    country.getSelectionModel().select(-1);
  }


  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: NewCustomerPanel");

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
