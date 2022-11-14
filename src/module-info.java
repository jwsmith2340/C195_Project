module c195.c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports main;
    opens main to javafx.fxml;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Models;
    opens Models to javafx.fxml;
}