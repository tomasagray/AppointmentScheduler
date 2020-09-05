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
public class Country {

  private int countryID;
  private String countryName;

  public Country(int id, String name) {
    this.countryID = id;
    this.countryName = name;
  }

  @Override
  public String toString() {
    return getCountryName();
  }

  // Getters
  // -------------------------------------------------------------------------
  public int getCountryID() {
    return this.countryID;
  }

  // Setters
  // -------------------------------------------------------------------------
  public void setCountryID(int countryID) {
    this.countryID = countryID;
  }

  public String getCountryName() {
    return this.countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

}
