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
package self.me.wgu.appointmentscheduler.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import self.me.wgu.appointmentscheduler.LoginManager;
import self.me.wgu.appointmentscheduler.SceneManager;
import self.me.wgu.appointmentscheduler.SettingsManager;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class UIContainerController implements Initializable {

  @FXML
  private VBox mainUISpace;
  @FXML
  private VBox menuBar;
  @FXML
  private Button calendarButton;
  @FXML
  private Button customersButton;
  @FXML
  private Button reportsButton;
  @FXML
  private Button settingsButton;


  @FXML
  public void handleCalendarButton() {
    mainUISpace.getChildren().clear();
    // Ensure calendar is populated
    CalendarController cc = SceneManager.getInstance()
        .getLoader("Calendar")
        .getController();
    cc.handleCalendarToday();

    // Add calendar to view space
    mainUISpace.getChildren()
        .add(SceneManager.getInstance().getScene("Calendar"));

    // Highlight menu item
    resetSelectionStyles();
    calendarButton.getStyleClass().add("selected");

  }

  @FXML
  public void handleCustomersButton() {
    mainUISpace.getChildren().clear();
    mainUISpace.getChildren()
        .add(SceneManager.getInstance().getScene("Customers"));
    resetSelectionStyles();
    customersButton.getStyleClass().add("selected");
  }

  @FXML
  public void handleReportsButton() {
    mainUISpace.getChildren().clear();
    mainUISpace.getChildren()
        .add(SceneManager.getInstance().getScene("Reports"));
    resetSelectionStyles();
    reportsButton.getStyleClass().add("selected");
  }

  @FXML
  public void handleSettingsButton() {
    mainUISpace.getChildren().clear();
    mainUISpace.getChildren()
        .add(SceneManager.getInstance().getScene("Settings"));
    resetSelectionStyles();
    settingsButton.getStyleClass().add("selected");
  }

  @FXML
  public void handleLogoutButton() {
    // Confirm the user really wants to logout
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setHeaderText("Logout");
    alert.setContentText("Are you sure?");
    Optional<ButtonType> buttonClicked = alert.showAndWait();

    if (buttonClicked.isPresent() && buttonClicked.get() == ButtonType.OK) {
      LoginManager.getInstance().logout(
          SettingsManager.getInstance().getUser()
      );
    }

  }

  private void resetSelectionStyles() {
    menuBar.getChildrenUnmodifiable().forEach((n) -> n.getStyleClass().remove("selected"));
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("Initializing: UIContainer");
  }

}
