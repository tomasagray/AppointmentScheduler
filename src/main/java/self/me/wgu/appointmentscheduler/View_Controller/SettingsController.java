/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller;

import self.me.wgu.appointmentscheduler.Resettable;
import self.me.wgu.appointmentscheduler.StartAfterEndException;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;
import javafx.scene.control.*;
import self.me.wgu.appointmentscheduler.AppointmentScheduler;
import self.me.wgu.appointmentscheduler.Model.SettingsData;
import self.me.wgu.appointmentscheduler.SettingsManager;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class SettingsController implements Initializable, Resettable 
{
    
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
    private void handleSaveSettings()
    {
        if( validateSettingsForm() )
        {
            SettingsData settings = new SettingsData();
            settings.setOffice( (SettingsData.Office)office.getValue() );
            settings.setLanguage(( SettingsData.Language)language.getValue() );
            settings.setStartOfBusiness( 
                    ((LocalTime)startOfBusiness.getValue()).getHour() 
            );
            settings.setEndOfBusiness( 
                    ((LocalTime)endOfBusiness.getValue()).getHour()
            );

            // Save settings
            SettingsManager.getInstance().writeSettingsFile(settings);
            errorMessages.setText("Settings saved.");
        }
    }
    
    @FXML
    private void handleResetSettings()
    {
        System.out.println("Reset settings");
        reset();
    }
    
    public void populateSettingsForm()
    {
        // Reset
        // ---------------------------------------------------------------------
        office.getItems().clear();
        language.getItems().clear();
        startOfBusiness.getItems().clear();
        endOfBusiness.getItems().clear();
        
        
        // Timezones for each office location 
        // ---------------------------------------------------------------------
        List<SettingsData.Office> offices = new ArrayList<>();
        for( SettingsData.Office office : SettingsData.Office.values() )
            offices.add(office);
        offices.stream().sorted((a,b) -> {
            return a.toString().compareTo(b.toString());
        }).forEach((of) -> {
            office.getItems().add(of);
        });

        
        // Languages - alphabetized
        // ---------------------------------------------------------------------
        List<SettingsData.Language> languages = new ArrayList<>();
        for( SettingsData.Language lang : SettingsData.Language.values() )
            languages.add(lang);
        languages.stream().sorted((a, b) -> {
            return a.toString().compareTo(b.toString());
        }).forEach((lc) -> {
            language.getItems().add(lc);
        });
        
        // Business hours
        // ---------------------------------------------------------------------
        for(int i=0; i < 24; i++)
        {
            startOfBusiness.getItems().add( LocalTime.of(i, 0));
            endOfBusiness.getItems().add( LocalTime.of(i, 0));
        }
    }
    
    public boolean validateSettingsForm()
    {
        boolean formValidated = false;
        
        try {
            int start = ((LocalTime)startOfBusiness.getValue()).getHour();
            int end = ((LocalTime)endOfBusiness.getValue()).getHour();
            
            if(end < start)
                throw new StartAfterEndException();
            else
                formValidated = true;
            
        } catch(RuntimeException e) {
            errorMessages.setText(e.getMessage());
        }
        
        return formValidated;
    }
    
    @Override
    public void reset()
    {
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
    public void initialize(URL url, ResourceBundle rb) 
    {
        System.out.println("Initializing: Settings");
        
        // Setup ListCells
        // ---------------------------------------------------------------------
        office.setCellFactory((combobox) -> {
           return new ListCell<SettingsData.Office>() 
           {
               @Override
               protected void updateItem(SettingsData.Office item, boolean empty)
               {
                   super.updateItem(item, empty);
                   
                   if(item == null || empty)
                       setText(null);
                   else
                       setText(item.toString());
               }
           };
        });
        language.setCellFactory((combobox) -> {
           return new ListCell<SettingsData.Language>() 
           {
               @Override
               protected void updateItem(SettingsData.Language item, boolean empty)
               {
                   super.updateItem(item, empty);
                   
                   if(item == null || empty)
                       setText(null);
                   else
                       setText(item.toString());
               }
           };
        });
        startOfBusiness.setCellFactory((combobox) -> {
           return new ListCell<LocalTime>() 
           {
               @Override
               protected void updateItem(LocalTime item, boolean empty)
               {
                   super.updateItem(item, empty);
                   
                   if(item == null || empty)
                       setText(null);
                   else
                       setText(AppointmentScheduler.formatTime(item.getHour()));
               }
           };
        });
        endOfBusiness.setCellFactory((combobox) -> {
           return new ListCell<LocalTime>() 
           {
               @Override
               protected void updateItem(LocalTime item, boolean empty)
               {
                   super.updateItem(item, empty);
                   
                   if(item == null || empty)
                       setText(null);
                   else
                       setText(AppointmentScheduler.formatTime(item.getHour()));
               }
           };
        });
        
        // Setup StringCoverters
        // ---------------------------------------------------------------------
        office.setConverter(
        new StringConverter<SettingsData.Office>()
        {
            @Override
            public String toString(SettingsData.Office zone)
            {
                if(zone == null)
                    return null;
                else
                    return zone.toString();
            }
            @Override
            public SettingsData.Office fromString(String str)
            {
                return null;
            }
        });
        language.setConverter(
        new StringConverter<SettingsData.Language>()
        {
            @Override
            public String toString(SettingsData.Language lang)
            {
                if(lang == null)
                    return null;
                else
                    return lang.toString();
            }
            @Override
            public SettingsData.Language fromString(String str) {
                return null;
            }
        });
        startOfBusiness.setConverter(
        new StringConverter<LocalTime>()
        {
           @Override
           public String toString(LocalTime time)
           {
               if( time == null)
                   return null;
               else
                   return AppointmentScheduler.formatTime( time.getHour() );
           }
           @Override
           public LocalTime fromString(String str) {
               return null;
           }
        });
        endOfBusiness.setConverter(
        new StringConverter<LocalTime>()
        {
           @Override
           public String toString(LocalTime time)
           {
               if( time == null)
                   return null;
               else
                   return AppointmentScheduler.formatTime( time.getHour() );
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
