package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    @FXML
    public TextField addAppointmentTitleField;
    @FXML
    public TextField addAppointmentDescriptionField;
    @FXML
    public TextField addAppointmentLocationField;
    @FXML
    public TextField addAppointmentContactField;
    @FXML
    public TextField addAppointmentTypeField;
    @FXML
    public ChoiceBox addAppointmentCusIdSelector;
    @FXML
    public ChoiceBox addAppointmentUserIdSelector;
    @FXML
    public Button addAppointmentSaveButton;
    @FXML
    public Button addAppointmentCancelButton;
    @FXML
    public DatePicker addAppointmentDatePicker;
    @FXML
    public ComboBox startTimeCombo;
    @FXML
    public ComboBox endTimeCombo;
    @FXML
    public ComboBox addAppointmentContactCombo;

    @FXML
    ObservableList<String> availableTimes = FXCollections.observableArrayList();

    @FXML
    ObservableList<String> endTime = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment add page initialized.");
//        while (sqlResult.next()) {
//            String country = sqlResult.getString("Country");
//            countryList.add(country);
//        }

        LocalTime startTimeBoxValues = LocalTime.MIN.plusHours(8);
        LocalTime endTimeBoxValues = LocalTime.MAX.minusHours(1).minusMinutes(45);

        while (endTimeBoxValues.isAfter(startTimeBoxValues)) {
            System.out.println("In the while");
            availableTimes.add(String.valueOf(startTimeBoxValues));
            System.out.println(availableTimes);
            startTimeBoxValues.plusMinutes(15);
        }

        startTimeCombo.setItems(availableTimes);
        endTimeCombo.setItems(availableTimes);
//
//        for (int i = 0; i < 5; i++) {
//            startTime.plus
//        }

    }

    public void addAppointmentSaveButton(ActionEvent actionEvent) throws IOException {
        java.util.Date datetime = new java.util.Date();
        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateTimeFormatted.format(datetime);

        String appointmentTitle = String.valueOf(addAppointmentTitleField.getText());
        String appointmentDescription = String.valueOf(addAppointmentDescriptionField.getText());
        String appointmentLocation = String.valueOf(addAppointmentLocationField.getText());
        String appointmentType = String.valueOf(addAppointmentTypeField.getText());
        LocalDate appointmentDate = addAppointmentDatePicker.getValue();


//
//        System.out.println(customerCountry);
//        System.out.println(customerDivision);
////        String sqlSelectStatement = ""
//
//        String sqlInsertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By," +
//                "Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?)";
//
//        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
//        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();
//
//        preparedStatement.setString(1, customerName);
//        preparedStatement.setString(2, customerAddress);
//        preparedStatement.setString(3, customerPostal);
//        preparedStatement.setString(4, customerPhone);
//        preparedStatement.setString(5, currentTime);
//        preparedStatement.setString(6, "user");
//        preparedStatement.setString(7, currentTime);
//        preparedStatement.setString(8, "user");
//        preparedStatement.setInt(9, 66);
//
//        try {
//            preparedStatement.execute();
//            if (preparedStatement.getUpdateCount() > 0) {
//                System.out.println("Number of rows affected: " + preparedStatement.getUpdateCount());
//                Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
//                Scene addPartScene = new Scene(add_product);
//                Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                addPartStage.setScene(addPartScene);
//                addPartStage.show();
//            } else {
//                System.out.println("An error occurred and no customers were created.");
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

    }

    public void addAppointmentCancelButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void addAppointmentDatePicker(ActionEvent actionEvent) {
    }

    public void startTimeCombo(ActionEvent actionEvent) {
    }

    public void endTimeCombo(ActionEvent actionEvent) {
    }
}
