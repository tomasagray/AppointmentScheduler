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
import self.me.wgu.appointmentscheduler.model.Customer;
import self.me.wgu.appointmentscheduler.SceneManager;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class CustomerDetailsPanelController implements Initializable
{
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
    private void handleEditButton()
    {
        CustomersController cc = SceneManager.getInstance()
                                            .getLoader("Customers")
                                            .getController();
        cc.handleEditCustomer(customer);
    }
    public void populateDetailsPanel(Customer customer)
    {
        // Save Customer object, 
        // associate it with this panel
        this.customer = customer;
        
        // Populate TextFields
        customerName.setText( customer.getCustomerName() );
        customerActive.setText( customer.isActiveString() );
        address.setText( 
                  customer.getAddress().toMultiLineString()
        );
        phone.setText( customer.getAddress().getPhone() );
    }
    
    /**
     * Initializes the controller class.
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("Initializing: CustomerDetailsPanel");
    }    
    
}
