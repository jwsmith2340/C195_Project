package Controllers;

import Database.DBConnection;
import Database.DBPreparedStatement;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCountryComboBox();
//        setDivisionComboBox();
    }

    public void addCustomerSave(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        java.util.Date datetime = new java.util.Date();
        java.text.SimpleDateFormat dateTimeFormatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = dateTimeFormatted.format(datetime);

        String customerName =String.valueOf(addCustomerNameField.getText());
        String customerAddress = String.valueOf(addCustomerAddressField.getText());
        String customerPostal = String.valueOf(addCustomerZipField.getText());
        String customerPhone = String.valueOf(addCustomerPhoneField.getText());
        String customerCountry = String.valueOf(addCustomerCountryCombo.getValue());
        String customerDivision = String.valueOf(addCustomerDivisionCombo.getValue());

        System.out.println(customerCountry);
        System.out.println(customerDivision);
//        String sqlSelectStatement = ""

        String sqlInsertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By," +
                "Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?)";

        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlInsertStatement);
        PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, customerAddress);
        preparedStatement.setString(3, customerPostal);
        preparedStatement.setString(4, customerPhone);
        preparedStatement.setString(5, currentTime);
        preparedStatement.setString(6, "user");
        preparedStatement.setString(7, currentTime);
        preparedStatement.setString(8, "user");
        preparedStatement.setInt(9, 66);

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

    public void addCustomerCancel(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

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
        addCustomerCountryCombo.getItems().addAll(countryList);
    }

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

    private int getDivisionId() {
        return 1;
    }

    public void addCustomerCountryCombo(ActionEvent actionEvent) {
        String countryName = addCustomerCountryCombo.getSelectionModel().getSelectedItem();
        System.out.println(countryName);
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
}
