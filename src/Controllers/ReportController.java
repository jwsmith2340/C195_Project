package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Appointment;
import Models.Country;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class handles the report view controller and its three table views
 */
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
    ObservableList<Country> countryList = FXCollections.observableArrayList();

    @FXML
    ObservableList<Month> monthList = FXCollections.observableArrayList();

    String monthsArray[] = new String[] {
            "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};

    /**
     * initialize sets the three table views on the Reports page by calling their respective methods
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("report initialized");
        populateAppointmentTable();
        populateCountryTable();
        populateMonthsTable();
    }

    /**
     * Redirects the user to the main menu
     * @param actionEvent
     * @throws IOException
     */
    public void reportBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /**
     * Populate appt table handles populating the main appointment table in the reports view. The sql statement is run
     * to grab all required column values for all the appointments in the DB. An array list is then populated with those
     * values and once all of the appointments have been added to the list, that list is used to populate the table view.
     */
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

                String appointmentStartLocalDT = localDateTimeFormatter(appointmentStart);
                String appointmentEndLocalDT = localDateTimeFormatter(appointmentEnd);

                Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, contactsName, appointmentType, appointmentStartLocalDT, appointmentEndLocalDT, customerId, userId);
                appointmentList.addAll(appointment);

                reportAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                reportAppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                reportAppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                reportAppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
                reportAppointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStartLocalDT"));
                reportAppointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEndLocalDT"));
                reportAppointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        reportAppointmentTable.setItems(appointmentList);
    }

    /**
     * Populate Country Table handles populating the country table in the Reports view. The sql statement is run
     * to grab all required column values for all the appointments in the DB. An array list is then populated with those
     * values and once all of the appointments have been added to the list, that list is used to populate the table view.
     */
    private void populateCountryTable() {
        String sqlStatement = "SELECT COUNT(*) as total, Country FROM Appointments INNER JOIN Customers ON " +
                "Appointments.Customer_ID = Customers.Customer_ID INNER JOIN first_level_divisions ON \n" +
                "Customers.Division_ID = first_level_divisions.Division_ID INNER JOIN Countries ON " +
                "first_level_divisions.Country_ID = Countries.Country_ID GROUP BY Countries.Country;";

        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {
                int apptTotal = sqlResult.getInt("total");
                String country = sqlResult.getString("Country");

                Country countryObj = new Country(apptTotal, country);
                countryList.addAll(countryObj);

                reportCountryApptNumCol.setCellValueFactory(new PropertyValueFactory<>("apptTotal"));
                reportCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        reportCountryTable.setItems(countryList);

    }

    /**
     * Populate Months Table handles populating the month/type view table in the Reports view. The sql statement is run
     * to grab all required column values for all the appointments in the DB. An array list is then populated with those
     * values and once all of the appointments have been added to the list, that list is used to populate the table view.
     *
     * LAMBDA #1- This method uses a lambda expression to loop through the monthsArray, which is declared in the variable
     * declaration section of this class. The lambda loops through those countries while incrementing a count variable so
     * that 12 queries can be run while only writing one loop instead of 12. This lambda expression greatly cut down on
     * unnecessary code.
     */
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

    /**
     * This method takes in a date time value as UTC and converts it to the user's local date time. That date time is
     * then formatted, and the formatted value is returned.
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
