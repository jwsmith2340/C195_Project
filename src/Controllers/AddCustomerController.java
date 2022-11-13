package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddCustomerController {
    @FXML
    public ComboBox addCustomerCountryCombo;
    @FXML
    public ComboBox addCustomerDivisionCombo;
    @FXML
    public TextField addCustomerNameField;
    @FXML
    public TextField addCustomerAddressField;
    @FXML
    public TextField addCustomerZipField;
    @FXML
    public TextField addCustomerPhoneField;
    public Button addCustomerSave;
    public Button addCustomerCancel;

    public void addCustomerSave(ActionEvent actionEvent) {
    }

    public void addCustomerCancel(ActionEvent actionEvent) {
    }
}
