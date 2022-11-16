package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void modifyCustomerSave(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        System.out.println("Modify Customer Save Button");
        java.util.Date datetime = new java.util.Date();
        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateTimeFormatted.format(datetime);

        Integer customerId = Integer.valueOf(modifyCustomerId.getText());
        String customerName = String.valueOf(modifyCustomerNameField.getText());
        String customerAddress = String.valueOf(modifyCustomerAddressField.getText());
        String customerZip = String.valueOf(modifyCustomerZipField.getText());
        String customerPhone = String.valueOf(modifyCustomerPhoneField.getText());
        String customerCountry = String.valueOf(modifyCustomerCountryCombo.getValue());
        String customerDivision = String.valueOf(modifyCustomerDivisionCombo.getValue());

        String sqlUpdateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE customer_id = ?";

        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlUpdateStatement);
        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, customerAddress);
        preparedStatement.setString(3, customerZip);
        preparedStatement.setString(4, customerPhone);
        preparedStatement.setString(5, currentTime);
        preparedStatement.setString(6, "updating user LOGIC NEEDED");
        preparedStatement.setInt(7, 66);
        preparedStatement.setInt(8, customerId);
        // This logic needs updated to parse the country/division info for validation and then return int div_id value,
        // also don't forget to validate if the country and division.country id matches

        try {
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Number of rows affected: " + preparedStatement.getUpdateCount());
                Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
                Scene addPartScene = new Scene(add_product);
                Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                addPartStage.setScene(addPartScene);
                addPartStage.show();
            } else {
                System.out.println("An error occurred and the customer wasn't updated.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
