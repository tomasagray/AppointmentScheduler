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
public class User 
{
    private int userID;
    private String userName;
    private String password;
    private boolean active = false;
    
    // Constructors
    // -------------------------------------------------------------------------
    public User(String username)
    {
        this.userName = username;
        this.password = "";
    }
    public User(String username, String password)
    {
        this.userName = username;
        this.password = password;
    }
    public User(int userID, String username, String password, boolean active)
    {
        this.userID = userID;
        this.userName = username;
        this.password = password;
        this.active = active;
    }

    // Getters
    // -------------------------------------------------------------------------
    public int getUserID() {
        return userID;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public boolean isActive() {
        return active;
    }
    // Setters
    // -------------------------------------------------------------------------
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    
    
}
