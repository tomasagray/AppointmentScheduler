/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller;

import self.me.wgu.appointmentscheduler.Resettable;
import java.net.URL;
import java.util.*;
import static java.util.stream.Collectors.groupingBy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import self.me.wgu.appointmentscheduler.Model.Customer;
import self.me.wgu.appointmentscheduler.MySQLConnection;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.View_Controller.CustomControls.ContactGroup;
import self.me.wgu.appointmentscheduler.View_Controller.CustomControls.ContactListing;
/**
 * FXML Controller class
 *
 * @author tomas
 */
public class CustomersController implements Initializable, Resettable 
{
    
    @FXML
    private GridPane customersContainer;
    @FXML
    private ContactListing contactsHeading;
    @FXML
    private VBox customerList;
    @FXML
    private VBox customerPanel;

    // Sub-panel controllers
    private NewCustomerPanelController newCustomerController;
    private CustomerDetailsPanelController customerDetailsController;
    private EditCustomerPanelController editCustomerController;
    
    
    @FXML
    public void handleAddCustomer()
    {
        System.out.println("Add customer");
        customerPanel.getChildren().clear();
        customerPanel.getChildren().add( 
                SceneManager.getInstance().getScene("NewCustomerPanel")
        );
        // Reset New Customer panel
        NewCustomerPanelController npc = SceneManager.getInstance()
                                                     .getLoader("NewCustomerPanel")
                                                     .getController();
        npc.reset();
    }
    
    @FXML
    public void handleCustomerDetails(Customer customer)
    {
        customerPanel.getChildren().clear();
        customerDetailsController.populateDetailsPanel(customer);
        customerPanel.getChildren().add(
                SceneManager.getInstance().getScene("CustomerDetailsPanel")
        );
    }
    
    @FXML
    public void handleEditCustomer(Customer customer)
    {
        customerPanel.getChildren().clear();
        editCustomerController.populateEditCustomer(customer);
        customerPanel.getChildren().add(
                SceneManager.getInstance().getScene("EditCustomerPanel")
        );
    }

    /**
     * Fill the sidebar with default content
     * 
     */
    public void handleDefault()
    {
        customerPanel.getChildren().clear();
        Label defaultText = new Label("Please select a customer\nto the left.");
        customerPanel.getChildren().add( defaultText );
    }
    
    
    public void populateCustomerList()
    {
        // Reset Customer list
        customerList.getChildren().clear();
        
        // Sort into groups according to the first letter
        // of the customer's first name.
        Map<Character, List<Customer>> customers = MySQLConnection
                                .getInstance()
                                .getCustomers()
                                .stream()
                                .collect( 
                                    groupingBy( c -> c.getCustomerName().charAt(0) ) 
                                );

        // Add groups of contacts
        customers.forEach((ch, customersList) -> { 
            List<ContactListing> contacts = new ArrayList<>(); 
            // Setup and add each contact listing
            customersList.forEach(c -> { 
                ContactListing cl = new ContactListing(c);
                // Attach functionality to each contact
                cl.setOnMouseClicked(e -> {
                    
                    // In each ContactGroup...
                    customerList.getChildren().forEach((Node n) -> {
                        Parent p = (Parent)n;
                        // For each ContactListing...
                        p.getChildrenUnmodifiable().forEach(child -> {
                            // ... remove selection highlight
                            child.getStyleClass().remove("selected");
                        });
                    });
                    // Load customer data to subpanel
                    handleCustomerDetails(cl.getCustomer());
                    // Highlight selected customer
                    cl.getStyleClass().add("selected");
                    
                });
                contacts.add(cl);
            });
            // Add each group
            customerList.getChildren().add(new ContactGroup(ch, contacts));
        });
    }
    
    @Override
    public void reset()
    {
        // Initialize sidebar with default content
        handleDefault();
        // Populate customers
        populateCustomerList();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    { 
        System.out.println("Initializing: Customers");
        
        // Add customer listing header
        contactsHeading.setupContact("Name", "Address", "Phone");
        contactsHeading.setStyle("-fx-font-weight: 900");
        
        // Attach controllers
        newCustomerController = SceneManager.getInstance()
                                            .getLoader("NewCustomerPanel")
                                            .getController();
        customerDetailsController = SceneManager.getInstance()
                                            .getLoader("CustomerDetailsPanel")
                                            .getController();
        editCustomerController = SceneManager.getInstance()
                                             .getLoader("EditCustomerPanel")
                                             .getController();
        
        // Initialize 
        reset();
    }
    
}
