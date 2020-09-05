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

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * @author tomas
 */
public class Appointment {

  // Empty/default appointment ID
  public static final int DEFAULT_ID = Integer.MIN_VALUE;

  private int appointmentID;
  private Customer customer;
  private String title;
  private String description;
  private String location;
  //    private String type;
  private String url;
  private ZonedDateTime start;
  private ZonedDateTime end;

  private Appointment() {
    this.customer = new Customer();
  }

  @Override
  public String toString() {
    // Check for null values
    String cID = null, customerName = null;
    if (customer != null) {
      cID = customer.getCustomerID() + "";
      customerName = customer.getCustomerName();
    }

    return "Appointment ID: " + this.appointmentID
        + ", Customer ID: " + cID
        + ", Title: " + this.title
        + ", Description: " + this.description
        + ", Location: " + this.location
        + ", Contact: " + customerName
//                +", Type: " +this.type
        + ", URL: " + url
        + ", Start: " + this.start
        + ", End: " + this.end;
  }

  // Getters
  // -------------------------------------------------------------------------
  public int getAppointmentID() {
    return appointmentID;
  }

  // Setters
  // -------------------------------------------------------------------------
  public void setAppointmentID(int appointmentID) {
    this.appointmentID = appointmentID;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  //    public String getType() {
//        return type;
//    }
  public String getUrl() {
    return url;
  }

  //    public void setType(String type) {
//        this.type = type;
//    }
  public void setUrl(String url) {
    this.url = url;
  }

  public ZonedDateTime getStart() {
    return start;
  }

  public void setStart(ZonedDateTime start) {
    this.start = start;
  }

  public ZonedDateTime getEnd() {
    return end;
  }

  public void setEnd(ZonedDateTime end) {
    this.end = end;
  }

  public LocalDate getStartDate() {
    return start.toLocalDate();
  }

  public LocalDate getEndDate() {
    return end.toLocalDate();
  }

  public Duration getDuration() {
    return Duration.between(start, end);
  }

  public boolean hasCustomer() {
    return (customer != null);
  }

  public boolean isStartSet() {
    return (start != null);
  }

  public boolean isEndSet() {
    return (end != null);
  }


  /*
      Appointment builder class
  */
  public static class AppointmentBuilder {

    private final int appointmentID;
    private Customer customer;
    private String title;
    private String description;
    private String location;
    //        private String type;
    private String url;
    private ZonedDateTime start;
    private ZonedDateTime end;

    public AppointmentBuilder() {
      this.appointmentID = Appointment.DEFAULT_ID;
    }

    public AppointmentBuilder(int id) {
      this.appointmentID = id;
    }

    public AppointmentBuilder setCustomer(Customer customer) {
      this.customer = customer;
      return this;
    }

    public AppointmentBuilder setTitle(String title) {
      this.title = title;
      return this;
    }

    public AppointmentBuilder setDescription(String desc) {
      this.description = desc;
      return this;
    }

    public AppointmentBuilder setLocation(String location) {
      this.location = location;
      return this;
    }

    //        public AppointmentBuilder setType(String type)
//        {
//            this.type = type;
//            return this;
//        }
    public AppointmentBuilder setURL(String url) {
      this.url = url;
      return this;
    }

    public AppointmentBuilder setStart(ZonedDateTime start) {
      this.start = start;
      return this;
    }

    public AppointmentBuilder setEnd(ZonedDateTime end) {
      this.end = end;
      return this;
    }

    public Appointment build() {
      Appointment appt = new Appointment();
      appt.setAppointmentID(this.appointmentID);
      appt.setCustomer(this.customer);
      appt.setDescription(this.description);
      appt.setTitle(this.title);
//            appt.setType(this.type);
      appt.setLocation(this.location);
      appt.setUrl(this.url);
      appt.setStart(this.start);
      appt.setEnd(this.end);

      return appt;
    }
  }

}
