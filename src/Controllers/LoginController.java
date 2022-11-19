package Controllers;

import Database.DBConnection;
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

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

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
//    public String locale = String.valueOf(Locale.getDefault());
    public String locale = "fr_FR"; // Here for manual testing



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login Page is Initialized");

        zoneIdLabel.setText(String.valueOf(localZoneId));
        System.out.println(locale);

        rb = ResourceBundle.getBundle("/Properties/" + locale);
        headingLabel.setText(rb.getString("heading"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginPageLogin.setText(rb.getString("login"));
        loginPageExit.setText(rb.getString("exit"));

    }

    public void loginPageLogin(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        // I don't think I need any input validation here
        String userName = String.valueOf(usernameField.getText());
        String userPassword = String.valueOf(passwordField.getText());

        String sqlStatement = "SELECT COUNT(*) as total FROM users WHERE User_Name = \"" + userName + "\" AND Password = \"" + userPassword + "\";";

        PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
        ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
        sqlResult.next();
        if (sqlResult.getInt("total") == 1) {


            // $$$$$$$$$$$$$$
            String appointmentSqlStatemtnt = "SELECT COUNT(*) apptTotal, Appointment_ID, Start FROM Appointments WHERE Start <= now() + interval 15 minute AND Start >= now();";

            PreparedStatement sqlAppointmentPreparedStatement = DBConnection.startConnection().prepareStatement(appointmentSqlStatemtnt);
            ResultSet appointmentSqlResult = sqlAppointmentPreparedStatement.executeQuery();
            appointmentSqlResult.next();

            if (appointmentSqlResult.getInt("apptTotal") == 1) {

                int appointmentId = appointmentSqlResult.getInt("Appointment_ID");
                String appointmentTime = appointmentSqlResult.getString("Start");

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appointment");
                alert.setHeaderText("Appointment Reminder");
                alert.setContentText("Appointment " + appointmentId + " is scheduled in the next 15 minutes at " + appointmentTime + ".");
                alert.showAndWait();

            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appointment");
                alert.setHeaderText("Appointment Reminder");
                alert.setContentText("There are no appointments scheduled in the next 15 minutes.");
                alert.showAndWait();

            }

            Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
            Scene addPartScene = new Scene(add_product);
            Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addPartStage.setScene(addPartScene);
            addPartStage.show();
        } else {
            rb = ResourceBundle.getBundle("/Properties/" + locale);
            loginErrorLabel.setText(rb.getString("invalid"));

        }

    }

    public void loginPageExit(ActionEvent actionEvent) {
        Platform.exit();
    }

//    public void loginPageLogin(ActionEvent actionEvent) {
//    }
}
