/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.model;

/**
 * @author tomas
 */
public class City {

  private int cityID;
  private String cityName;
  private int countryID;

  public City(int id, String name, int countryID) {
    this.cityID = id;
    this.cityName = name;
    this.countryID = countryID;
  }

  @Override
  public String toString() {
    return getCityName();
  }

  // Getters
  // -------------------------------------------------------------------------
  public int getCityID() {
    return this.cityID;
  }

  // Setters
  // -------------------------------------------------------------------------
  public void setCityID(int cityID) {
    this.cityID = cityID;
  }

  public String getCityName() {
    return this.cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public int getCountryID() {
    return this.countryID;
  }

  public void setCountryID(int countryID) {
    this.countryID = countryID;
  }

}
