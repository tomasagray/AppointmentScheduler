/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.Model;

/**
 *
 * @author tomas
 */
public class Country 
{
    private int countryID;
    private String countryName;
    
    public Country(int id, String name)
    {
        this.countryID = id;
        this.countryName = name;
    }
    
    @Override
    public String toString()
    {
        return getCountryName();
    }
    
    // Getters
    // -------------------------------------------------------------------------
    public int getCountryID()
    {
        return this.countryID;
    }
    public String getCountryName()
    {
        return this.countryName;
    }
    // Setters
    // -------------------------------------------------------------------------
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
}
