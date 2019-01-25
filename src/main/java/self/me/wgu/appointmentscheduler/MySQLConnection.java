/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler;

import java.sql.*;
import java.time.*;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import self.me.wgu.appointmentscheduler.Model.Address;
import self.me.wgu.appointmentscheduler.Model.Appointment;
import self.me.wgu.appointmentscheduler.Model.City;
import self.me.wgu.appointmentscheduler.Model.Country;
import self.me.wgu.appointmentscheduler.Model.Customer;
import self.me.wgu.appointmentscheduler.Model.User;

/**
 *
 * @author tomas
 */
public class MySQLConnection 
{
    private static final MySQLConnection instance = new MySQLConnection();
    private Connection conn = null;

    // Connection details
    private final String driver;
    private final String db;
    private final String url;
    private final String user;
    private final String pass;
    
    private MySQLConnection()
    {
        driver = "com.mysql.cj.jdbc.Driver";
        db = "U04hNx";
        url = "jdbc:mysql://52.206.157.109/" + db;
        user = "U04hNx";
        pass = "53688244537";
        
        connect();
    }
    
    public final static MySQLConnection getInstance()
    {
        return instance;
    }
    
    public final void connect()
    {
        // Check if we are already connected
        if( conn == null)
        {
            System.out.println("Connecting to: "+ url);

            try {
                Class.forName(driver);
                conn = DriverManager.getConnection( url, user, pass );
                // Success!
                System.out.println("Connected to database: " + db);
            } catch (SQLException e) {
                System.out.println("COULD NOT CONNECT TO DATABASE AT: " + url + "!!!");
            } catch( ClassNotFoundException e ) {
                System.out.println("Class "+ driver +" not found!");
            }
        }
    }

    // ---------------------------------------------------------------------- \\
    // User (consultant) functions
    // ---------------------------------------------------------------------- \\
    public List<String> getUsersList()
    {
        String query = "SELECT userId, userName FROM user";
        List<String> users = new ArrayList<>();
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            ResultSet rs = pStmt.executeQuery();
            
            while(rs.next())
            {
                users.add( rs.getString("userName"));
            }
        } catch( SQLException e ) {
            handleSQLException("getUsersList", e);
        }
        
        return users;
    }
    
    public Map<String, Map<Integer, Integer>> getUserSchedule(LocalDate start, LocalDate end)
    {
        Map<String, Map<Integer, Integer>> data = new HashMap<>();
        List<String> users = getUsersList();
        String query = "SELECT  "
                + "MONTH(start) AS month, "
                + "COUNT(appointmentId) AS count "
                + "FROM appointment "
                + "WHERE start > ? AND end < ? "
                + "AND createdBy=? "
                + "GROUP BY MONTH(start)";
        
        users.forEach((user) -> {
            try {
                PreparedStatement pStmt = conn.prepareStatement(query);
                // Bind data
                pStmt.setDate(1, java.sql.Date.valueOf(start) );
                pStmt.setDate(2, java.sql.Date.valueOf(end) );
                pStmt.setString(3, user);
                // Get results
                ResultSet rs = pStmt.executeQuery();
                Map<Integer, Integer> schedule = new HashMap<>();
                while(rs.next())
                    schedule.put(rs.getInt("month"), rs.getInt("count"));
                // Add schedule to data collection
                data.put(user, schedule);
            } catch( SQLException e) {
                handleSQLException("getUserSchedule", e);
            }
        });
        
        return data;
    }
    
    // ---------------------------------------------------------------------- \\
    // Country functions
    // ---------------------------------------------------------------------- \\
    /**
     * Adds an array of Country objects to the database
     * @param countries 
     */
    public void addCountries( Country... countries )
    {
        Set<Integer> countryIDs = getIDs("country");
        String query = "INSERT INTO country(countryID, country, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?)";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);

            for( Country country : countries )
            {
                while(countryIDs.contains(country.getCountryID()))
                    country.setCountryID(country.getCountryID() + 1);
                // Update the list of taken countryIDs
                countryIDs.add(country.getCountryID());

                pStmt.setInt(1, country.getCountryID());
                pStmt.setString(2, country.getCountryName());
                pStmt.setTimestamp(3, Timestamp.from(Instant.now()));
                pStmt.setString(4, LoginManager.getInstance().getUser().getUserName());
                pStmt.setString(5, LoginManager.getInstance().getUser().getUserName());
                pStmt.executeUpdate();
            }
        } catch( SQLException e ) {
            handleSQLException("addCountries", e);
        }
    }
    
    /**
     * Get a single Country, as specified by the Country ID (int)
     * @param countryID int
     * @return 
     */
    public Country getCountry( int countryID )
    {
        String query = "SELECT * FROM country "
                + "WHERE countryId=?";
        Country country = null;
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, countryID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next())
            {
                int cID = rs.getInt("countryId");
                String name = rs.getString("country");
                country = new Country(cID, name);
            }
        } catch( SQLException e) {
            handleSQLException("getCountry", e);
        }
        
        return country;
    }
    
    /**
     * Retrieve an alphabetized list of all countries from the database.
     * 
     * @return List<Country>
     */
    public List<Country> getCountries()
    {
        List<Country> countries = new ArrayList<>();
        String query = "SELECT * FROM country ORDER BY country";
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next())
            {
                int countryID = rs.getInt("countryId");
                String name = rs.getString("country");
                countries.add( new Country(countryID, name) );
            }
        } catch( SQLException e ) {
            handleSQLException("getCountries", e);
        }
        
        return countries;
    }
    
    
    // ---------------------------------------------------------------------- \\
    // City functions
    // ---------------------------------------------------------------------- \\
    public void addCities( City... cities )
    {
        Set<Integer> cityIDs = getIDs("city");
        String query = "INSERT INTO city (cityID, city, countryID, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?,?)";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);

            for( City city : cities )
            {
                try {
                    verifyForeignKey("country", city.getCountryID());
                } catch( InvalidIdentifierException e) {
                    System.out.println(e.getMessage());
                    System.out.println("You must create an entry in the country table before it can be used.");
                    return;
                }

                // Ensure cityId has not already been used;
                // if it has, find a new one and update it
                while(cityIDs.contains(city.getCityID()))
                    city.setCityID(city.getCityID()+ 1);
                // Update list of taken IDs
                cityIDs.add(city.getCityID());

                pStmt.setInt(1, city.getCityID());
                pStmt.setString(2, city.getCityName());
                pStmt.setInt(3, city.getCountryID());
                pStmt.setTimestamp(4, Timestamp.from(Instant.now()));
                pStmt.setString(5, LoginManager.getInstance().getUser().getUserName());
                pStmt.setString(6, LoginManager.getInstance().getUser().getUserName());
                pStmt.executeUpdate();
            }
        } catch( SQLException e ) {
            handleSQLException("addCities", e);
        }
    }
    
    /**
     * Get one city, specified by that city's ID
     * @param cityID int
     * @return 
     */
    public City getCity(int cityID)
    {
        String query = "SELECT * FROM city "
                + "WHERE cityId=?";
        City city = null;
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, cityID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next())
            {
                int cID = rs.getInt("cityId");
                int countryID = rs.getInt("countryId");
                String name = rs.getString("city");
                // Assign city
                city = new City(cID, name, countryID);
            }
        } catch( SQLException e) {
            handleSQLException("getCity", e);
        }
        
        return city;
    }
    
    /**
     * Get an alphabetized list of City objects from the database
     * @return 
     */
    public List<City> getCities()
    {
        String query = "SELECT * FROM city ORDER BY city";
        List<City> cities = new ArrayList<>();
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next())
            {
                int cityID = rs.getInt("cityId");
                String name = rs.getString("city");
                int countryID = rs.getInt("countryId");
                
                cities.add( new City(cityID, name, countryID) );
            }
        } catch( SQLException e ) {
            handleSQLException("getCities", e);
        }
        
        return cities;
    }
    
    
    // ---------------------------------------------------------------------- \\
    // Address functions
    // ---------------------------------------------------------------------- \\
    /**
     * Add addresses to the database. If the addressID of the
     * supplied address(es) is already in use, it will be overwritten
     * with a new value;
     * 
     * @param addresses
     * 
     */
    public void addAddresses( Address... addresses )
    {
        Set<Integer> addressIDs = getIDs("address");
        String query = "INSERT INTO address( addressID, address, address2, cityId, "
                + "postalCode, phone, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);

            for( Address address: addresses )
            {
                try {
                    verifyForeignKey("city", address.getCity().getCityID());
                } catch( InvalidIdentifierException e) {
                    System.out.println(e.getMessage());
                    System.out.println("You must create an entry in the city table before it can be used.");
                    return;
                }

                while(addressIDs.contains(address.getAddressID()))
                    address.setID(address.getAddressID() + 1);
                addressIDs.add(address.getAddressID()); // Update local list of used IDs

                // Add the address
                pStmt.setInt(1, address.getAddressID());
                pStmt.setString(2, address.getAddress());
                pStmt.setString(3, address.getAddress2());
                pStmt.setInt(4, address.getCity().getCityID() );
                pStmt.setString(5, address.getPostalCode());
                pStmt.setString(6, address.getPhone());
                pStmt.setTimestamp(7, Timestamp.from(Instant.now()));
                pStmt.setString(8, LoginManager.getInstance().getUser().getUserName());
                pStmt.setString(9, LoginManager.getInstance().getUser().getUserName());
                pStmt.executeUpdate();
            }
        } catch( SQLException e ) {
            handleSQLException("addAddresses", e);
        }
    }
    
    public void updateAddress( Address address )
    {
        String query = "UPDATE address "
                + "SET address=?, "
                + "address2=?, "
                + "cityId=?, "
                + "postalCode=?, "
                + "phone=?, "
                + "lastUpdateBy=? "
                + "WHERE addressId=?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setString(1, address.getAddress() );
            pStmt.setString(2, address.getAddress2() );
            pStmt.setInt(3, address.getCity().getCityID() );
            pStmt.setString(4, address.getPostalCode() );
            pStmt.setString(5, address.getPhone() );
            pStmt.setString(6, LoginManager.getInstance().getUser().getUserName() );
            pStmt.setInt(7, address.getAddressID() );
            
            pStmt.executeUpdate();
        } catch( SQLException e) {
            handleSQLException("updateAddress", e);
        }
    }
    
    public void deleteAddress( Address address )
    {
        String query = "DELETE FROM address "
                + "WHERE addressId=?";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, address.getAddressID() );
            pStmt.executeUpdate();
            
        } catch( SQLException e ) {
            handleSQLException("deleteAddress", e);
        }
    }
    
    
    // ---------------------------------------------------------------------- \\
    // Customer functions
    // ---------------------------------------------------------------------- \\
    public List<Customer> getCustomers()
    {
        List<Customer> customers = new ArrayList<>();
        String q = "SELECT addressId, customerId, active, customerName, address, address2, "
                + "city, country, postalCode, phone, cityId, countryId " 
                + "FROM "
                + "customer INNER JOIN address "
                + "USING(addressId) "
                + "INNER JOIN city "
                + "USING(cityId) "
                + "INNER JOIN country "
                + "USING(countryId) "
                + "ORDER BY customerName";
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);

            while(rs.next())
            {
                // Get primitives
                int addressID = rs.getInt("addressId");
                int id = rs.getInt("customerId");
                boolean active = rs.getBoolean("active");
                String customerName = rs.getString("customerName");
                String address = rs.getString("address");
                String address2 = rs.getString("address2");
                String postalCode = rs.getString("postalCode");
                String phone = rs.getString("phone");
                
                // Get objects; requires additional database call
                City city = new City(
                        rs.getInt("cityId"),
                        rs.getString("city"),
                        rs.getInt("countryId")
                );
                Country country = new Country(
                        rs.getInt("countryId"), 
                        rs.getString("country")
                );

                Address addr = new Address.AddressBuilder(addressID)
                                    .setAddress(address)
                                    .setAddress2(address2)
                                    .setCity(city)
                                    .setCountry(country)
                                    .setPostalCode(postalCode)
                                    .setPhone(phone)
                                    .build();
                
                Customer customer = new Customer(id, customerName, addr, active);
                customers.add( customer );
            }
            return customers;
        } catch( SQLException e ) {
            handleSQLException("getCustomers", e);
        }
        // Something didn't work
        return null;
    }
    
    /**
     * Get a single customer, specified by the customerID
     * 
     */
    public Customer getCustomer(int customerID)
    {
        String query = "SELECT * FROM customer "
                + "WHERE customerId=?";
        Customer customer = null;
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, customerID );
            ResultSet rs = pStmt.executeQuery();
            
            if(rs.next()) 
            {
                int cID = rs.getInt("customerId");
                String name = rs.getString("customerName");
                customer = new Customer(cID, name);
            }
            
        } catch( SQLException e) {
            handleSQLException("getCustomer", e);
        }
        
        return customer;
    }
    
    public void addCustomers( Customer... customers )
    {
        Set<Integer> customerIDs = getIDs("customer");
        String query = "INSERT INTO customer(customerId, customerName, addressId, active, "
                + "createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement pStmt = conn.prepareStatement(query);

            for(Customer customer : customers)
            {
                try {
                    verifyForeignKey("address", customer.getAddress().getAddressID() );
                } catch( InvalidIdentifierException e) {
                    System.out.println(e.getMessage());
                    System.out.println("You must create an entry in the city table before it can be used.");
                    return;
                }

                // Add address FIRST, to ensure addressId is updated before
                // associating it with this customer
                addAddresses(customer.getAddress());
                
                while(customerIDs.contains(customer.getCustomerID()))
                    customer.setCustomerID(customer.getCustomerID() + 1);
                customerIDs.add(customer.getCustomerID());

                pStmt.setInt(1, customer.getCustomerID());
                pStmt.setString(2, customer.getCustomerName());
                pStmt.setInt(3, customer.getAddress().getAddressID() );
                pStmt.setInt(4, customer.isActive() ? 1 : 0 );
                pStmt.setTimestamp(5, Timestamp.from(Instant.now()));
                pStmt.setString(6, LoginManager.getInstance().getUser().getUserName());
                pStmt.setString(7, LoginManager.getInstance().getUser().getUserName());
                pStmt.executeUpdate();
                
            }
        } catch( SQLException e ) {
            handleSQLException("addCustomers", e);
        }
    }
    
    public void updateCustomer( Customer customer )
    {
        String query = "UPDATE customer "
                + "SET customerName=?, "
                + "lastUpdateBy=?,"
                + "active=? "
                + "WHERE customerId=?";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setString(1, customer.getCustomerName() );
            pStmt.setString(2, LoginManager.getInstance().getUser().getUserName() );
            pStmt.setInt(3, customer.isActive() ? 1 : 0 );
            pStmt.setInt(4, customer.getCustomerID() );
            pStmt.executeUpdate();
            
            // Update the address
            updateAddress( customer.getAddress() );
            
        } catch( SQLException e) {
            handleSQLException("updateCustomer", e);
        }
    }
    
    public void deleteCustomer(Customer customer)
    {
        String query = "DELETE FROM customer "
                + "WHERE customerId=?";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, customer.getCustomerID() );
            pStmt.executeUpdate();
            
            // Delete the associated address
            deleteAddress(customer.getAddress());
            
        } catch( SQLException e ) {
            handleSQLException("deleteCustomer", e);
        }
    }
    
    // ---------------------------------------------------------------------- \\
    // Appointment functions
    // ---------------------------------------------------------------------- \\    
    public List<Appointment> getAppointments(LocalDate begin, LocalDate end)
    {
        
        List<Appointment> appointments = new ArrayList<>();
        String q = "SELECT * FROM appointment "
                 + "WHERE start > ? AND start < ?"
                 + "AND createdBy=?";     // Only the appointments for current user?

        try {
            PreparedStatement pStmt = conn.prepareStatement(q);
            pStmt.setDate(1, java.sql.Date.valueOf(begin));
            pStmt.setDate(2, java.sql.Date.valueOf(end)); 
            pStmt.setString(3, LoginManager.getInstance().getUser().getUserName() );
            ResultSet rs = pStmt.executeQuery();

            while(rs.next())
            {
                int id = rs.getInt("appointmentId");
                int customerID = rs.getInt("customerId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String url = rs.getString("url");
                ZonedDateTime apptStart = ZonedDateTime.ofInstant(
                        rs.getTimestamp("start").toInstant(),
                        SettingsManager.getInstance()
                                .getSettingsData().getOffice().getZoneId()
                );
                ZonedDateTime apptEnd = ZonedDateTime.ofInstant(
                        rs.getTimestamp("end").toInstant(),
                        SettingsManager.getInstance()
                                .getSettingsData().getOffice().getZoneId()
                );
                
                Customer customer = new Customer(customerID, contact);
 
                Appointment appt = new Appointment.AppointmentBuilder(id)
                                                    .setCustomer(customer)
                                                    .setTitle(title)
                                                    .setDescription(description)
                                                    .setLocation(location)
                                                    .setURL(url)
                                                    .setStart(apptStart)
                                                    .setEnd(apptEnd)
                                                    .build();
                appointments.add(appt);
            }
            
            return appointments;
        } catch( SQLException e ) {
            handleSQLException("getAppointments", e);
        }
        // Something went wrong
        return null;
    }
    
    public Map<String, Map<Integer, Integer>> getAppointmentCount(LocalDate begin, LocalDate end )
    {
        Map<String, Map<Integer, Integer>> apptCount = new HashMap<>();
        String[] types = { "Executive Meeting", "Sales Meeting", "Boring Meeting",
            "Exciting Meeting", "Mind Meeting"
        };
        
        String query = "SELECT "
                + "MONTH(start) AS month, "
                + "COUNT(appointmentId) AS count "
                + "FROM appointment "
                + "WHERE start > ? AND end < ? "
                + "AND title=? "
                + "GROUP BY MONTH(start)";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            for(String type : types)
            {
                pStmt.setDate(1, java.sql.Date.valueOf(begin) );
                pStmt.setDate(2, java.sql.Date.valueOf(end) );
                pStmt.setString(3, type);
                
                ResultSet rs = pStmt.executeQuery();
                Map<Integer, Integer> monthCount = new HashMap<>();
                while(rs.next())
                    monthCount.put(rs.getInt("month"), rs.getInt("count") );
                
                apptCount.put(type, monthCount);
            } 
            
        } catch( SQLException e ) {
            handleSQLException("getAppointmentCount", e);
        }
        return apptCount;
    }
    
    public void addAppointments( Appointment... appointments)
    {
        Set<Integer> appointmentIDs = getIDs("appointment");
        String query = "INSERT INTO appointment(appointmentId, customerId, "
                + "title, description, location, contact, url, start, end, "
                + "createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);

            for(Appointment appointment : appointments)
            { 
                // Enforce valid foreign keys, since this doesn't
                // seem to be set in the DB
                try {
                    verifyForeignKey("customer", appointment.getCustomer().getCustomerID());
                } catch( InvalidIdentifierException e) {
                    System.out.println(e.getMessage());
                    // Exit the method
                    return;
                }

                // If this appointment has the default ID, get a new one
                if( appointment.getAppointmentID() == Appointment.DEFAULT_ID )
                    appointment.setAppointmentID(
                        (int)appointmentIDs.stream().count()
                    );
                
                // Ensure appointmentId is unique; if it has already been used,
                // increment it until an unused key is found
                while(appointmentIDs.contains(appointment.getAppointmentID()))
                    appointment.setAppointmentID(appointment.getAppointmentID() + 1);
                // Update list of used IDs with new ^
                appointmentIDs.add(appointment.getAppointmentID());
                
                // Convert appointment times to Timestamps
                // for database standardization
                Timestamp startTS = Timestamp.from( 
                        Instant.ofEpochSecond(
                                appointment.getStart().toEpochSecond()
                        )
                );
                Timestamp endTS = Timestamp.from(
                        Instant.ofEpochSecond(
                                appointment.getEnd().toEpochSecond()
                        )
                );
                
                // Bind data to SQL statement
                pStmt.setInt(1, appointment.getAppointmentID());
                pStmt.setInt(2, appointment.getCustomer().getCustomerID());
                pStmt.setString(3, appointment.getTitle());
                pStmt.setString(4, appointment.getDescription());
                pStmt.setString(5, appointment.getLocation());
                pStmt.setString(6, appointment.getCustomer().getCustomerName());
    //            pStmt.setString(7, appointment.getType());    // field TYPE not used?
                pStmt.setString(7, appointment.getUrl());
                pStmt.setTimestamp(8, startTS ); 
                pStmt.setTimestamp(9, endTS ); 
                pStmt.setTimestamp(10, Timestamp.from(Instant.now()));
                pStmt.setString(11, LoginManager.getInstance().getUser().getUserName());
                pStmt.setString(12, LoginManager.getInstance().getUser().getUserName());
                pStmt.executeUpdate();
            }
        } catch( SQLException e ) {
            handleSQLException("addAppointments", e);
        }
    }
    
    public void updateAppointment( Appointment appointment )
    {
        
        String query = "UPDATE appointment "
                + "SET customerId=?, "
                + "title=?, "
                + "description=?, "
                + "location=?, "
                + "contact=?, "
                + "url=?, "
                + "start=?, "
                + "end=?, "
                + "lastUpdateBy=? "
                + "WHERE appointmentId=?";
        try {
            // Convert appointment times to Timestamps
            // for database standardization
            Timestamp startTS = Timestamp.from( 
                    Instant.ofEpochSecond(
                            appointment.getStart().toEpochSecond()
                    )
            );
            Timestamp endTS = Timestamp.from(
                    Instant.ofEpochSecond(
                            appointment.getEnd().toEpochSecond()
                    )
            );
            PreparedStatement pStmt = conn.prepareStatement(query);
            // Bind data to statement
            pStmt.setInt(1, appointment.getCustomer().getCustomerID() );
            pStmt.setString(2, appointment.getTitle() );
            pStmt.setString(3, appointment.getDescription() );
            pStmt.setString(4, appointment.getLocation() );
            pStmt.setString(5, appointment.getCustomer().getCustomerName() );
            pStmt.setString(6, appointment.getUrl() );
            pStmt.setTimestamp(7, startTS );
            pStmt.setTimestamp(8, endTS );
            pStmt.setString(9, LoginManager.getInstance().getUser().getUserName() );
            pStmt.setInt(10, appointment.getAppointmentID() );
            
            pStmt.executeUpdate();
            
        } catch( SQLException e ) {
            handleSQLException("updateAppointment", e);
        }
    }
    
    public void deleteAppointment(Appointment appointment)
    {
        String query = "DELETE FROM appointment "
                        + "WHERE appointmentId=?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, appointment.getAppointmentID() );
            pStmt.executeUpdate();
        } catch( SQLException e ) {
            handleSQLException("deleteAppointment", e );
        }
    }
    
    
      
    // ---------------------------------------------------------------------- \\
    //  SQL functions
    // ---------------------------------------------------------------------- \\
    /**
     * Get a List<Integer> of already-used IDs from the given table.
     * Returns null if nothing found.
     * 
     * @param tableName
     * @return 
     */
    private Set<Integer> getIDs( String tableName )
    {
        Set<Integer> ids = new HashSet<>();
        // Get the name of the primary key for this table
        String primaryKey = getPrimaryKeyColumn(tableName);
        // If we found a match...
        if(primaryKey != null)
        {
            try {
                // Get all the primary keys in this table   
                Statement stmt = conn.createStatement();
                String q = "SELECT "+ primaryKey +" FROM " + tableName;
                ResultSet rs = stmt.executeQuery(q);
                while(rs.next())
                    ids.add( rs.getInt(primaryKey) );

                return ids;
            } catch( SQLException e ) {
                handleSQLException("getIDs", e);
            }
        }
        return null;
    }
    
    /**
     * Determine the primary key column name from the 
     * requested table
     * 
     * @param tableName
     * @return
     */
    private String getPrimaryKeyColumn( String tableName )
    {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW INDEX FROM " + tableName);
            // Only one result should be returned
            rs.next();
            return rs.getString("Column_name");
        } catch( SQLException e ) {
            handleSQLException("getPrimaryKeyColumn", e);
        }
        
        // Something went wrong
        return null;
    }
        
    /**
     * Verifies the existence of the given key (id) in the given table
     * 
     */
    private void verifyForeignKey( String tableName, int id ) throws InvalidIdentifierException
    {
        try {
            String primaryKey = getPrimaryKeyColumn(tableName);
            Statement stmt = conn.createStatement();
            boolean keyFound = stmt.execute("SELECT " + primaryKey +" FROM "+ tableName 
                    +" WHERE " + primaryKey +"="+ id);
            if(!keyFound)
                throw new InvalidIdentifierException();
        } catch( SQLException e ) {
            handleSQLException("verifyForeignKey", e);
        }
    }
    
    
    // ---------------------------------------------------------------------- \\
    //  Login functions
    // ---------------------------------------------------------------------- \\
    public Optional<User> getUserCredentials(String username)
    {
        String query = "SELECT userId, userName, password, active FROM user "
                + "WHERE userName=?";
        // Empty user to be filled with retreived credentials
        User user = null;
        
        try {
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setString(1, username);
            ResultSet rs = pStmt.executeQuery();
            rs.next();      // Advance to first record, if it exists
            
            int userID = rs.getInt("userId");
            String userName = rs.getString("userName");
            String password = rs.getString("password");
            boolean active = rs.getBoolean("active");
            
            // Create a User object from data retrieved from DB
            user = new User(userID, userName, password, active);
            
        } catch( SQLException e ) {
            System.out.println("User not found: "+ username);
        }
        
        return Optional.ofNullable(user);
    }
    
    public void setUserActive(User user)
    { 
        try {
            PreparedStatement pStmt = conn.prepareStatement("UPDATE user SET active=1 WHERE userId=?");
            pStmt.setInt(1, user.getUserID() );
            pStmt.executeUpdate();
        } catch( SQLException e ) {
            handleSQLException("LoginManager.setUserActive", e);
        }
    }
    
    public void setUserInactive(User user)
    {
        try {
            PreparedStatement pStmt = conn.prepareStatement("UPDATE user SET active=0 WHERE userId=?");
            pStmt.setInt(1, user.getUserID() );
            pStmt.executeUpdate();
        } catch( SQLException e ) {
            handleSQLException("LoginManager.setUserInactive", e);
        }
    }
    
    
    // ---------------------------------------------------------------------- \\
    //  Helper functions
    // ---------------------------------------------------------------------- \\
    public void handleSQLException(String method, SQLException e)
    {
        // Print exception info to console
        printSQLException(method, e);
        
        // Attempt to boot user out to login screen
        try {
            if(conn != null && conn.isClosed())
            {
                System.out.println("Connection is closed.");
                Alert alert = new Alert(
                        AlertType.INFORMATION, 
                        "The SQL connection is closed. You have been logged out."
                );
                alert.showAndWait();
                SceneManager.getInstance().initializeLoginForm();
            }
            
        } catch(SQLException ex) {
            printSQLException("handleSQLException", ex);
        }
    }
    
    public void printSQLException(String method, SQLException e)
    {
        System.out.println("Error in method: " + method);
        System.out.println("SQLException: "+e.getMessage());
        System.out.println("SQLState: "+e.getSQLState());
        System.out.println("VendorError: "+e.getErrorCode());
    }
    
    /**
     * COMPLETELY ERASES ALL INFORMATION IN DATABASE!!!
     * Use with EXTREME caution! And always backup.
     * 
     */
    public void resetDB()
    {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM country");
            stmt.execute("DELETE FROM city");
            stmt.execute("DELETE FROM address");
            stmt.execute("DELETE FROM customer");
            stmt.execute("DELETE FROM appointment");
        } catch(SQLException e) {
            handleSQLException("resetDB", e);
        }
    }
}
