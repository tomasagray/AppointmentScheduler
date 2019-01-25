/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.me.wgu.appointmentscheduler.View_Controller;

import self.me.wgu.appointmentscheduler.Resettable;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import self.me.wgu.appointmentscheduler.MySQLConnection;
import self.me.wgu.appointmentscheduler.SettingsManager;

/**
 * FXML Controller class
 *
 * @author tomas
 */
public class ReportsController implements Initializable, Resettable
{
    public final class LoginRecord
    {
        private final String userName;
        private final String password;
        private final Timestamp timestamp;
        private final boolean success;
        
        public LoginRecord(String record)
        {
            // Split the string into its elements
            String[] recordItems = record.split(";");
            this.userName = recordItems[0];
            this.password = recordItems[1];
            this.timestamp = Timestamp.valueOf(recordItems[2]);
            // Decode success String into boolean
            if(recordItems[3].toLowerCase().equals("true"))
                this.success = true;
            else
                this.success = false;
        }
        
        public String getUserName()
        {
            return this.userName;
        }
        public String getPassword()
        {
            return this.password;
        }
        public Timestamp getTimestamp()
        {
            return this.timestamp;
        }
        public String getSuccess()
        {
            return this.success+"";
        }
    }
    
    
    @FXML
    private VBox apptTypesPerMonth;
    @FXML
    private VBox consultantSchedules;
    @FXML
    private VBox loginReport;
    
    @FXML
    private TableView loginRecordsTable;
    @FXML
    private TableColumn<LoginRecord, String> userNameColumn;
    @FXML
    private TableColumn<LoginRecord, String> passwordColumn;
    @FXML
    private TableColumn<LoginRecord, Timestamp> timestampColumn;
    @FXML
    private TableColumn<LoginRecord, String> successColumn;

    @FXML
    private void handleApptTypesPerMonth()
    {
        System.out.println("Appointments per month");
        reset();
        
        // Create bar chart
        // ---------------------------------------------------------------------
        //Define the x axis               
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(
                FXCollections.<String>observableArrayList( 
                        Arrays.asList( new DateFormatSymbols().getMonths() )
                )
        );
        xAxis.setLabel("Month");  

        //Define the y axis 
        NumberAxis yAxis = new NumberAxis(); 
        yAxis.setLabel("# of appointments");
        
        // Create BarChart object
        BarChart<String, Number> typeChart = new BarChart(xAxis, yAxis);
        // Date range
        LocalDate start = LocalDate.now().withMonth(1).withDayOfMonth(1);
        LocalDate end   = start.plusYears(1);
        // Get data
        Map<String, Map<Integer, Integer>> data = 
                MySQLConnection.getInstance().getAppointmentCount(start, end);
 
        // For each appointment "type"
        data.keySet().stream().map((type) -> {
            // Create bars for this "type"
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(type);
            Map<Integer, Integer> types = data.get(type);
            // For each month...
            types.keySet().forEach((month) -> {
                String monthName = new DateFormatSymbols().getMonths()[month - 1];
                series.getData().add( new XYChart.Data<>(monthName, types.get(month) ) );
            });
            return series;
        }).forEachOrdered((series) -> {
            typeChart.getData().add(series);
        });
        
        apptTypesPerMonth.getChildren().add(typeChart);
    }
    
    
    @FXML
    private void handleConsultantSchedules()
    {
        System.out.println("Consultant schedules");
        reset();    
        
        // Create bar chart
        // ---------------------------------------------------------------------
        //Define the x axis               
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(
                FXCollections.<String>observableArrayList( 
                        Arrays.asList( new DateFormatSymbols().getMonths() )
                )
        );
        xAxis.setLabel("Month");   

        //Define the y axis 
        NumberAxis yAxis = new NumberAxis(); 
        yAxis.setLabel("# of appointments");
        
        // Create BarChart object
        BarChart<String, Integer> scheduleChart = new BarChart(xAxis, yAxis);
        // Date range
        LocalDate start = LocalDate.now().withMonth(1).withDayOfMonth(1);
        LocalDate end   = start.plusYears(1);
        // Get data
        Map<String, Map<Integer, Integer>> data = 
                MySQLConnection.getInstance().getUserSchedule(start, end);

        data.keySet().forEach((consultant) -> {
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName(consultant);
            Map<Integer, Integer> schedule = data.get(consultant);
            schedule.keySet().forEach((month) -> {
                String monthName = new DateFormatSymbols().getMonths()[month - 1];
                series.getData().add( new XYChart.Data<>(monthName, schedule.get(month) ));
            });
            // Add series to histogram
            scheduleChart.getData().add(series);
        });
        // Add chart to reports screen
        consultantSchedules.getChildren().add(scheduleChart);
    }
    
    @FXML
    private void handleLoginReport()
    {
        // Reset to default view
        reset();
        // Show the table
        loginRecordsTable.setVisible(true);
        loginRecordsTable.setManaged(true);
        
        // Get a Path to log file
        Path path = Paths.get( SettingsManager.LOG_FILE );
        
        try {
            // Get all records
            Files.lines(path).forEach((line) -> {
                loginRecordsTable.getItems().add( new LoginRecord(line) );
            });
            
        } catch( IOException e ) {
            loginReport.getChildren().add( 
                    new Text("Could not read log file!\n"
                            + "Log file location: " + path)
            );
        }
    }
    
    @Override
    public void reset()
    {
        apptTypesPerMonth.getChildren().clear();
        consultantSchedules.getChildren().clear();
        loginRecordsTable.getItems().clear();
        loginRecordsTable.setVisible(false);
        loginRecordsTable.setManaged(false);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        System.out.println("Initializing: Reports");
        
        // Setup cell factories
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        successColumn.setCellValueFactory(new PropertyValueFactory<>("success"));
        
        // Reset to default view
        reset();
    }    
    
}
