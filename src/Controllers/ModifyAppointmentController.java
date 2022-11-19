package Controllers;

import Models.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ModifyAppointmentController {

    @FXML
    public TextField modifyAppointmentTitleField;
    @FXML
    public TextField modifyAppointmentDescriptionField;
    @FXML
    public TextField modifyAppointmentLocationField;
    @FXML
    public TextField modifyAppointmentTypeField;
    @FXML
    public ChoiceBox modifyAppointmentCusIdSelector;
    @FXML
    public ChoiceBox modifyAppointmentUserIdSelector;
    @FXML
    public Button modifyAppointmentSaveButton;
    @FXML
    public Button modifyAppointmentCancelButton;
    @FXML
    public DatePicker modifyAppointmentDatePicker;
    @FXML
    public ComboBox startTimeCombo;
    @FXML
    public ComboBox endTimeCombo;
    @FXML public ComboBox modifyAppointmentContactCombo;
    @FXML
    public TextField modifyAppointmentIdField;

    Appointment modifyAppointment;

    public void addAppointmentSaveButton(ActionEvent actionEvent) throws IOException {
    }

    public void setAppointment(Appointment selectedAppointment) {

        modifyAppointment = selectedAppointment;

        String rawDate = String.valueOf(selectedAppointment.getAppointmentStart());
        String[] apptDate = rawDate.split(" ");
        String parsedDate = apptDate[0];
        String parsedStartTime = apptDate[1];

        String rawEnd = String.valueOf(selectedAppointment.getAppointmentEnd());
        String[] endTime = rawEnd.split(" ");
        String parsedEndTime = endTime[1];

        modifyAppointmentIdField.setText(Integer.toString(selectedAppointment.getAppointmentId()));
        modifyAppointmentTitleField.setText(selectedAppointment.getAppointmentTitle());
        modifyAppointmentDescriptionField.setText(selectedAppointment.getAppointmentDescription());
        modifyAppointmentLocationField.setText(selectedAppointment.getAppointmentLocation());
        modifyAppointmentContactCombo.getSelectionModel().select(selectedAppointment.getContactsName());
        modifyAppointmentTypeField.setText(selectedAppointment.getAppointmentType());
        modifyAppointmentDatePicker.setValue(LocalDate.parse(parsedDate));
        startTimeCombo.setValue(parsedStartTime);
        endTimeCombo.setValue(parsedEndTime);
        modifyAppointmentCusIdSelector.setValue(selectedAppointment.getCustomerId());
        modifyAppointmentCusIdSelector.setValue(selectedAppointment.getUserId());

    }

    public void addAppointmentIdField(ActionEvent actionEvent) {
    }

    public void modifyAppointmentSaveButton(ActionEvent actionEvent) {
    }

    public void modifyAppointmentCancelButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void modifyAppointmentDatePicker(ActionEvent actionEvent) {
    }

    public void startTimeCombo(ActionEvent actionEvent) {
    }

    public void endTimeCombo(ActionEvent actionEvent) {
    }
}
