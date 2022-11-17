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
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    @FXML
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment main page initialized");

        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Customers.Customer_Name, Users.User_Name FROM Appointments INNER JOIN Customers ON " +
                "Appointments.Customer_ID = Customers.Customer_ID INNER JOIN Users on Appointments.User_ID = " +
                "Users.User_ID INNER JOIN Contacts on Appointments.Contact_ID = Contacts.Contact_ID";

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

    public void appointmentAddButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AddAppointment.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void appointmentModifyButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/ModifyAppointment.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void appointmentBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void appointmentWeekRadio(MouseEvent actionEvent) {
        appointmentList.clear();
        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Customers.Customer_Name, Users.User_Name FROM Appointments INNER JOIN Customers ON " +
                "Appointments.Customer_ID = Customers.Customer_ID INNER JOIN Users on Appointments.User_ID = Users.User_ID " +
                "INNER JOIN Contacts on Appointments.Contact_ID = Contacts.Contact_ID " +
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

    public void appointmentMonthRadio(ActionEvent actionEvent) {
        appointmentList.clear();
        String sqlStatement = "SELECT Appointments.Appointment_ID, Appointments.Title, Appointments.Description, " +
                "Appointments.Location, Contacts.Contact_Name, Appointments.Type, Appointments.Start, Appointments.End, " +
                "Customers.Customer_Name, Users.User_Name FROM Appointments INNER JOIN Customers ON " +
                "Appointments.Customer_ID = Customers.Customer_ID INNER JOIN Users on Appointments.User_ID = Users.User_ID " +
                "INNER JOIN Contacts on Appointments.Contact_ID = Contacts.Contact_ID " +
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

    public void initialize(ActionEvent actionEvent) {}


}
