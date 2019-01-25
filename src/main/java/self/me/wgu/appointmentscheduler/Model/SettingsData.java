/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.Model;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
import self.me.wgu.appointmentscheduler.AppointmentScheduler;

/**
 *
 * @author tomas
 */



public class SettingsData implements Serializable
{
    public static enum Language 
    { 
        ENGLISH("English", "en"), 
        SPANISH("Español", "es"), 
        ROMANIAN("Română", "ro");
        
        private final String name;
        private final Locale locale;
        Language(String lang, String languageTag)
        {
            this.name = lang;
            locale = Locale.forLanguageTag(languageTag);
        }
        @Override
        public String toString()
        {
            return name;
        }
        public Locale getLocale()
        {
            return this.locale;
        }
    }
    
    public static enum Office 
    {
        PHOENIX("America/Phoenix"),
        NEW_YORK("America/New_York"),
        LONDON("Europe/London");
        
        private final TimeZone timezone;
        private final String name;
        private final ZoneId zone;
        Office()
        {
            this(ZoneId.systemDefault().getId());
        }
        Office(String zoneID)
        {
            this.zone = ZoneId.of(zoneID);
            this.timezone = TimeZone.getTimeZone( zone );
            String[] str = zoneID.split("/");
            name = str[1].replace('_', ' ');
        }
        @Override
        public String toString()
        {
            return this.name;
        }
        public ZoneId getZoneId()
        {
            return this.zone;
        }
    }
    
    private Office office;
    private Language language;
    private int startOfBusiness;
    private int endOfBusiness;
    
    public SettingsData()
    {
        // Default settings; can be overwritten from loaded
        // settings.dat file
        office = Office.PHOENIX;
        language = Language.ENGLISH;
        startOfBusiness = 9;
        endOfBusiness = 17;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getStartOfBusiness() {
        return startOfBusiness;
    }

    public void setStartOfBusiness(int startOfBusiness) {
        this.startOfBusiness = startOfBusiness;
    }

    public int getEndOfBusiness() {
        return endOfBusiness;
    }

    public void setEndOfBusiness(int endOfBusiness) {
        this.endOfBusiness = endOfBusiness;
    }
    
    @Override
    public String toString()
    {
        return "TimeZone: " + office
                + ", Language: " + language
                + ", Start of Business: " + AppointmentScheduler.formatTime(startOfBusiness)
                + ", End of Business: " + AppointmentScheduler.formatTime(endOfBusiness);
    }
}
