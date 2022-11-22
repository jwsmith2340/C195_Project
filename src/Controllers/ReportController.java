package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Appointment;
import Models.Month;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportController implements Initializable {
    @FXML
    public TableView reportMonthTable;
    @FXML
    public TableColumn reportMonthMonthColumn;
    @FXML
    public TableColumn reportMonthTypeColumn;
    @FXML
    public TableColumn reportMonthCountColumn;
    @FXML
    public Button reportBackButton;
    @FXML
    public TableView reportAppointmentTable;
    @FXML
    public TableColumn reportAppointmentId;
    @FXML
    public TableColumn reportAppointmentTitle;
    @FXML
    public TableColumn reportAppointmentType;
    @FXML
    public TableColumn reportAppointmentDescription;
    @FXML
    public TableColumn reportAppointmentStart;
    @FXML
    public TableColumn reportAppointmentEnd;
    @FXML
    public TableColumn reportAppointmentCustomerId;
    @FXML
    public TableView reportCountryTable;
    @FXML
    public TableColumn reportCountryColumn;
    @FXML
    public TableColumn reportCountryApptNumCol;

    @FXML
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    ObservableList<Appointment> countryList = FXCollections.observableArrayList();

    @FXML
    ObservableList<Month> monthList = FXCollections.observableArrayList();

    String monthsArray[] = new String[] {
            "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("report initialized");
        populateAppointmentTable();
//        populateCountryTable();
        populateMonthsTable();
    }

    public void reportBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    private void populateAppointmentTable() {

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

                reportAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                reportAppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                reportAppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                reportAppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
                reportAppointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                reportAppointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
                reportAppointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        reportAppointmentTable.setItems(appointmentList);
    }

    private void populateCountryTable() {


    }

    private void populateMonthsTable() {

        AtomicInteger monthCount = new AtomicInteger();

        // First lambda expression
        Arrays.stream(monthsArray).forEach(month -> {
            monthCount.addAndGet(1);

            int monthInt = monthCount.intValue();

            String sqlStatementType = "SELECT Type FROM Appointments;";

            PreparedStatement sqlPreparedStatement = null;
            try {
                sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatementType);
                ResultSet sqlResultType = sqlPreparedStatement.executeQuery(sqlStatementType);
                while (sqlResultType.next()) {
                    String appointmentType = sqlResultType.getString("Type");

                    String sqlStatement = "SELECT COUNT(*) as total FROM Appointments WHERE MONTH(Start) = ? AND Type = ?;";
                    try {
                        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlStatement);
                        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                        preparedStatement.setInt(1, monthInt);
                        preparedStatement.setString(2, appointmentType);

                        ResultSet sqlResult = preparedStatement.executeQuery();

                        while (sqlResult.next()) {
                            int monthTotal = sqlResult.getInt("total");
                            System.out.println("Month total value:");
                            System.out.println(monthTotal);

                            Month appointmentMonthData = new Month(monthTotal, month, appointmentType);

                            monthList.addAll(appointmentMonthData);

                            reportMonthMonthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
                            reportMonthCountColumn.setCellValueFactory(new PropertyValueFactory<>("monthTotal"));
                            reportMonthTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));

                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }




            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }






        });

        reportMonthTable.setItems(monthList);

    }

}
