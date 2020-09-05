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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import self.me.wgu.appointmentscheduler.AppointmentScheduler;
import self.me.wgu.appointmentscheduler.Resettable;
import self.me.wgu.appointmentscheduler.SettingsManager;
import self.me.wgu.appointmentscheduler.StartAfterEndException;
import self.me.wgu.appointmentscheduler.model.SettingsData;
import self.me.wgu.appointmentscheduler.model.SettingsData.Language;
import self.me.wgu.appointmentscheduler.model.SettingsData.Office;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class SettingsController implements Initializable, Resettable {

  @FXML
  private ComboBox office;
  @FXML
  private ComboBox language;
  @FXML
  private ComboBox startOfBusiness;
  @FXML
  private ComboBox endOfBusiness;
  @FXML
  private Label errorMessages;


  @FXML
  private void handleSaveSettings() {
    if (validateSettingsForm()) {
      SettingsData settings = new SettingsData();
      settings.setOffice((SettingsData.Office) office.getValue());
      settings.setLanguage((SettingsData.Language) language.getValue());
      settings.setStartOfBusiness(
          ((LocalTime) startOfBusiness.getValue()).getHour()
      );
      settings.setEndOfBusiness(
          ((LocalTime) endOfBusiness.getValue()).getHour()
      );

      // Save settings
      SettingsManager.getInstance().writeSettingsFile(settings);
      errorMessages.setText("Settings saved.");
    }
  }

  @FXML
  private void handleResetSettings() {
    System.out.println("Reset settings");
    reset();
  }

  public void populateSettingsForm() {
    // Reset
    // ---------------------------------------------------------------------
    office.getItems().clear();
    language.getItems().clear();
    startOfBusiness.getItems().clear();
    endOfBusiness.getItems().clear();

    // Timezones for each office location
    // ---------------------------------------------------------------------
    List<SettingsData.Office> offices = new ArrayList<>();
    Collections.addAll(offices, Office.values());
    offices.stream().sorted(Comparator.comparing(Office::toString))
        .forEach((of) -> office.getItems().add(of));

    // Languages - alphabetized
    // ---------------------------------------------------------------------
    List<SettingsData.Language> languages = new ArrayList<>();
    Collections.addAll(languages, Language.values());
    languages.stream().sorted(Comparator.comparing(Language::toString))
        .forEach((lc) -> language.getItems().add(lc));

    // Business hours
    // ---------------------------------------------------------------------
    for (int i = 0; i < 24; i++) {
      startOfBusiness.getItems().add(LocalTime.of(i, 0));
      endOfBusiness.getItems().add(LocalTime.of(i, 0));
    }
  }

  public boolean validateSettingsForm() {
    boolean formValidated = false;

    try {
      int start = ((LocalTime) startOfBusiness.getValue()).getHour();
      int end = ((LocalTime) endOfBusiness.getValue()).getHour();

        if (end < start) {
            throw new StartAfterEndException();
        } else {
            formValidated = true;
        }

    } catch (RuntimeException e) {
      errorMessages.setText(e.getMessage());
    }

    return formValidated;
  }

  @Override
  public void reset() {
    // Select defaults
    // ---------------------------------------------------------------------
    office.getSelectionModel().select(
        SettingsManager.getInstance().getSettingsData()
            .getOffice()
    );
    language.getSelectionModel().select(
        SettingsManager.getInstance().getSettingsData()
            .getLanguage()
    );
    startOfBusiness.getSelectionModel().select(
        SettingsManager.getInstance().getSettingsData()
            .getStartOfBusiness()
    );
    endOfBusiness.getSelectionModel().select(
        SettingsManager.getInstance().getSettingsData()
            .getEndOfBusiness()
    );

    // Clear error messages
    errorMessages.setText("");
  }


  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: Settings");

    // Setup ListCells
    // ---------------------------------------------------------------------
    office.setCellFactory((combobox) -> new ListCell<Office>() {
      @Override
      protected void updateItem(Office item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(item.toString());
          }
      }
    });
    language.setCellFactory((combobox) -> new ListCell<Language>() {
      @Override
      protected void updateItem(Language item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(item.toString());
          }
      }
    });
    startOfBusiness.setCellFactory((combobox) -> new ListCell<LocalTime>() {
      @Override
      protected void updateItem(LocalTime item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(AppointmentScheduler.formatTime(item.getHour()));
          }
      }
    });
    endOfBusiness.setCellFactory((combobox) -> new ListCell<LocalTime>() {
      @Override
      protected void updateItem(LocalTime item, boolean empty) {
        super.updateItem(item, empty);

          if (item == null || empty) {
              setText(null);
          } else {
              setText(AppointmentScheduler.formatTime(item.getHour()));
          }
      }
    });

    // Setup StringCoverters
    // ---------------------------------------------------------------------
    office.setConverter(
        new StringConverter<SettingsData.Office>() {
          @Override
          public String toString(SettingsData.Office zone) {
              if (zone == null) {
                  return null;
              } else {
                  return zone.toString();
              }
          }

          @Override
          public SettingsData.Office fromString(String str) {
            return null;
          }
        });
    language.setConverter(
        new StringConverter<SettingsData.Language>() {
          @Override
          public String toString(SettingsData.Language lang) {
              if (lang == null) {
                  return null;
              } else {
                  return lang.toString();
              }
          }

          @Override
          public SettingsData.Language fromString(String str) {
            return null;
          }
        });
    startOfBusiness.setConverter(
        new StringConverter<LocalTime>() {
          @Override
          public String toString(LocalTime time) {
              if (time == null) {
                  return null;
              } else {
                  return AppointmentScheduler.formatTime(time.getHour());
              }
          }

          @Override
          public LocalTime fromString(String str) {
            return null;
          }
        });
    endOfBusiness.setConverter(
        new StringConverter<LocalTime>() {
          @Override
          public String toString(LocalTime time) {
              if (time == null) {
                  return null;
              } else {
                  return AppointmentScheduler.formatTime(time.getHour());
              }
          }

          @Override
          public LocalTime fromString(String str) {
            return null;
          }
        });

    populateSettingsForm();
    reset();
  }

}
