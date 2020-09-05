/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.controller.custom;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import self.me.wgu.appointmentscheduler.AppointmentScheduler;
import self.me.wgu.appointmentscheduler.DoubleBookedAppointmentException;
import self.me.wgu.appointmentscheduler.EmptyFieldException;
import self.me.wgu.appointmentscheduler.InvalidCustomerException;
import self.me.wgu.appointmentscheduler.MySQLConnection;
import self.me.wgu.appointmentscheduler.OutsideBusinessHoursException;
import self.me.wgu.appointmentscheduler.Resettable;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;
import self.me.wgu.appointmentscheduler.StartAfterEndException;
import self.me.wgu.appointmentscheduler.model.Appointment;
import self.me.wgu.appointmentscheduler.model.Customer;

/**
 * FXML Controller class to control the Add Appointment popup.
 *
 * @author tomas
 */
public class AddAppointmentDialogController implements Initializable, Resettable {

  // Already existing appointments for today; loaded in initialize()
  List<Appointment> appointments;
  private Appointment appointment = new Appointment.AppointmentBuilder().build();
  @FXML
  private Button closeDialogButton;
  @FXML
  private TextField appointmentName;
  @FXML
  private ComboBox appointmentStart;
  @FXML
  private ComboBox appointmentEnd;
  @FXML
  private ComboBox customerNames;
  @FXML
  private TextField appointmentLocation;
  @FXML
  private TextField appointmentDescription;
  //    @FXML
//    private TextField appointmentType;
  @FXML
  private TextField appointmentURL;
  @FXML
  private Label errorMessage;
  @FXML
  private Button deleteAppointmentButton;

  @FXML
  public void handleCloseDialog() {
    reset();        // Reset the modal
    SceneManager.getInstance().closeAppointmentDialog();
  }

  @FXML
  public void handleDoneButton() {
    if (validateAppointmentDialog()) {
      // Update the appointment being modelled by this
      // dialog
      updateLocalAppointment();

      // Are we adding a new appointment, or are
      // we updating an existing entry?
      if (this.appointment.getAppointmentID() == Appointment.DEFAULT_ID) {
        MySQLConnection.getInstance().addAppointments(appointment);

      } else {    // Non-default ID; we are updating
        // a pre-existing appointment
        MySQLConnection.getInstance().updateAppointment(appointment);
      }

      // Close up shop
      handleCloseDialog();
    }
  }

  @FXML
  public void handleDeleteAppointment() {
    MySQLConnection.getInstance().deleteAppointment(appointment);
    handleCloseDialog();
  }

  public void setAppointment(Appointment appointment) {
    this.appointment = appointment;
    // Get appointments for this day
    appointments = MySQLConnection.getInstance()
        .getAppointments(
            appointment.getStartDate(),
            appointment.getEndDate().plusDays(1)
        );
  }

  public void populateAppointmentDialog() {
    // Populate text fields; can be null/default
    appointmentName.setText(this.appointment.getTitle());
    appointmentLocation.setText(this.appointment.getLocation());
    appointmentDescription.setText(this.appointment.getDescription());
    appointmentURL.setText(this.appointment.getUrl());

    // Reset customer names data
    customerNames.getItems().clear();
    // Re-populate customers list with current data from DB
    List<Customer> customers = MySQLConnection.getInstance().getCustomers();
    // Add them to the ComboBox
    customers.forEach(customerNames.getItems()::add);

    // Select items from combo boxes; check to ensure no null values
    if (appointment.hasCustomer()) {
      customerNames.getSelectionModel()
          .select(this.appointment.getCustomer());
    }

    if (appointment.isStartSet()) {
      appointmentStart.getSelectionModel()
          .select(Integer.valueOf(this.appointment.getStart().getHour()));
    }

      if (appointment.isEndSet()) {
          appointmentEnd.getSelectionModel()
              .select(Integer.valueOf(this.appointment.getEnd().getHour()));
      }

    // Show delete appointment button only if a pre-existing appointment
    // is loaded
      if (appointment.getAppointmentID() != Appointment.DEFAULT_ID) {
          deleteAppointmentButton.setVisible(true);
      }
  }

  /**
   * Extract data from the modal dialog and store it in the Appointment instance variable.
   */
  public void updateLocalAppointment() {
    this.appointment.setTitle(appointmentName.getText());
    this.appointment.setDescription(appointmentDescription.getText());
    this.appointment.setLocation(appointmentLocation.getText());
    this.appointment.setUrl(appointmentURL.getText());
    this.appointment.setStart(appointment.getStart()
        .withHour((Integer) appointmentStart.getValue())
    );
    this.appointment.setEnd(appointment.getEnd()
        .withHour((Integer) appointmentEnd.getValue())
    );
    this.appointment.setCustomer((Customer) customerNames.getSelectionModel().getSelectedItem());
  }

  public boolean validateAppointmentDialog() {
    // Reset error message display
    errorMessage.setText("");

    try {
      // Validate TextFields
      // Will be NULL if untouched, "" if the user has clicked
      // on them, but they remain empty.
      // -----------------------------------------------------------------
      if (
          appointmentName.getText() == null
              || appointmentName.getText().equals("")
              || appointmentDescription.getText() == null
              || appointmentDescription.getText().equals("")
              || appointmentLocation.getText() == null
              || appointmentLocation.getText().equals("")
              || appointmentURL.getText() == null
              || appointmentURL.getText().equals("")) {
        throw new EmptyFieldException();
      }

      // Validate URL; throws exception
      // -----------------------------------------------------------------
      URL testURL = new URL(appointmentURL.getText());

      // Validate appointment times
      // -----------------------------------------------------------------
      int start = (Integer) appointmentStart.getValue();
      int end = (Integer) appointmentEnd.getValue();

      // Illogical start and end times
        if (start >= end) {
            throw new StartAfterEndException();
        }

      // Appointment already scheduled during given times?
      appointments.stream().filter((Appointment appt) -> {
        // Don't compare modified appointment to itself
        return appt.getAppointmentID() != appointment.getAppointmentID();
      }).forEach(appt -> {
        // Ensure no overlapping appointments
        if ((start < appt.getStart().getHour() && end > appt.getStart().getHour())
            ||
            (start < appt.getEnd().getHour() && end > appt.getEnd().getHour())
            ||
            (start <= appt.getStart().getHour() && end >= appt.getEnd().getHour())) {
          throw new DoubleBookedAppointmentException();
        }
      });

      // Ensure appointment is during business hours (as specified
      // in settings. It should not be possible to do this, but...
        if (start < SettingsManager.getInstance()
            .getSettingsData().getStartOfBusiness()
            || end > SettingsManager.getInstance()
            .getSettingsData().getEndOfBusiness()) {
            throw new OutsideBusinessHoursException();
        }

      // Ensure a valid customer contact is selected
      // -----------------------------------------------------------------
      if (customerNames.getSelectionModel().getSelectedItem() != null) {
        // Check that the selected user is actually in the DB
        Customer selectedCustomer = (Customer) customerNames
            .getSelectionModel()
            .getSelectedItem();
        Customer dbCustomer = MySQLConnection.getInstance()
            .getCustomer(selectedCustomer.getCustomerID());
          if (dbCustomer == null) {
              throw new InvalidCustomerException();
          }

      } else {
        throw new InvalidCustomerException();
      }

    } catch (RuntimeException e) {
      errorMessage.setText(e.getMessage());
      return false;
    } catch (MalformedURLException e) {
      errorMessage.setText("Please enter a valid URL.");
      return false;
    }

    // We made it! Everything looks good
    return true;
  }


  /**
   * Resets the modal. Returns all text fields to blank ("") and clears the selection of all combo
   * boxes.
   */
  @Override
  public void reset() {
    // Reset dialog
    appointmentName.setText("");
    appointmentLocation.setText("");
    appointmentDescription.setText("");
    appointmentURL.setText("");
    appointmentStart.getSelectionModel().clearAndSelect(0);
    appointmentEnd.getSelectionModel().clearAndSelect(1);
    customerNames.getSelectionModel().select(null);
    errorMessage.setText("");
    // Hide delete button from empty dialog
    deleteAppointmentButton.setVisible(false);
  }

  /**
   * Initializes the controller class.
   *
   * @param url URL
   * @param rb  ResourceBundle
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: AddAppointmentDialog");

    // Add only business hours (as defined in SettingsManager)
    // to the ComboBoxes
    int startOfBusiness = SettingsManager.getInstance()
        .getSettingsData().getStartOfBusiness();
    int endOfBusiness = SettingsManager.getInstance()
        .getSettingsData().getEndOfBusiness();
    IntStream hours = IntStream.rangeClosed(
        startOfBusiness, endOfBusiness
    );
    // Reset
    appointmentStart.getItems().clear();
    appointmentEnd.getItems().clear();
    // Add hours
    hours.forEachOrdered(i -> {
      appointmentStart.getItems().add(i);
      appointmentEnd.getItems().add(i);
    });

    // Setup cell factories
    // ---------------------------------------------------------------------
    appointmentStart.setCellFactory((combobox) -> new ListCell<Integer>() {
      @Override
      protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(AppointmentScheduler.formatTime(item));
          }
      }
    });
    appointmentEnd.setCellFactory((combobox) -> new ListCell<Integer>() {
      @Override
      protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(AppointmentScheduler.formatTime(item));
          }
      }
    });
    customerNames.setCellFactory((combobox) -> new ListCell<Customer>() {
      @Override
      protected void updateItem(Customer item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(item.getCustomerName());
          }
      }
    });
    // ---------------------------------------------------------------------

    // Setup rendering of selected values
    // ---------------------------------------------------------------------
    customerNames.setConverter(
        new StringConverter<Customer>() {
          @Override
          public String toString(Customer customer) {
              if (customer == null) {
                  return null;
              } else {
                  return customer.getCustomerName();
              }
          }

          @Override
          public Customer fromString(String customerString) {
            return null;        // Just because the interface wants it
          }
        });
    appointmentStart.setConverter(
        new StringConverter<Integer>() {
          @Override
          public String toString(Integer hour) {
              if (hour == null) {
                  return null;
              } else {
                  return AppointmentScheduler.formatTime(hour);
              }
          }

          @Override
          public Integer fromString(String str) {
            return null;
          }
        });
    appointmentEnd.setConverter(
        new StringConverter<Integer>() {
          @Override
          public String toString(Integer hour) {
              if (hour == null) {
                  return null;
              } else {
                  return AppointmentScheduler.formatTime(hour);
              }
          }

          @Override
          public Integer fromString(String str) {
            return null;
          }
        });
    // ---------------------------------------------------------------------
  }

}

