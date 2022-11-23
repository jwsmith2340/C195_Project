package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
import Models.Customer;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * modify customer controller handles the modify customer view for modifying customers
 */
public class ModifyCustomerController implements Initializable {
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

    @FXML
    ObservableList<String> countryList = FXCollections.observableArrayList();
    @FXML
    ObservableList<String> divisionList = FXCollections.observableArrayList();

    Customer modifyCustomer;

    /**
     * initialize sets the country combo box so the values appear on load
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCountryComboBox();
    }

    /**
     * The modify save first sets the current time in UTC time and then sets the text fields. Validation is then
     * performed and the division id is set as an integer so it can be set in the SQL db properly.
     * @param actionEvent
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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

        if (customerCountry != "null") {

            if (customerDivision != "null") {

                Customer customer = new Customer();
                Integer customerCountryId = customer.getCustomerCountryId(customerCountry);
                Integer customerDivisionId = customer.getCustomerDivisionId(customerDivision);

                if (customerFieldTypeValidation(customerName, customerAddress, customerZip, customerPhone)) {

                    String sqlUpdateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                            "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE customer_id = ?";

                    DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlUpdateStatement);
                    PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                    preparedStatement.setString(1, customerName);
                    preparedStatement.setString(2, customerAddress);
                    preparedStatement.setString(3, customerZip);
                    preparedStatement.setString(4, customerPhone);
                    preparedStatement.setString(5, currentTime);
                    preparedStatement.setString(6, User.userName);
                    preparedStatement.setInt(7, customerDivisionId);
                    preparedStatement.setInt(8, customerId);

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

            } else {
                errorAlert(6);
            }

        } else {
            errorAlert(5);
        }

    }

    /**
     * Redirects the user to the main customer view
     * @param actionEvent
     * @throws IOException
     */
    public void modifyCustomerCancel(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /**
     * Sets the customer fields in the modify view
     * @param customer
     */
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

    /**
     * Sets the country combo box with all available countries after running a SQL Select statment
     */
    private void setCountryComboBox() {
        String sqlStatement = "SELECT country FROM countries;";
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

        modifyCustomerCountryCombo.getItems().clear();
        modifyCustomerCountryCombo.getItems().addAll(countryList);
    }

    /**
     * The division combo box is set when this method is called, which is each time a new country is selected in the
     * country combo box.
     * @param Country_ID
     */
    private void setDivisionComboBox(int Country_ID) {
        divisionList.clear();
        String sqlStatement = "SELECT division FROM first_level_divisions WHERE Country_ID = ?;";

        try {
            DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlStatement);
            PreparedStatement sqlPreparedStatement = DBPreparedStatement.getPreparedStatement();

            sqlPreparedStatement.setInt(1, Country_ID);

            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            while (sqlResult.next()) {

                String division = sqlResult.getString("Division");
                divisionList.add(division);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        modifyCustomerDivisionCombo.getItems().clear();
        modifyCustomerDivisionCombo.getItems().addAll(divisionList);
    }

    /**
     * This method sets the country value when a country is selected in the combo box. It also calls the set Division
     * Combo Box method to update the div combo box based on the currently selected country.
     * @param actionEvent
     */
    public void modifyCustomerCountryCombo(ActionEvent actionEvent) {
        String countryName = modifyCustomerCountryCombo.getSelectionModel().getSelectedItem();
        String sqlStatement = "SELECT Country_ID FROM Countries WHERE Country = ?;";

        try {
            DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlStatement);
            PreparedStatement sqlPreparedStatement = DBPreparedStatement.getPreparedStatement();

            sqlPreparedStatement.setString(1, countryName);

            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            try {
                sqlResult.next();

                int countryId = sqlResult.getInt("Country_ID");
                modifyCustomerDivisionCombo.setDisable(false);
                setDivisionComboBox(countryId);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This validation method checks each text field value and displays an error alert if validation fails. It also returns
     * a boolean value if the validation passes or fails.
     * @param customerName
     * @param customerAddress
     * @param customerPostal
     * @param customerPhone
     * @return
     */
    public boolean customerFieldTypeValidation(String customerName, String customerAddress, String customerPostal, String customerPhone) {

        boolean validationResult = true;

        if (customerName.length() == 0) {
            errorAlert(1);
            validationResult = false;
        }

        if (customerAddress.length() == 0) {
            errorAlert(2);
            validationResult = false;
        }

        if (customerPostal.length() == 0) {
            errorAlert(3);
            validationResult = false;
        }

        if (customerPhone.length() == 0) {
            errorAlert(4);
            validationResult = false;
        }

        return validationResult;

    }

    /**
     * Sets all error alert messages so they can be called from other methods.
     * @param errorCode
     */
    private void errorAlert(int errorCode) {
        if(errorCode == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Name Validation");
            alert.setContentText("Please enter a name to modify a customer record.");
            alert.showAndWait();
        } else if(errorCode == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Address Validation");
            alert.setContentText("Please enter an address to modify a customer record.");
            alert.showAndWait();
        } else if(errorCode == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Postal Code Validation");
            alert.setContentText("Please enter a postal code to modify a customer record.");
            alert.showAndWait();
        } else if(errorCode == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Phone Number Validation");
            alert.setContentText("Please enter a phone number to modify a customer record.");
            alert.showAndWait();
        } else if(errorCode == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Country Validation");
            alert.setContentText("Please enter a country to modify a customer record.");
            alert.showAndWait();
        } else if(errorCode == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Division Validation");
            alert.setContentText("Please enter a first level division to modify a customer record.");
            alert.showAndWait();
        }

    }

}
