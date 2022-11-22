package Controllers;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    @FXML
    public TableView reportMonthTable;
    @FXML
    public TableColumn reportMonthMonthColumn;
    @FXML
    public TableColumn reportMonthTypeColumn;
    @FXML
    public TableColumn reportMonthCountColumn;
    @FXML
    public Button reportBackButton;
    @FXML
    public TableView reportAppointmentTable;
    @FXML
    public TableColumn reportAppointmentId;
    @FXML
    public TableColumn reportAppointmentTitle;
    @FXML
    public TableColumn reportAppointmentType;
    @FXML
    public TableColumn reportAppointmentDescription;
    @FXML
    public TableColumn reportAppointmentStart;
    @FXML
    public TableColumn reportAppointmentEnd;
    @FXML
    public TableColumn reportAppointmentCustomerId;
    @FXML
    public TableView reportCountryTable;
    @FXML
    public TableColumn reportCountryColumn;
    @FXML
    public TableColumn reportCountryApptNumCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("report initialized");

    }

    public void reportBackButton(ActionEvent actionEvent) throws IOException {
        Parent add_product = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        Scene addPartScene = new Scene(add_product);
        Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

}
