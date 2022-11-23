package Models;

import Database.DBConnection;
import Database.DBPreparedStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The contact class creates instances of the Contact class to populate the contact drop boxes
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * Contact constructor, creates a contact with id, name, and email
     * @param contactId
     * @param contactName
     * @param contactEmail
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * A contact constructor with zero arguments to create blank instances of contacts
     */
    public Contact() {

    }

    /**
     * Runs a commonly used sql query statement to reduce code clutter throughout the application.
     * Runs the select statement to return a contact id from the contact name, returns the id.
     * @param contactName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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

}
