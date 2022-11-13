package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAppointmentController {
    public TextField addAppointmentTitleField;
    public TextField addAppointmentDescriptionField;
    public TextField addAppointmentLocationField;
    public TextField addAppointmentContactField;
    public TextField addAppointmentTypeField;
    public DatePicker addAppointmentStartDatePicker;
    public DatePicker addAppointmentEndDatePicker;
    public ChoiceBox addAppointmentCusIdSelector;
    public ChoiceBox addAppointmentUserIdSelector;
    public Button addAppointmentSaveButton;
    public Button addAppointmentCancelButton;

    public void addAppointmentSaveButton(ActionEvent actionEvent) throws IOException {
    }

    public void addAppointmentCancelButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }
}
