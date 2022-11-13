package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ModifyCustomerController {
    @FXML
    public ComboBox modifyCustomerCountryCombo;
    @FXML
    public ComboBox modifyCustomerDivisionCombo;
    @FXML
    public TextField modifyCustomerNameField;
    @FXML
    public TextField modifyCustomerAddressField;
    @FXML
    public TextField modifyCustomerZipField;
    @FXML
    public TextField modifyCustomerPhoneField;
    public Button addCustomerSave;
    public Button addCustomerCancel;

    public void addCustomerSave(ActionEvent actionEvent) {
    }

    public void addCustomerCancel(ActionEvent actionEvent) {
    }
}
