package Controllers;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import Database.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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
    public TableColumn customerPhoneColumn;
    @FXML
    public Button customersDeleteButton;
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
                String customerPostalCode = sqlResult.getString("Postal_Code");
                String customerPhone = sqlResult.getString("Phone");
                String customerCountry = sqlResult.getString("Country");
                String customerDivision = sqlResult.getString("Division");
                Customer customer = new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhone, customerCountry, customerDivision);
                customerList.addAll(customer);

                customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
                customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
                customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
                customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
                customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));

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
        if(customerMainTableView.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = (Customer) customerMainTableView.getSelectionModel().getSelectedItem();
            System.out.println(selectedCustomer);
            Parent parent;
            Stage stage;
            stage = (Stage) customersModifyButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyCustomer.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            ModifyCustomerController controller = loader.getController();
            controller.setCustomer(selectedCustomer);
        } else {
            errorAlert(2);
        }

    }

    public void customersBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void customersDeleteButton(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        // Don't forget the cascade delete for all events when this is deleted
        if(customerMainTableView.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = (Customer) customerMainTableView.getSelectionModel().getSelectedItem();
            Integer customerId = selectedCustomer.getCustomerId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Parts");
            alert.setHeaderText("Delete");
            alert.setContentText("Do you want to delete this customer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                String sqlCascadeDeleteStatement = "DELETE FROM Appointments WHERE Customer_ID = ?";
                DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlCascadeDeleteStatement);
                PreparedStatement preparedStatement = DBPreparedStatement.getPreparedStatement();

                preparedStatement.setInt(1, customerId);
                // Deletes the customer's appointments first
                try {
                    preparedStatement.execute();
                    if (preparedStatement.getUpdateCount() > 0) {
                        System.out.println("Number of rows affected: " + preparedStatement.getUpdateCount());
                    } else {
                        System.out.println("An error occurred and the customer's appointments weren't deleted.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                String sqlDeleteStatement = "DELETE FROM Customers WHERE Customer_ID = ?";

                DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlDeleteStatement);
                PreparedStatement secondPreparedStatement = DBPreparedStatement.getPreparedStatement();

                secondPreparedStatement.setInt(1, customerId);

                try {
                    secondPreparedStatement.execute();
                    if (secondPreparedStatement.getUpdateCount() > 0) {
                        System.out.println("Number of rows affected: " + secondPreparedStatement.getUpdateCount());
                        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
                        Scene addPartScene = new Scene(add_product);
                        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        addPartStage.setScene(addPartScene);
                        addPartStage.show();
                    } else {
                        System.out.println("An error occurred and the customer wasn't deleted.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }


        } else {
            errorAlert(1);
        }
    }

    private void errorAlert(int errorCode) {
        if(errorCode == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Delete Customer");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        } else if(errorCode == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Modify Customer");
            alert.setContentText("Please select a customer to modify.");
            alert.showAndWait();
        }
    }
}
