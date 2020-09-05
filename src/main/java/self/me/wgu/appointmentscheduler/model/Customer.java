/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.model;

/**
 * @author tomas
 */
public class Customer {

  private int customerID;
  private String customerName;
  private boolean active = false;
  private Address address;

  public Customer() {
    this.address = new Address.AddressBuilder(0).build();
  }

  public Customer(int id, String name) {
    this.customerID = id;
    this.customerName = name;
  }

  public Customer(int id, String name, Address address) {
    this(id, name);
    this.address = address;
  }

  public Customer(int id, String name, Address address, boolean active) {
    this(id, name, address);
    this.active = active;
  }

  @Override
  public String toString() {
    return "Customer Name: " + customerName
        + ", ID: " + customerID;
  }

  public String getCustomerName() {
    return this.customerName;
  }

  // Getters & setters
  public void setCustomerName(String name) {
    this.customerName = name;
  }

  public int getCustomerID() {
    return this.customerID;
  }

  public void setCustomerID(int id) {
    this.customerID = id;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  // Alternative method for active indicator
  public String isActiveString() {
      if (this.active) {
          return "Active";
      } else {
          return "Inactive";
      }
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

}
