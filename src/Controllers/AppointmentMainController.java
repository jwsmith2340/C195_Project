package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Appointment;
import Models.Customer;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The appointmentMainController handles the main appointment page, including all redirect button clicks
 * and table view population.
 */
public class AppointmentMainController implements Initializable {

    @FXML
    public TableColumn appointmentColumn;
    @FXML
    public TableColumn titleColumn;
    @FXML
    public TableColumn descriptionColumn;
    @FXML
    public TableColumn locationColumn;
    @FXML
    public TableColumn contactColumn;
    @FXML
    public TableColumn typeColumn;
    @FXML
    public TableColumn startDateColumn;
    @FXML
    public TableColumn endDateColumn;
    @FXML
    public TableColumn customerIdColumn;
    @FXML
    public TableColumn userIdColumn;
    @FXML
    public TableView appointmentsTableView;
    public Button appointmentAddButton;
    public Button appointmentModifyButton;
    public Button appointmentBackButton;
    public RadioButton appointmentWeekRadio;
    public RadioButton appointmentMonthRadio;
    public RadioButton appointmentAll;
    public ToggleGroup timeRangeToggleGroup;
    public Button appointmentDeleteButton;
    @FXML
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    /**
     * initialize gets all appointments in the DB and populates the appointmentList arrayList. Once all of the
     * appointments have been added to arrayList, it is used to populate the appointment table.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment main page initialized");

        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Appointments.Customer_ID, Appointments.User_ID FROM Appointments " +
                "INNER JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID";

        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {

                int appointmentId = sqlResult.getInt("Appointment_ID");
                String appointmentTitle = sqlResult.getString("Title");
                String appointmentDescription = sqlResult.getString("Description");
                String appointmentLocation = sqlResult.getString("Location");
                String contactsName = sqlResult.getString("Contact_Name");
                String appointmentType = sqlResult.getString("Type");
                String appointmentStart = sqlResult.getString("Start");
                String appointmentEnd = sqlResult.getString("End");
                int customerId = sqlResult.getInt("Customer_ID");
                int userId = sqlResult.getInt("User_ID");

                String appointmentStartLocalDT = localDateTimeFormatter(appointmentStart);
                String appointmentEndLocalDT = localDateTimeFormatter(appointmentEnd);

                Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, contactsName, appointmentType, appointmentStartLocalDT, appointmentEndLocalDT, customerId, userId);
                appointmentList.addAll(appointment);

                appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
                contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactsName"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                startDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStartLocalDT"));
                endDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEndLocalDT"));
                customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        appointmentsTableView.setItems(appointmentList);
    }

    /**
     * The add button redirects the user to the add appointment view.
     * @param actionEvent
     * @throws IOException
     */
    public void appointmentAddButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AddAppointment.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /**
     * The modify button requires that a user first select an appointment to modify. If an appointment is selected,
     * a new scene is created and the selected appointment is passed in to be modified in the ModifyAppointment view.
     * @param actionEvent
     * @throws IOException
     */
    public void appointmentModifyButton(ActionEvent actionEvent) throws IOException {
        if(appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            Appointment selectedAppointment = (Appointment) appointmentsTableView.getSelectionModel().getSelectedItem();
            Parent parent;
            Stage stage;
            stage = (Stage) appointmentModifyButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyAppointment.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            ModifyAppointmentController controller = loader.getController();
            controller.setAppointment(selectedAppointment);
        } else {
            errorAlert(2);
        }

    }

    /**
     * The back button redirects the user to the main appointment page without saving any input.
     * @param actionEvent
     * @throws IOException
     */
    public void appointmentBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /**
     * The week radio button runs a query for all appointments in the DB between now and one week from now. Those
     * appointments are then loaded into an array list, and once populated, that array list is used to set the
     * appointment table view.
     * @param actionEvent
     */
    public void appointmentWeekRadio(ActionEvent actionEvent) {
        appointmentList.clear();
        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Appointments.Customer_ID, Appointments.User_ID FROM Appointments " +
                "INNER JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID " +
                "WHERE Appointments.Start <= now() + interval 7 day AND appointments.Start >= now();";

        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {

                int appointmentId = sqlResult.getInt("Appointment_ID");
                String appointmentTitle = sqlResult.getString("Title");
                String appointmentDescription = sqlResult.getString("Description");
                String appointmentLocation = sqlResult.getString("Location");
                String contactsName = sqlResult.getString("Contact_Name");
                String appointmentType = sqlResult.getString("Type");
                String appointmentStart = sqlResult.getString("Start");
                String appointmentEnd = sqlResult.getString("End");
                int customerId = sqlResult.getInt("Customer_ID");
                int userId = sqlResult.getInt("User_ID");

                Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, contactsName, appointmentType, appointmentStart, appointmentEnd, customerId, userId);
                appointmentList.addAll(appointment);

                appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
                contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactsName"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                startDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                endDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
                customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        appointmentsTableView.setItems(appointmentList);
    }

    /**
     * The month radio button runs a query for all appointments in the DB between now and one month from now. Those
     * appointments are then loaded into an array list, and once populated, that array list is used to set the
     * appointment table view.
     * @param actionEvent
     */
    public void appointmentMonthRadio(ActionEvent actionEvent) {
        appointmentList.clear();
        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Appointments.Customer_ID, Appointments.User_ID FROM Appointments " +
                "INNER JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID " +
                "WHERE Appointments.Start <= now() + interval 1 month AND appointments.Start >= now();";

        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {

                int appointmentId = sqlResult.getInt("Appointment_ID");
                String appointmentTitle = sqlResult.getString("Title");
                String appointmentDescription = sqlResult.getString("Description");
                String appointmentLocation = sqlResult.getString("Location");
                String contactsName = sqlResult.getString("Contact_Name");
                String appointmentType = sqlResult.getString("Type");
                String appointmentStart = sqlResult.getString("Start");
                String appointmentEnd = sqlResult.getString("End");
                int customerId = sqlResult.getInt("Customer_ID");
                int userId = sqlResult.getInt("User_ID");

                Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, contactsName, appointmentType, appointmentStart, appointmentEnd, customerId, userId);
                appointmentList.addAll(appointment);

                appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
                contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactsName"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                startDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                endDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
                customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        appointmentsTableView.setItems(appointmentList);
    }

    /**
     * The all radio button runs a query for all appointments in the DB. Those appointments are then loaded into an
     * array list, and once populated, that array list is used to set the appointment table view.
     * @param actionEvent
     */
    public void appointmentAll(ActionEvent actionEvent) {
        appointmentList.clear();
        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Appointments.Customer_ID, Appointments.User_ID FROM Appointments " +
                "INNER JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID";

        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {
                int appointmentId = sqlResult.getInt("Appointment_ID");
                String appointmentTitle = sqlResult.getString("Title");
                String appointmentDescription = sqlResult.getString("Description");
                String appointmentLocation = sqlResult.getString("Location");
                String contactsName = sqlResult.getString("Contact_Name");
                String appointmentType = sqlResult.getString("Type");
                String appointmentStart = sqlResult.getString("Start");
                String appointmentEnd = sqlResult.getString("End");
                int customerId = sqlResult.getInt("Customer_ID");
                int userId = sqlResult.getInt("User_ID");
                Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, contactsName, appointmentType, appointmentStart, appointmentEnd, customerId, userId);

                appointmentList.addAll(appointment);

                appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
                contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactsName"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                startDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                endDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
                customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        appointmentsTableView.setItems(appointmentList);

    }

    /**
     * The delete button first requires that a user has selected an appointment they wish to delete. Once confirmed
     * that there is a selected appointment, an alert confirming the user wishes to delete the appointment appears.
     * If the user selects that they wish to continue, a DELETE statement is run in the DB, and the appointment main
     * page is reloaded to reflect the dropped appointment.
     * @param actionEvent
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void appointmentDeleteButton(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {

        if(appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            Appointment selectedAppointment = (Appointment) appointmentsTableView.getSelectionModel().getSelectedItem();
            Integer appointmentId = selectedAppointment.getAppointmentId();
            String appointmentType = selectedAppointment.getAppointmentType();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Appointments");
            alert.setHeaderText("Cancel");
            alert.setContentText("Do you want to cancel appointment # " + appointmentId + " (" + appointmentType + " type appointment)?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                String sqlCascadeDeleteStatement = "DELETE FROM Appointments WHERE Appointment_ID = ?";
                DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlCascadeDeleteStatement);
                PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                preparedStatement.setInt(1, appointmentId);
                // Deletes the customer's appointments first

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
                        System.out.println("An error occurred and the appointment wasn't deleted.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }

        } else {
            errorAlert(1);
        }
    }

    /**
     * All error alerts are set here so they can be called instead of being created in each method.
     * @param errorCode
     */
    private void errorAlert(int errorCode) {
        if(errorCode == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cancel Appointment");
            alert.setContentText("Please select an appointment to cancel.");
            alert.showAndWait();
        } else if(errorCode == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Modify Appointment");
            alert.setContentText("Please select an appointment to modify.");
            alert.showAndWait();
        }
    }

    /**
     * local date time formatter takes UTC Date Time as a string and transforms it to the user's local date time. The
     * time is formatted and returned in the standard format that I've chosen for this project.
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

        return formattedDateTimeFull;
    }
}
