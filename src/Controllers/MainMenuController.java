package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class MainMenuController {
    public Button mainCustomerButton;
    public Button mainAppointmentButton;
    public Button mainReportsButton;
    public Button mainLogsButton;
    public Button mainExitButton;

    public void mainCustomerButton(ActionEvent actionEvent) {
    }

    public void mainAppointmentButton(ActionEvent actionEvent) {
    }

    public void mainReportsButton(ActionEvent actionEvent) {
    }

    public void mainLogsButton(ActionEvent actionEvent) {
    }

    public void mainExitButton(ActionEvent actionEvent) {
        Platform.exit();
    }
}
