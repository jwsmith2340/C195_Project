package Models;

import Database.DBConnection;
import Database.DBPreparedStatement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;


    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public Contact() {

    }


    public int getContactId(String contactName) throws ClassNotFoundException, SQLException {

        String sqlContactIdStatement = "SELECT Contact_ID from Contacts WHERE Contact_Name = ?";
        DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlContactIdStatement);
        PreparedStatement contactPreparedStatement = DBPreparedStatement.getPreparedStatement();

        contactPreparedStatement.setString(1, contactName);

        ResultSet sqlResult = contactPreparedStatement.executeQuery();
        sqlResult.next();
        int contactId = sqlResult.getInt("Contact_ID");

        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
