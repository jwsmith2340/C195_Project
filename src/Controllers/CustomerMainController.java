package Controllers;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import Database.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerMainController implements Initializable {

    @FXML
    public TableView customerMainTableView;
    @FXML
    public TableColumn customerIdColumn;
    @FXML
    public TableColumn customerNameColumn;
    @FXML
    public TableColumn customerAddressColumn;
    @FXML
    public TableColumn customerPostalColumn;
    @FXML
    public TableColumn customerCountryColumn;
    @FXML
    public TableColumn customerDivisionColumn;
    @FXML
    public Button customersAddButton;
    @FXML
    public Button customersModifyButton;
    @FXML
    public Button customersBackButton;
    @FXML
    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Customer main page initialized");

        String sqlStatement = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Country, Division\n" +
                "FROM CUSTOMERS \n" +
                "INNER JOIN first_level_divisions \n" +
                "INNER JOIN countries\n" +
                "WHERE customers.Division_ID = first_level_divisions.Division_ID\n" +
                "AND first_level_divisions.Country_ID = countries.Country_ID;";
        try {
            PreparedStatement sqlPreparedStatement = DBConnection.startConnection().prepareStatement(sqlStatement);
            ResultSet sqlResult = sqlPreparedStatement.executeQuery(sqlStatement);
            while (sqlResult.next()) {

                int customerId = sqlResult.getInt("Customer_ID");
                String customerName = sqlResult.getString("Customer_Name");
                String customerAddress = sqlResult.getString("Address");
                int customerPostalCode = sqlResult.getInt("Postal_Code");
                int customerPhone = sqlResult.getInt("Phone");
                String customerCountry = sqlResult.getString("Country");
                String customerDivision = sqlResult.getString("Division");
                Customer customer = new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhone, customerCountry, customerDivision);
                customerList.addAll(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        customerMainTableView.setItems(customerList);
    }

    public void customersAddButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/AddCustomer.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void customersModifyButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/ModifyCustomer.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void customersBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

}
