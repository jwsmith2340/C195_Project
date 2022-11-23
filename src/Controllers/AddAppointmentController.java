package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Contact;
import Models.User;
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
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    @FXML
    public TextField addAppointmentTitleField;
    @FXML
    public TextField addAppointmentDescriptionField;
    @FXML
    public TextField addAppointmentLocationField;
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

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");

    @FXML
    ObservableList<String> availableTimes = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> contactNames = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> customerIDs = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> userIDs = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment add page initialized.");

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

        addAppointmentContactCombo.getItems().clear();
        addAppointmentContactCombo.setItems(contactNames);

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

        addAppointmentCusIdSelector.getItems().clear();
        addAppointmentCusIdSelector.setItems(customerIDs);

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

        addAppointmentUserIdSelector.getItems().clear();
        addAppointmentUserIdSelector.setItems(userIDs);

    }

    public void addAppointmentSaveButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("save button clicked");
//        java.util.Date datetime = new java.util.Date();
//        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        String currentTime = dateTimeFormatted.format(datetime);

        ZoneId utcZoneId = ZoneId.of("UTC");
        LocalDateTime utcDateTime = LocalDateTime.now(utcZoneId);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        String utcFormattedDateTime = utcDateTime.format(dateTimeFormatter);

        if (addAppointmentCusIdSelector.getValue() != null) {

            if (addAppointmentUserIdSelector.getValue() != null) {

                String appointmentTitle = String.valueOf(addAppointmentTitleField.getText());
                String appointmentDescription = String.valueOf(addAppointmentDescriptionField.getText());
                String appointmentLocation = String.valueOf(addAppointmentLocationField.getText());
                String appointmentContact = String.valueOf(addAppointmentContactCombo.getValue());
                String appointmentType = String.valueOf(addAppointmentTypeField.getText());
                String appointmentDate = String.valueOf(addAppointmentDatePicker.getValue());
                String appointmentStartTime = String.valueOf(startTimeCombo.getValue());
                String appointmentEndTime = String.valueOf(endTimeCombo.getValue());
                Integer appointmentCustomerId = Integer.valueOf((String) addAppointmentCusIdSelector.getValue());
                Integer appointmentUserId = Integer.valueOf((String) addAppointmentUserIdSelector.getValue());
                System.out.println(appointmentCustomerId);
                if (appointmentContact != "null") {

                    if (appointmentDate != "null") {

                        if (appointmentStartTime != "null") {

                            if (appointmentEndTime != "null") {

                                Contact contactIDCall = new Contact();
                                int contactID = contactIDCall.getContactId(appointmentContact);

                                String startDateFormatted = appointmentDate + " " + appointmentStartTime + ":00";
                                String endDateFormatted = appointmentDate + " " + appointmentEndTime + ":00";

                                String startDateFormattedUtc = utcDateTimeFormatter(startDateFormatted);
                                String endDateFormattedUtc = utcDateTimeFormatter(endDateFormatted);

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
                                        System.out.println(sqlApptCheck);
                                        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlApptCheck);
                                        PreparedStatement overlapPreparedStatement = DBPreparedStatement.getPreparedStatement();

                                        overlapPreparedStatement.setString(1, String.valueOf(startDateFormattedUtc));
                                        overlapPreparedStatement.setString(2, String.valueOf(endDateFormattedUtc));
                                        overlapPreparedStatement.setString(3, String.valueOf(startDateFormattedUtc));
                                        overlapPreparedStatement.setString(4, String.valueOf(endDateFormattedUtc));
                                        overlapPreparedStatement.setInt(5, appointmentCustomerId);

                                        System.out.println(sqlApptCheck);
                                        try {
                                            ResultSet sqlResult = overlapPreparedStatement.executeQuery();
                                            sqlResult.next();
                                            System.out.println(sqlResult.getInt("total"));
                                            if (sqlResult.getInt("total") == 0) {
                                                System.out.println("There are no overlapping appointments here mate");

                                                String sqlInsertStatement = "INSERT INTO Appointments (Title, Description, Location, Type, Start, End, Create_Date," +
                                                        " Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                                                DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
                                                PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                                                preparedStatement.setString(1, appointmentTitle);
                                                preparedStatement.setString(2, appointmentDescription);
                                                preparedStatement.setString(3, appointmentLocation);
                                                preparedStatement.setString(4, appointmentType);
                                                preparedStatement.setString(5, String.valueOf(startDateFormattedUtc));
                                                preparedStatement.setString(6, String.valueOf(endDateFormattedUtc));
                                                preparedStatement.setString(7, utcFormattedDateTime);
                                                preparedStatement.setString(8, User.userName);
                                                preparedStatement.setString(9, utcFormattedDateTime);
                                                preparedStatement.setString(10, User.userName);
                                                preparedStatement.setInt(11, appointmentCustomerId);
                                                preparedStatement.setInt(12, appointmentUserId);
                                                preparedStatement.setInt(13, contactID);

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

    public String utcDateTimeFormatter(CharSequence dateToBeFormatted) {
        String UTC_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime localStartDateTime = LocalDateTime.parse(dateToBeFormatted, DateTimeFormatter.ofPattern(UTC_STANDARD_FORMAT));
        ZonedDateTime systemStartZonedDateTime = localStartDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcSqlStartTime = systemStartZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        Instant utcSqlStartTimestamp = systemStartZonedDateTime.toInstant();

        String utcSqlStartTimestampString = String.valueOf(utcSqlStartTimestamp);
        String utcSqlStartSubString = utcSqlStartTimestampString.substring(0,19);
        String[] startTime = utcSqlStartSubString.split("T");
        String formattedDateTimeFull = startTime[0] + " " + startTime[1];

        return formattedDateTimeFull;
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
            alert.setContentText("Please enter a title to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Description Validation");
            alert.setContentText("Please enter a description to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Location Validation");
            alert.setContentText("Please enter a location to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment type Validation");
            alert.setContentText("Please enter an appointment type to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Contact Validation");
            alert.setContentText("Please enter a contact to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Date Validation");
            alert.setContentText("Please enter an appointment date to create a new appointment.");
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
            alert.setContentText("Please enter a start time to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 9) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Time Error");
            alert.setContentText("Please enter an end time to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 10) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer ID Error");
            alert.setContentText("Please enter a customer ID to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 11) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User ID Error");
            alert.setContentText("Please enter a user ID to create a new appointment.");
            alert.showAndWait();
        } else if(errorCode == 12) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Error");
            alert.setContentText("This appointment interferes with another one of this customer's appoinments. Please " +
                    "change the customer, or change the date/time of the appointment.");
            alert.showAndWait();
        }

    }

}
