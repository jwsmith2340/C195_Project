package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        java.util.Date datetime = new java.util.Date();
        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateTimeFormatted.format(datetime);

        String appointmentTitle = String.valueOf(addAppointmentTitleField.getText());
        String appointmentDescription = String.valueOf(addAppointmentDescriptionField.getText());
        String appointmentLocation = String.valueOf(addAppointmentLocationField.getText());
        String appointmentContact = String.valueOf(addAppointmentContactCombo.getValue());
        String appointmentType = String.valueOf(addAppointmentTypeField.getText());
        LocalDate appointmentDate = addAppointmentDatePicker.getValue();
        String appointmentStartTime = String.valueOf(startTimeCombo.getValue());
        String appointmentEndTime = String.valueOf(endTimeCombo.getValue());
        Integer appointmentCustomerId = Integer.valueOf((String) addAppointmentCusIdSelector.getValue());
        Integer appointmentUserId = Integer.valueOf((String) addAppointmentUserIdSelector.getValue());

        Contact contactIDCall = new Contact();
        int contactID = contactIDCall.getContactId(appointmentContact);
        System.out.println(appointmentDate);
//        String startDateRaw = appointmentDate +
//
//
//
//        LocalTime appointmentFormattedStart =
//        LocalTime appointmentFormattedEnd = LocalTime.from(LocalDateTime.of(appointmentDate, appointmentFormattedEndTime));
//
//        System.out.println(appointmentCustomerId);
//        System.out.println(appointmentUserId);
//
//        String sqlInsertStatement = "INSERT INTO Appointments (Title, Description, Location, Type, Start, End, Create_Date," +
//                " Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
//        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();
//
//        preparedStatement.setString(1, appointmentTitle);
//        preparedStatement.setString(2, appointmentDescription);
//        preparedStatement.setString(3, appointmentLocation);
//        preparedStatement.setString(4, appointmentType);
//        preparedStatement.setString(5, String.valueOf(appointmentFormattedStart));
//        preparedStatement.setString(6, String.valueOf(appointmentFormattedEnd));
//        preparedStatement.setString(7, currentTime);
//        preparedStatement.setString(8, "Whoever made it");
//        preparedStatement.setString(9, currentTime);
//        preparedStatement.setString(10, "Whoever updated it");
//        preparedStatement.setInt(11, appointmentCustomerId);
//        preparedStatement.setInt(12, appointmentUserId);
//        preparedStatement.setInt(13, contactID);
//
//        try {
//            preparedStatement.execute();
//            if (preparedStatement.getUpdateCount() > 0) {
//                System.out.println("Number of rows affected: " + preparedStatement.getUpdateCount());
//                Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
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
