package Controllers;

import Database.DBConnection;
import Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
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
    @FXML
    ObservableList<String> countryList = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> divisionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCountryComboBox();
        setDivisionComboBox();
    }

    public void addCustomerSave(ActionEvent actionEvent) {
        String customerName =String.valueOf(addCustomerNameField.getText());
        String customerAddress = String.valueOf(addCustomerAddressField.getText());
        String customerPostal = String.valueOf(addCustomerZipField.getText());
        String customerPhone = String.valueOf(addCustomerPhoneField.getText());
        String customerCountry = String.valueOf(addCustomerCountryCombo.getValue());
        String customerDivision = String.valueOf(addCustomerDivisionCombo.getValue());
    }

    public void addCustomerCancel(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    private void setCountryComboBox() {
        String sqlStatement = "SELECT Country FROM countries;";
        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {

                String country = sqlResult.getString("Country");
                countryList.add(country);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        addCustomerCountryCombo.setItems(countryList);
    }

    private void setDivisionComboBox() {
        String sqlStatement = "SELECT Division FROM first_level_divisions;";
        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {

                String division = sqlResult.getString("Country");
                divisionList.add(division);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        addCustomerDivisionCombo.setItems(divisionList);
    }

}
