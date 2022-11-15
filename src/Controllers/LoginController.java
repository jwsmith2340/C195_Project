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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login Page is Initialized");

        zoneIdLabel.setText(String.valueOf(localZoneId));
        String locale = String.valueOf(Locale.getDefault());
        System.out.println(locale);
//        locale = "fr_FR"; // Manually set locale to France for testing

        rb = ResourceBundle.getBundle("/Properties/" + locale);
        headingLabel.setText(rb.getString("heading"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginPageLogin.setText(rb.getString("login"));
        loginPageExit.setText(rb.getString("exit"));

    }

    public void loginPageLogin(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String userName = String.valueOf(usernameField.getText());
        String userPassword = String.valueOf(passwordField.getText());

        String sqlStatement = "SELECT COUNT(*) as total FROM users WHERE User_Name = \"" + userName + "\" AND Password = \"" + userPassword + "\";";

        PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
        ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);

        sqlResult.next();
        if (sqlResult.getInt("total") != 1) {
            Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
            Scene addPartScene = new Scene(add_product);
            Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addPartStage.setScene(addPartScene);
            addPartStage.show();
        } else {
            loginErrorLabel.setText("Incorrect Username or Password");
        }

    }

    public void loginPageExit(ActionEvent actionEvent) {
        Platform.exit();
    }

}
