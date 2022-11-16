package Controllers;

import Models.Customer;
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
    public ComboBox<String> modifyCustomerCountryCombo;
    @FXML
    public ComboBox<String> modifyCustomerDivisionCombo;
    @FXML
    public TextField modifyCustomerNameField;
    @FXML
    public TextField modifyCustomerAddressField;
    @FXML
    public TextField modifyCustomerZipField;
    @FXML
    public TextField modifyCustomerPhoneField;
    public Button modifyCustomerSave;
    public Button modifyCustomerCancel;
    @FXML
    public TextField modifyCustomerId;

    Customer modifyCustomer;

    public void modifyCustomerSave(ActionEvent actionEvent) {
        System.out.println("Modify Customer Save Button");

    }

    public void modifyCustomerCancel(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    void setCustomer(Customer customer) {

        modifyCustomer = customer;

        modifyCustomerId.setText(Integer.toString(modifyCustomer.getCustomerId()));
        modifyCustomerNameField.setText(modifyCustomer.getCustomerName());
        modifyCustomerAddressField.setText(modifyCustomer.getCustomerAddress());
        modifyCustomerZipField.setText(modifyCustomer.getCustomerPostal());
        modifyCustomerPhoneField.setText(modifyCustomer.getCustomerPhone());
        modifyCustomerCountryCombo.getSelectionModel().select(modifyCustomer.getCustomerCountry());
        modifyCustomerDivisionCombo.getSelectionModel().select(modifyCustomer.getCustomerDivision());

    }

//    public void getCustomerModify(Inventory inv) {
//        this.inventory = inv;
//    }

}
