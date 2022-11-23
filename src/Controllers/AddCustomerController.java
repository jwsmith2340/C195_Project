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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * AddCustomerController handles adding a new customer in the AddCustomer view. User input is taken and a
 * new customer is generated after input validation is performed.
 */
public class AddCustomerController implements Initializable {
    @FXML
    public ComboBox<String> addCustomerCountryCombo;
    @FXML
    public ComboBox<String> addCustomerDivisionCombo;
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

    /**
     * Initialize sets the country combo box. The division combo box is not set here because its values
     * based on the country selected
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCountryComboBox();
    }

    /**
     * addCustomerSave first gets the local time and converts it to UTC for the DB. All text fields and combo box values
     * are then assigned to variables, and null checks are performed to ensure user input. Validation is performed, and
     * if validation passes, an INSERT statement is run to create a new customer. The user is then redirected to the
     * main customer view.
     * @param actionEvent
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void addCustomerSave(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {

        ZoneId utcZoneId = ZoneId.of("UTC");
        LocalDateTime utcDateTime = LocalDateTime.now(utcZoneId);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        String utcFormattedDateTime = utcDateTime.format(dateTimeFormatter);

        String customerName =String.valueOf(addCustomerNameField.getText());
        String customerAddress = String.valueOf(addCustomerAddressField.getText());
        String customerPostal = String.valueOf(addCustomerZipField.getText());
        String customerPhone = String.valueOf(addCustomerPhoneField.getText());
        String customerCountry = String.valueOf(addCustomerCountryCombo.getValue());
        String customerDivision = String.valueOf(addCustomerDivisionCombo.getValue());

        if (customerCountry != "null") {

            if (customerDivision != "null") {

                Customer customer = new Customer();
                Integer customerCountryId = customer.getCustomerCountryId(customerCountry);
                Integer customerDivisionId = customer.getCustomerDivisionId(customerDivision);

                if (customerFieldTypeValidation(customerName, customerAddress, customerPostal, customerPhone)) {

                    String sqlInsertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By," +
                            "Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?)";

                    DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
                    PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                    preparedStatement.setString(1, customerName);
                    preparedStatement.setString(2, customerAddress);
                    preparedStatement.setString(3, customerPostal);
                    preparedStatement.setString(4, customerPhone);
                    preparedStatement.setString(5, utcFormattedDateTime);
                    preparedStatement.setString(6, User.userName);
                    preparedStatement.setString(7, utcFormattedDateTime);
                    preparedStatement.setString(8, User.userName);
                    preparedStatement.setInt(9, customerDivisionId);

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
                            System.out.println("An error occurred and no customers were created.");
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
     * addCustomerCancel redirects the user to the CustomerMain view without saving any input.
     * @param actionEvent
     * @throws IOException
     */
    public void addCustomerCancel(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /**
     * setCountryComboBox is run when the AddCustomer view is visited. A query is made to the db to get all countries,
     * and that data then populates the countryList, which is then used to populate the country combo box.
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

        addCustomerCountryCombo.getItems().clear();
//        addCustomerCountryCombo.getItems().addAll(countryList);
        countryList.forEach(country -> {
            addCustomerCountryCombo.getItems().add(country);
        });
        // addCustomerCountryCombo.getItems().add()     GOOD CANDIDATE FOR 2ND LAMBDA
    }

    /**
     * setDivisionComboBox is called whenever a country is selected from the country combo box. Once a country is
     * selected, a query is run on the first_level_divisions table where that country's ID matches that division.
     * The division combo box is then populated with those values.
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

        addCustomerDivisionCombo.getItems().clear();
        addCustomerDivisionCombo.getItems().addAll(divisionList);
    }

    /**
     * When a country is selected, this method is run. It selects the country ID for use in the setDivisionComboBox
     * method. It also enables the division Combo Box, which had been disabled until a country was selected.
     * @param actionEvent
     */
    public void addCustomerCountryCombo(ActionEvent actionEvent) {
        String countryName = addCustomerCountryCombo.getSelectionModel().getSelectedItem();
        String sqlStatement = "SELECT Country_ID FROM Countries WHERE Country = ?;";

        try {
            DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlStatement);
            PreparedStatement sqlPreparedStatement = DBPreparedStatement.getPreparedStatement();

            sqlPreparedStatement.setString(1, countryName);

            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            try {
                sqlResult.next();

                int countryId = sqlResult.getInt("Country_ID");
                addCustomerDivisionCombo.setDisable(false);
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
     * This method provides validation for user input.
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
     * All error alerts are set with appropriate values to declutter the other methods.
     * @param errorCode
     */
    private void errorAlert(int errorCode) {
        if(errorCode == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Name Validation");
            alert.setContentText("Please enter a name to create a new customer record.");
            alert.showAndWait();
        } else if(errorCode == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Address Validation");
            alert.setContentText("Please enter an address to create a new customer record.");
            alert.showAndWait();
        } else if(errorCode == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Postal Code Validation");
            alert.setContentText("Please enter a postal code to create a new customer record.");
            alert.showAndWait();
        } else if(errorCode == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Phone Number Validation");
            alert.setContentText("Please enter a phone number to create a new customer record.");
            alert.showAndWait();
        } else if(errorCode == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Country Validation");
            alert.setContentText("Please enter a country to create a new customer record.");
            alert.showAndWait();
        } else if(errorCode == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Division Validation");
            alert.setContentText("Please enter a first level division to create a new customer record.");
            alert.showAndWait();
        }

    }

}
