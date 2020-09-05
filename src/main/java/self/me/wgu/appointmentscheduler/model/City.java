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
