/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.Model;

/**
 * Class representing a physical mailing address.
 * 
 * @author tomas
 */
public class Address 
{   
    private int addressID;
    private String address;
    private String address2;
    private City city;
    private Country country;
    private String postalCode;
    private String phone;

    // Builder pattern; private constructor
    private Address() {}
    
    // Address getters and setters
    // -------------------------------------------------------------------------
    public int getAddressID()
    {
        return this.addressID;
    }
    public void setID(Integer id)
    {
        this.addressID = id;
    }
    public String getAddress()
    {
        return this.address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getAddress2()
    {
        return this.address2;
    }
    public void setAddress2(String address)
    {
        this.address2 = address;
    }
    public City getCity()
    {
        return this.city;
    }
    public void setCity(City city)
    {
        this.city = city;
    }
    public Country getCountry()
    {
        return this.country;
    }
    public void setCountry(Country country)
    {
        this.country = country;
    }
    public String getPostalCode()
    {
        return this.postalCode;
    }
    public void setPostalCode(String code)
    {
        this.postalCode = code;
    }
    public String getPhone()
    {
        return this.phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    @Override
    public String toString()
    {
        return address + " "
                + address2 + ", "
                + city + ", "
                + country + " "
                + postalCode;
    }
    public String toMultiLineString()
    {
        StringBuilder str = new StringBuilder( address + "\n" );
        
        if(!address2.equals(""))
            str.append(address2 + "\n");
        str.append(city + ", " + country + " " + postalCode);
        
        return str.toString();
    }
        
    public static class AddressBuilder
    {
        private int addressID;
        private String address;
        private String address2;
        private City city;
        private Country country;
        private String postalCode;
        private String phone;
        
        public AddressBuilder(int id)
        {
            this.addressID = id;
        }
        public AddressBuilder setAddress(String address)
        {
            this.address = address;
            return this;
        }
        public AddressBuilder setAddress2(String address)
        {
            this.address2 = address;
            return this;
        }
        public AddressBuilder setCity(City city)
        {
            this.city = city;
            return this;
        }
        public AddressBuilder setCountry(Country country)
        {
            this.country = country;
            return this;
        }
        public AddressBuilder setPostalCode(String code)
        {
            this.postalCode = code;
            return this;
        }
        public AddressBuilder setPhone(String phone)
        {
            this.phone = phone;
            return this;
        }
        public Address build()
        {
            Address address = new Address();
            address.addressID = this.addressID;
            address.address = this.address;
            address.address2 = this.address2;
            address.city = this.city;
            address.country = this.country;
            address.postalCode = this.postalCode;
            address.phone = this.phone;
            
            return address;
        }
    }
    
}
