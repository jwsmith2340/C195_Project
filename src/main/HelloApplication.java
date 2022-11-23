package main;

import Models.UserLocalTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import Database.DBConnection;

import java.io.IOException;
import java.text.ParseException;
import java.util.TimeZone;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        TimeZone timezone = TimeZone.getDefault();
        String localeTime = timezone.getID();
        UserLocalTime.setUserName(localeTime);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Views/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 304, 370);
        stage.setTitle("Appointment Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch();
        DBConnection.closeConnection();
    }

}