package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Database.DBConnection;


import java.io.IOException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Views/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 304, 370);
        stage.setTitle("Appointment Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        launch();
        DBConnection.closeConnection();
//
////        '2022-11-22 08:30:00'
//        String startDateFormatted = "2022-11-22 08:30:00";
//        String UTC_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
//
//        LocalDateTime localStartDateTime = LocalDateTime.parse(startDateFormatted, DateTimeFormatter.ofPattern(UTC_STANDARD_FORMAT));
//        ZonedDateTime systemStartZonedDateTime = localStartDateTime.atZone(ZoneId.systemDefault());
//        ZonedDateTime utcSqlStartTime = systemStartZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
//        Instant utcSqlStartTimestamp = systemStartZonedDateTime.toInstant();
//        System.out.println(utcSqlStartTimestamp);
//        String utcSqlStartTimestampString = String.valueOf(utcSqlStartTimestamp);
//        String utcSqlStartSubString = utcSqlStartTimestampString.substring(0,19);
//        System.out.println(utcSqlStartSubString);
//        String[] startTime = utcSqlStartSubString.split("T");
//        String startTimeFull = startTime[0] + " " + startTime[1];
//        System.out.println(startTimeFull);
    }

}