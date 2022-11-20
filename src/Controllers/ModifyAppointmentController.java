package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Appointment;
import Models.Contact;
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
    @FXML
    public ComboBox modifyAppointmentContactCombo;
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

    public void modifyAppointmentSaveButton(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        System.out.println("save button clicked");
        java.util.Date datetime = new java.util.Date();
        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateTimeFormatted.format(datetime);

        int appointmentIdField = Integer.parseInt(modifyAppointmentIdField.getText());
        System.out.println(appointmentIdField);

        if (modifyAppointmentCusIdSelector.getValue() != null) {
            System.out.println("in the first statement");
            if (modifyAppointmentUserIdSelector.getValue() != null) {
                System.out.println("inside the second if statement");
                String appointmentTitle = String.valueOf(modifyAppointmentTitleField.getText());
                System.out.println("title");
                String appointmentDescription = String.valueOf(modifyAppointmentDescriptionField.getText());
                System.out.println("desc");
                String appointmentLocation = String.valueOf(modifyAppointmentLocationField.getText());
                System.out.println("loca");
                String appointmentContact = String.valueOf(modifyAppointmentContactCombo.getValue());
                System.out.println("conta");
                String appointmentType = String.valueOf(modifyAppointmentTypeField.getText());
                System.out.println("type");
                String appointmentDate = String.valueOf(modifyAppointmentDatePicker.getValue());
                System.out.println("date");
                String appointmentStartTime = String.valueOf(startTimeCombo.getValue());
                System.out.println("start");
                String appointmentEndTime = String.valueOf(endTimeCombo.getValue());
                System.out.println("end");

                boolean customerCatch = false;
                boolean userCatch = false;
                Integer appointmentCustomerId = null;
                Integer appointmentUserId = null;

                try {
                    appointmentCustomerId = (Integer) modifyAppointmentCusIdSelector.getValue();
                } catch (Exception e) {
                    System.out.println(e);
                    customerCatch = true;
                }

                try {
                    appointmentUserId = (Integer) modifyAppointmentUserIdSelector.getValue();
                } catch (Exception e) {
                    System.out.println(e);
                    userCatch = true;
                }

                if (customerCatch) {
                    appointmentCustomerId = Integer.valueOf((String) modifyAppointmentCusIdSelector.getValue());
                }

                if (userCatch) {
                    appointmentUserId = Integer.valueOf((String) modifyAppointmentUserIdSelector.getValue());
                }

                if (appointmentContact != "null") {

                    if (appointmentDate != "null") {

                        if (appointmentStartTime != "null") {

                            if (appointmentEndTime != "null") {

                                Contact contactIDCall = new Contact();
                                int contactID = contactIDCall.getContactId(appointmentContact);

                                String startDateFormatted = appointmentDate + " " + appointmentStartTime;
                                String endDateFormatted = appointmentDate + " " + appointmentEndTime;

                                String[] startTime = appointmentStartTime.split(":");
                                String startTimeFull = startTime[0] + startTime[1];
                                int startTimeInt = Integer.parseInt(startTimeFull);

                                String[] endTime = appointmentEndTime.split(":");
                                String endTimeFull = endTime[0] + endTime[1];
                                int endTimeInt = Integer.parseInt(endTimeFull);

                                if (endTimeInt > startTimeInt) {

                                    if (appointmentFieldTypeValidation(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType)) {

                                        // code for checking other appoinments PICK UP HERE, FINISH THE SQL STATEMENT, NEED TO
                                        // CHECK FOR OTHER APPOINTMENTS WITH THE SAME CUSTOMER W/ A TIME OVERLAP

                                        String sqlApptCheck = "SELECT COUNT(*) AS total FROM Appointments WHERE Start <= "

                                        String sqlInsertStatement = "UPDATE Appointments SET Title = ?, Description = ?, " +
                                                "Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?," +
                                                " Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
                                                "User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";

                                        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
                                        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                                        preparedStatement.setString(1, appointmentTitle);
                                        preparedStatement.setString(2, appointmentDescription);
                                        preparedStatement.setString(3, appointmentLocation);
                                        preparedStatement.setString(4, appointmentType);
                                        preparedStatement.setString(5, String.valueOf(startDateFormatted));
                                        preparedStatement.setString(6, String.valueOf(endDateFormatted));
                                        preparedStatement.setString(7, currentTime);
                                        preparedStatement.setString(8, "Whoever made it");
                                        preparedStatement.setString(9, currentTime);
                                        preparedStatement.setString(10, "Whoever updated it");
                                        preparedStatement.setInt(11, appointmentCustomerId);
                                        preparedStatement.setInt(12, appointmentUserId);
                                        preparedStatement.setInt(13, contactID);

                                        preparedStatement.setInt(14, appointmentIdField);

                                        try {
                                            preparedStatement.execute();
                                            if (preparedStatement.getUpdateCount() > 0) {
                                                System.out.println("Number of rows affected: " + preparedStatement.getUpdateCount());
                                                Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
                                                Scene addPartScene = new Scene(add_product);
                                                Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                                addPartStage.setScene(addPartScene);
                                                addPartStage.show();
                                            } else {
                                                System.out.println("An error occurred and no customers were created.");
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }



                                    }

                                } else {
                                    errorAlert(7);
                                }

                            } else {
                                errorAlert(9);
                            }

                        } else {
                            errorAlert(8);
                        }

                    } else {
                        errorAlert(6);
                    }

                } else {
                    errorAlert(5);
                }

            } else {
                errorAlert(11);
            }

        } else {
            errorAlert(10);
        }
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

    public boolean appointmentFieldTypeValidation(String appointmentTitle, String appointmentDescription, String appointmentLocation, String appointmentType) {

        boolean validationResult = true;

        if (appointmentTitle.length() == 0) {
            errorAlert(1);
            validationResult = false;
        }

        if (appointmentDescription.length() == 0) {
            errorAlert(2);
            validationResult = false;
        }

        if (appointmentLocation.length() == 0) {
            errorAlert(3);
            validationResult = false;
        }

        if (appointmentType.length() == 0) {
            errorAlert(4);
            validationResult = false;
        }

        return validationResult;

    }

    private void errorAlert(int errorCode) {
        if(errorCode == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Title Validation");
            alert.setContentText("Please enter a title to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Description Validation");
            alert.setContentText("Please enter a description to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Location Validation");
            alert.setContentText("Please enter a location to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment type Validation");
            alert.setContentText("Please enter an appointment type to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Contact Validation");
            alert.setContentText("Please enter a contact to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Date Validation");
            alert.setContentText("Please enter an appointment date to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 7) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Time Error");
            alert.setContentText("The appointment end time has to be later than the start time.");
            alert.showAndWait();
        } else if(errorCode == 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Time Error");
            alert.setContentText("Please enter a start time to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 9) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Time Error");
            alert.setContentText("Please enter an end time to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 10) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer ID Error");
            alert.setContentText("Please enter a customer ID to modify an appointment.");
            alert.showAndWait();
        } else if(errorCode == 11) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User ID Error");
            alert.setContentText("Please enter a user ID to modify an appointment.");
            alert.showAndWait();
        }

    }

}
