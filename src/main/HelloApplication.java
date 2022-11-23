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
    /**
     * start sets global variables for the user's local timezone as well as ET time zone. It then loads the user
     * into the login page
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        TimeZone timezone = TimeZone.getDefault();
        String localeTime = timezone.getID();
        UserLocalTime.setUserName(localeTime);

        TimeZone businessTimeZone = TimeZone.getTimeZone("America/New_York");
        String businessTime = businessTimeZone.getID();
        UserLocalTime.setBusinessTime(businessTime);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Views/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 304, 370);
        stage.setTitle("Appointment Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * main is the gateway into the application and is the first thing that is ran in the program.
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        launch();
        DBConnection.closeConnection();
    }

}