package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    public void addCustomerCancel(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }
}
