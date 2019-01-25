/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler;

import java.io.*;
import java.util.*;
import self.me.wgu.appointmentscheduler.Model.SettingsData;
import self.me.wgu.appointmentscheduler.Model.User;

/**
 *
 * @author tomas
 */
public class SettingsManager 
{
    private final static SettingsManager instance = new SettingsManager();
    

    // Application Settings    
    // -------------------------------------------------------------------------
    //  Size of CalendarHour box in Weekly view
    public final static double CALENDAR_HOUR_HEIGHT = 75.0;
    // Preferred main UI dimensions 
    public final static double WINDOW_HEIGHT = 750.0;
    public final static double WINDOW_WIDTH = 1_300.0;
    // Login screen dimensions
    public final static double LOGIN_WINDOW_HEIGHT = 450.0;
    public final static double LOGIN_WINDOW_WIDTH = 300.0;
    // Log file location
    public final static String LOG_FILE = "logfile.txt";
    // Settings file
    public final static String SETTINGS_FILE = "settings.dat";
   

    // Instance variables
    private User user;
    // Load default settings
    private SettingsData settingsData = new SettingsData();
    
    private SettingsManager() {}
    
    public final static SettingsManager getInstance()
    {
        return instance;
    }
    
    // Getters 
    // -------------------------------------------------------------------------
    public ResourceBundle getMessages()
    {
        return ResourceBundle.getBundle(
                "MessagesBundle", 
                settingsData.getLanguage().getLocale()
        );
    }
    public User getUser()
    {
        return user;
    }
    public SettingsData getSettingsData()
    {
        return this.settingsData;
    }
    
    public void loadSettingsData()
    {   
        // Overwrite default settings if a settings file exists
        Optional<SettingsData> settings = this.loadSettingsFile();
        if( settings.isPresent() )
            this.settingsData = settings.get(); 
    }
 
    
    // Setters
    // -------------------------------------------------------------------------
    public void setUser(User user)
    {
        this.user = user;
    }
    
    
    /**
     * Reads the settings file and loads the data into a SettingsData
     * object, which it returns.
     * @return 
     */
    private Optional<SettingsData> loadSettingsFile()
    {
        SettingsData settings = null;
        
        try( 
            FileInputStream fis = new FileInputStream (
                                     new File( SETTINGS_FILE ));
            ObjectInputStream ois = new ObjectInputStream(fis);
        )
        {
            settings = (SettingsData)ois.readObject();
        } catch(FileNotFoundException e) {
            // Do nothing;
        } catch(ClassNotFoundException | IOException e) {
            System.out.println("There was a problem reading the settings file.");
            System.out.println(e.getMessage());
        }
        
        return Optional.ofNullable(settings);
    }
    
    /**
     * Writes the SettingsData object to a file.
     * @param settings
     */
    public void writeSettingsFile(SettingsData settings)
    {
        try(
            FileOutputStream fos = new FileOutputStream(
                                    new File( SETTINGS_FILE ));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(settings);
            
            // No exceptions; update current SettingsData in memory
            this.settingsData = settings;
            
        } catch( IOException e) {
            System.out.println("There was a problem writing settings file.");
            System.out.println(e.getMessage());
        }
    }
}