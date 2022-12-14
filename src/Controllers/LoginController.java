package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.User;
import Models.UserLocalTime;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is the landing page for the application, handles login and logging services.
 */
public class LoginController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button loginPageLogin;
    @FXML
    public Button loginPageExit;
    public Label zoneIdLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label headingLabel;
    @FXML
    public Label loginErrorLabel;
    private ZoneId localZoneId = ZoneId.systemDefault();
    private ResourceBundle rb;
    public String locale = String.valueOf(Locale.getDefault());

    /**
     * initialize determines the user's locale and sets all labels, buttons, and alerts to English or French
     * based on the user's locale.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login Page is Initialized");

        zoneIdLabel.setText(UserLocalTime.userTimeZone);

        rb = ResourceBundle.getBundle("/Properties/" + locale);
        headingLabel.setText(rb.getString("heading"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginPageLogin.setText(rb.getString("login"));
        loginPageExit.setText(rb.getString("exit"));

    }

    /**
     * loginPageLogin grabs the user's input for the username and password fields, that input is then used in a sql
     * statement to determine if that user + password combination exists in the users table. If the login is successful,
     * the log function is called. The user's local date and time is then determined and a query is made to the DB
     * to determine whether there is an appointment within the next 15 minutes. An alert is created whether there is
     * an appointment or not.
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void loginPageLogin(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String userName = String.valueOf(usernameField.getText());
        String userPassword = String.valueOf(passwordField.getText());

        String sqlStatement = "SELECT COUNT(*) as total FROM users WHERE User_Name = \"" + userName + "\" AND Password = \"" + userPassword + "\";";

        PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
        ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
        sqlResult.next();
        if (sqlResult.getInt("total") == 1) {

            logLoginSuccess(userName);

            String ldt = String.valueOf(LocalDateTime.now(ZoneId.of(UserLocalTime.userTimeZone)));
            ldt = ldt.substring(0,19);
            String[] localTime = ldt.split("T");
            String formattedDateTimeFull = localTime[0] + " " + localTime[1];
            String ldtUtc = localToUtcDateTimeFormatter(formattedDateTimeFull);

            String appointmentSqlStatemtnt = "SELECT COUNT(*) apptTotal, Appointment_ID, Start FROM Appointments WHERE Start <= ? + interval 15 minute AND Start >= ?;";

            DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), appointmentSqlStatemtnt);
            PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

            preparedStatement.setString(1, String.valueOf(ldtUtc));
            preparedStatement.setString(2, String.valueOf(ldtUtc));

            ResultSet sqlTotalResult = preparedStatement.executeQuery();

            sqlTotalResult.next();

            if (sqlTotalResult.getInt("apptTotal") == 1) {

                int appointmentId = sqlTotalResult.getInt("Appointment_ID");
                String appointmentTime = sqlTotalResult.getString("Start");

                String appointmentTimeLocal = localDateTimeFormatter(appointmentTime);

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appointment");
                alert.setHeaderText("Appointment Reminder");
                alert.setContentText("Appointment " + appointmentId + " is scheduled in the next 15 minutes at " + appointmentTimeLocal + ".");
                alert.showAndWait();

            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appointment");
                alert.setHeaderText("Appointment Reminder");
                alert.setContentText("There are no appointments scheduled in the next 15 minutes.");
                alert.showAndWait();

            }

            User.setUserName(userName);

            Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
            Scene addPartScene = new Scene(add_product);
            Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addPartStage.setScene(addPartScene);
            addPartStage.show();
        } else {

            logLoginFailure(userName);
            rb = ResourceBundle.getBundle("/Properties/" + locale);
            loginErrorLabel.setText(rb.getString("invalid"));

        }

    }

    /**
     * Exits the application
     * @param actionEvent
     */
    public void loginPageExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * If the login is successful, the username and time is logged in the log file
     * @param username
     * @throws IOException
     */
    private void logLoginSuccess(String username) throws IOException {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String file = "login_activity";
            BufferedWriter bufferedFileWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedFileWriter.append("\nUser " + username + " logged into the system at " + currentTime.toString());
            bufferedFileWriter.flush();
            bufferedFileWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * If the login fails, the username and time is logged in the log file
     * @param username
     * @throws IOException
     */
    private void logLoginFailure(String username) throws IOException {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String file = "login_activity";
            BufferedWriter bufferedFileWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedFileWriter.append("\nUser " + username + " failed to log in to the system at " + currentTime.toString());
            bufferedFileWriter.flush();
            bufferedFileWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
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

}
