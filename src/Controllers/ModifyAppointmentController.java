package Controllers;

import Database.DBConnection;
import Models.Appointment;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {

    @FXML
    public TextField modifyAppointmentTitleField;
    @FXML
    public TextField modifyAppointmentDescriptionField;
    @FXML
    public TextField modifyAppointmentLocationField;
    @FXML
    public TextField modifyAppointmentTypeField;
    @FXML
    public ChoiceBox modifyAppointmentCusIdSelector;
    @FXML
    public ChoiceBox modifyAppointmentUserIdSelector;
    @FXML
    public Button modifyAppointmentSaveButton;
    @FXML
    public Button modifyAppointmentCancelButton;
    @FXML
    public DatePicker modifyAppointmentDatePicker;
    @FXML
    public ComboBox startTimeCombo;
    @FXML
    public ComboBox endTimeCombo;
    @FXML public ComboBox modifyAppointmentContactCombo;
    @FXML
    public TextField modifyAppointmentIdField;

    @FXML
    ObservableList<String> availableTimes = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> contactNames = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> customerIDs = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> userIDs = FXCollections.observableArrayList();

    Appointment modifyAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment modify page initialized.");

        LocalTime startTimeBoxValues = LocalTime.MIN.plusHours(8);
        LocalTime endTimeBoxValues = LocalTime.MAX.minusHours(1).minusMinutes(45);

        if (!startTimeBoxValues.equals(0) || !endTimeBoxValues.equals(0)) {

            while (startTimeBoxValues.isBefore(endTimeBoxValues)) {
                availableTimes.add(String.valueOf(startTimeBoxValues));
                startTimeBoxValues = startTimeBoxValues.plusMinutes(15);
            }

        }

        startTimeCombo.setItems(availableTimes);
        endTimeCombo.setItems(availableTimes);

        // contacts
        String sqlContactStatement = "SELECT Contact_Name FROM Contacts;";
        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlContactStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            while (sqlResult.next()) {
                String contactName = sqlResult.getString("Contact_Name");
                contactNames.add(contactName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        modifyAppointmentContactCombo.getItems().clear();
        modifyAppointmentContactCombo.setItems(contactNames);

        String sqlCustomerIdStatement = "SELECT Customer_ID FROM Customers ORDER BY Customer_ID ASC;";
        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlCustomerIdStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            while (sqlResult.next()) {
                String customerID = sqlResult.getString("Customer_ID");
                customerIDs.add(customerID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        modifyAppointmentCusIdSelector.getItems().clear();
        modifyAppointmentCusIdSelector.setItems(customerIDs);

        String sqlUserIdStatement = "SELECT User_ID FROM Users ORDER BY User_ID ASC;";
        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlUserIdStatement);
            ResultSet sqlUserResult = sqlPreparedStatement.executeQuery();
            while (sqlUserResult.next()) {
                String userID = sqlUserResult.getString("User_ID");
                userIDs.add(userID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        modifyAppointmentUserIdSelector.getItems().clear();
        modifyAppointmentUserIdSelector.setItems(userIDs);

    }

    public void addAppointmentSaveButton(ActionEvent actionEvent) throws IOException {
    }

    public void setAppointment(Appointment selectedAppointment) {

        modifyAppointment = selectedAppointment;

        String rawDate = String.valueOf(selectedAppointment.getAppointmentStart());
        String[] apptDate = rawDate.split(" ");
        String parsedDate = apptDate[0];
        String parsedStartTime = apptDate[1];

        String rawEnd = String.valueOf(selectedAppointment.getAppointmentEnd());
        String[] endTime = rawEnd.split(" ");
        String parsedEndTime = endTime[1];

        modifyAppointmentIdField.setText(Integer.toString(selectedAppointment.getAppointmentId()));
        modifyAppointmentTitleField.setText(selectedAppointment.getAppointmentTitle());
        modifyAppointmentDescriptionField.setText(selectedAppointment.getAppointmentDescription());
        modifyAppointmentLocationField.setText(selectedAppointment.getAppointmentLocation());
        modifyAppointmentContactCombo.getSelectionModel().select(selectedAppointment.getContactsName());
        modifyAppointmentTypeField.setText(selectedAppointment.getAppointmentType());
        modifyAppointmentDatePicker.setValue(LocalDate.parse(parsedDate));
        startTimeCombo.setValue(parsedStartTime);
        endTimeCombo.setValue(parsedEndTime);
        modifyAppointmentCusIdSelector.setValue(selectedAppointment.getCustomerId());
        modifyAppointmentUserIdSelector.setValue(selectedAppointment.getUserId());

    }

    public void addAppointmentIdField(ActionEvent actionEvent) {
    }

    public void modifyAppointmentSaveButton(ActionEvent actionEvent) {
    }

    public void modifyAppointmentCancelButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void modifyAppointmentDatePicker(ActionEvent actionEvent) {
    }

    public void startTimeCombo(ActionEvent actionEvent) {
    }

    public void endTimeCombo(ActionEvent actionEvent) {
    }

}
