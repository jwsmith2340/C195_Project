package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Appointment;
import Models.Contact;
import Models.User;
import Models.UserLocalTime;
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
import java.sql.SQLOutput;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * modify appointment controller handles the modify appointment view for modifying appointments
 */
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

    /**
     * initialize populates the start time and end time combo boxes, the contact combo box, the customer id combo
     * box, the user id combo box.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment modify page initialized.");

        LocalTime startTimeBoxValues = LocalTime.MIN.plusHours(0);
        LocalTime endTimeBoxValues = LocalTime.MAX.minusHours(0).minusMinutes(15);

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

    /**
     * setAppointment first gets the dates and times from the selected appointment, it then parses that date time to
     * extract the time in string format, and it sets the start and end time combo boxes with those values. Additionally,
     * it sets the text fields according to the selected appointment values.
     * @param selectedAppointment
     */
    public void setAppointment(Appointment selectedAppointment) {

        modifyAppointment = selectedAppointment;

        String rawDate = String.valueOf(selectedAppointment.getAppointmentStartLocalDT());
        String rawEnd = String.valueOf(selectedAppointment.getAppointmentEndLocalDT());

        String[] apptDate = rawDate.split(" ");
        String parsedDate = apptDate[0];
        String parsedStartTime = apptDate[1];
        parsedStartTime = parsedStartTime.substring(0,5);

        String[] endTime = rawEnd.split(" ");
        String parsedEndTime = endTime[1];
        parsedEndTime = parsedEndTime.substring(0,5);

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

    /**
     * This method takes in a date in UTC time and returns it in the user's local time
     * @param dateToBeFormatted
     * @return
     */
    public String localDateTimeFormatter(CharSequence dateToBeFormatted) {
        String UTC_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime localStartDateTimeee = LocalDateTime.parse(dateToBeFormatted, DateTimeFormatter.ofPattern(UTC_STANDARD_FORMAT));
        ZonedDateTime systemStartZonedDateTimeee = localStartDateTimeee.atZone(ZoneId.of("UTC"));
        ZonedDateTime utcSqlStartTimeee = systemStartZonedDateTimeee.withZoneSameInstant(ZoneId.systemDefault());

        String localTimeString = String.valueOf(utcSqlStartTimeee);
        String localTimeSubString = localTimeString.substring(0,16);
        String[] localTime = localTimeSubString.split("T");
        String formattedDateTimeFull = localTime[0] + " " + localTime[1] + ":00";

        System.out.println(formattedDateTimeFull);
        return formattedDateTimeFull;
    }

    /**
     * This date formatter takes in a string value of a current date, the local date/time input by the user. A
     * LocalDateTime object is then created with the UTC_STANDARD_FORMAT that I set and the user's input. This input
     * is then zoned according to the user's system ZoneId. The user's system time is then converted to UTC time, and
     * the value is transformed to a string, formatted, and returned to the save method.
     * @param dateToBeFormatted
     * @return
     */
    public String localToUtcDateTimeFormatter(String dateToBeFormatted) {
        String UTC_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime localStartDateTimeee = LocalDateTime.parse(dateToBeFormatted, DateTimeFormatter.ofPattern(UTC_STANDARD_FORMAT));
        ZonedDateTime systemStartZonedDateTimeee = localStartDateTimeee.atZone(ZoneId.systemDefault());
        ZonedDateTime utcSqlStartTimeee = systemStartZonedDateTimeee.withZoneSameInstant(ZoneId.of("UTC"));

        String localTimeString = String.valueOf(utcSqlStartTimeee);
        String localTimeSubString = localTimeString.substring(0,16);
        String[] localTime = localTimeSubString.split("T");
        String formattedDateTimeFull = localTime[0] + " " + localTime[1] + ":00";

        System.out.println(formattedDateTimeFull);
        return formattedDateTimeFull;
    }

    /**
     * This date formatter takes in a string value of a current date, the local date/time input by the user. A
     * LocalDateTime object is then created with the UTC_STANDARD_FORMAT that I set and the user's input. This input
     * is then zoned according to the user's system ZoneId. The user's system time is then converted to eastern time, and
     * the value is transformed to a string, formatted, and returned to the save method.
     * @param dateToBeChecked
     * @return
     */
    public boolean businessHoursChecker(String dateToBeChecked) {
        String UTC_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime localStartDateTimeee = LocalDateTime.parse(dateToBeChecked, DateTimeFormatter.ofPattern(UTC_STANDARD_FORMAT));
        ZonedDateTime systemStartZonedDateTimeee = localStartDateTimeee.atZone(ZoneId.of(UserLocalTime.userTimeZone));
        ZonedDateTime utcSqlStartTimeee = systemStartZonedDateTimeee.withZoneSameInstant(ZoneId.of(UserLocalTime.businessTimeZone));

        String localTimeString = String.valueOf(utcSqlStartTimeee);
        String localTimeSubString = localTimeString.substring(11,16);
        String formattedDateTimeFull = localTimeSubString + ":00";

        String[] timeToInt = formattedDateTimeFull.split(":");
        String startTimeFull = timeToInt[0] + timeToInt[1];
        int finalTimeInt = Integer.parseInt(startTimeFull);

        if (finalTimeInt < 800 || finalTimeInt > 2200) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * The save button first gets the current time and appointment id. If there is a customer and user id, the text
     * fields are populated with the values of the selected appointment. Validation is then performed on each field
     * and the date time is formatted to upload from local time to UTC date time. That UTC date time is then used
     * to search the appointments table for conflicting appointment times. If there are no conflicting appointments
     * and all other validations have cleared, an UPDATE SQL statement is run to update the modified appointment.
     * Error codes exist at every level in the event that validations fail.
     * @param actionEvent
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void modifyAppointmentSaveButton(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        System.out.println("save button clicked");
        java.util.Date datetime = new java.util.Date();
        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateTimeFormatted.format(datetime);

        int appointmentIdField = Integer.parseInt(modifyAppointmentIdField.getText());

        if (modifyAppointmentCusIdSelector.getValue() != null) {
            if (modifyAppointmentUserIdSelector.getValue() != null) {
                String appointmentTitle = String.valueOf(modifyAppointmentTitleField.getText());
                String appointmentDescription = String.valueOf(modifyAppointmentDescriptionField.getText());
                String appointmentLocation = String.valueOf(modifyAppointmentLocationField.getText());
                String appointmentContact = String.valueOf(modifyAppointmentContactCombo.getValue());
                String appointmentType = String.valueOf(modifyAppointmentTypeField.getText());
                String appointmentDate = String.valueOf(modifyAppointmentDatePicker.getValue());
                String appointmentStartTime = String.valueOf(startTimeCombo.getValue());
                String appointmentEndTime = String.valueOf(endTimeCombo.getValue());

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

                                String startDateFormatted = appointmentDate + " " + appointmentStartTime + ":00";
                                String endDateFormatted = appointmentDate + " " + appointmentEndTime + ":00";

                                boolean startTimeInRange = businessHoursChecker(startDateFormatted);
                                boolean endTimeInRange = businessHoursChecker(endDateFormatted);

                                if (startTimeInRange) {

                                    if (endTimeInRange) {

                                        String[] startTime = appointmentStartTime.split(":");
                                        String startTimeFull = startTime[0] + startTime[1];
                                        int startTimeInt = Integer.parseInt(startTimeFull);

                                        String[] endTime = appointmentEndTime.split(":");
                                        String endTimeFull = endTime[0] + endTime[1];
                                        int endTimeInt = Integer.parseInt(endTimeFull);

                                        if (endTimeInt > startTimeInt) {

                                            if (appointmentFieldTypeValidation(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType)) {

                                                String sqlApptCheck = "SELECT COUNT(*) AS total FROM Appointments WHERE " +
                                                        "((Start >= ? AND Start <= ?) " +
                                                        "OR (End >= ? AND End <= ?)) AND Customer_ID = ?;";

                                                DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlApptCheck);
                                                PreparedStatement overlapPreparedStatement = DBPreparedStatement.getPreparedStatement();

                                                String startDateFormattedUtc = localToUtcDateTimeFormatter(startDateFormatted);
                                                String endDateFormattedUtc = localToUtcDateTimeFormatter(endDateFormatted);

                                                overlapPreparedStatement.setString(1, String.valueOf(startDateFormattedUtc));
                                                overlapPreparedStatement.setString(2, String.valueOf(endDateFormattedUtc));
                                                overlapPreparedStatement.setString(3, String.valueOf(startDateFormattedUtc));
                                                overlapPreparedStatement.setString(4, String.valueOf(endDateFormattedUtc));
                                                overlapPreparedStatement.setInt(5, appointmentCustomerId);

                                                try {
                                                    ResultSet sqlResult = overlapPreparedStatement.executeQuery();
                                                    sqlResult.next();

                                                    if (sqlResult.getInt("total") == 0) {
                                                        String sqlInsertStatement = "UPDATE Appointments SET Title = ?, Description = ?, " +
                                                                "Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?," +
                                                                " Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
                                                                "User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";

                                                        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
                                                        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                                                        preparedStatement.setString(1, appointmentTitle);
                                                        preparedStatement.setString(2, appointmentDescription);
                                                        preparedStatement.setString(3, appointmentLocation);
                                                        preparedStatement.setString(4, appointmentType);
                                                        preparedStatement.setString(5, String.valueOf(startDateFormattedUtc));
                                                        preparedStatement.setString(6, String.valueOf(endDateFormattedUtc));
                                                        preparedStatement.setString(7, currentTime);
                                                        preparedStatement.setString(8, currentTime);
                                                        preparedStatement.setString(9, User.userName);
                                                        preparedStatement.setInt(10, appointmentCustomerId);
                                                        preparedStatement.setInt(11, appointmentUserId);
                                                        preparedStatement.setInt(12, contactID);

                                                        preparedStatement.setInt(13, appointmentIdField);

                                                        try {
                                                            preparedStatement.execute();
                                                            if (preparedStatement.getUpdateCount() > 0) {
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
                                                    } else {
                                                        errorAlert(12);
                                                    }

                                                } catch (Exception e) {
                                                    System.out.println(e.getMessage());
                                                }

                                            }

                                        } else {
                                            errorAlert(7);
                                        }

                                    } else {
                                        errorAlert(14);
                                    }

                                } else {
                                    errorAlert(13);
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

    /**
     * The cancel button redirects the user to the main appointment view
     * @param actionEvent
     * @throws IOException
     */
    public void modifyAppointmentCancelButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /**
     * The validation method provides error codes and a boolean validation result to cause the save button method
     * to stop in the case validation fails.
     * @param appointmentTitle
     * @param appointmentDescription
     * @param appointmentLocation
     * @param appointmentType
     * @return
     */
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

    /**
     * Error codes are created in this method that are then used in the rest of this class.
     * @param errorCode
     */
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
        } else if(errorCode == 12) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Error");
            alert.setContentText("This appointment interferes with another one of this customer's appoinments. Please " +
                    "change the customer, or change the date/time of the appointment.");
            alert.showAndWait();
        } else if(errorCode == 13) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Scheduling Error");
            alert.setContentText("The appointment time was set before 8:00am ET, please updated the start time of your" +
                    " appointment.");
            alert.showAndWait();
        } else if(errorCode == 14) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Scheduling Error");
            alert.setContentText("The appointment time was set after 10:00pm ET, please updated the start time of your" +
                    " appointment.");
            alert.showAndWait();
        }

    }

}
