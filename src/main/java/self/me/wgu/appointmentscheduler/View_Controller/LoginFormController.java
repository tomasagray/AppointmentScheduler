/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller;

import self.me.wgu.appointmentscheduler.Resettable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import self.me.wgu.appointmentscheduler.LoginManager;
import self.me.wgu.appointmentscheduler.Model.User;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;

/**
 *
 * @author tomas
 */
public class LoginFormController implements Initializable, Resettable 
{
    
    @FXML
    private GridPane loginForm;
    @FXML
    private ImageView loginAvatar;
    @FXML
    private TextField loginUsername;
    @FXML
    private TextField loginPassword;
    @FXML
    private Label errorMessages;
    @FXML
    private Button loginSubmit;
    
    
    @FXML
    private void handleLoginSubmit()
    { 
        String userName = loginUsername.getText();
        String password = loginPassword.getText();
        
        // Validate login form
        if( userName == null || userName.equals("") )
        {
            errorMessages.setText(
                    SettingsManager.getInstance()
                            .getMessages().getString("noUsernameError")
            );
            return;     // login has failed
        }
        if(password == null || password.equals(""))
        {
            errorMessages.setText(
                    SettingsManager.getInstance()
                            .getMessages().getString("noPasswordError")
            );
            return;
        }
        
        // Perform login
        User user = new User(userName, password);
        LoginManager lm = LoginManager.getInstance();
        
        if( lm.login(user) )
        {
            // Reset form
            reset();
            // Add user profile to settings
            SettingsManager.getInstance().setUser(user);
            // Load the main user interface
            SceneManager.getInstance().initializeUIContainer();
            
        } else {
            errorMessages.setText( SettingsManager.getInstance()
                    .getMessages().getString("loginFailedError") );  
        }
    }
    
    @Override
    public void reset()
    {
        loginUsername.clear();
        loginPassword.clear();
        errorMessages.setText("");
        loginUsername.requestFocus();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("Initializing: LoginForm");
        
        
        // Submit the login form if the user
        // presses the 'ENTER' key
        loginForm.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ENTER)
                handleLoginSubmit();
        });
        
        // Ensure form fields are blank
        loginUsername.clear();
        loginPassword.clear();
        // Clear error messages
        errorMessages.setText("");
    }
    
}
