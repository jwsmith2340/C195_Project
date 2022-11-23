package Models;

import Database.DBConnection;
import Database.DBPreparedStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Customer class constructs instances of the Customer class and provides getter methods to return information
 * on customer objects
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostal;
    private String customerPhone;
    private String customerCountry;
    private String customerDivision;
    private int customerCountryId;
    private int customerDivisionId;

    /**
     * The customer constructor builds customers using several data values
     * @param customerId
     * @param customerName
     * @param customerAddress
     * @param customerPostal
     * @param customerPhone
     * @param customerCountry
     * @param customerDivision
     */
    public Customer(int customerId, String customerName, String customerAddress, String customerPostal, String customerPhone, String customerCountry, String customerDivision) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostal = customerPostal;
        this.customerPhone = customerPhone;
        this.customerCountry = customerCountry;
        this.customerDivision = customerDivision;
    }

    /**
     * A blank customer constructor is used to create empty customer objects
     */
    public Customer() {

    }

    /**
     * returns the customer phone number
     * @return
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * returns the customer id
     * @return
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * returns the customer name
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * returns the customer address
     * @return
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * returns the customer postal code
     * @return
     */
    public String getCustomerPostal() {
        return customerPostal;
    }

    /**
     * returns the customer country
     * @return
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * returns the customer country id. This runs a sql statement that takes a country name
     * as input and returns the country id
     * @param customerCountryName
     * @return
     */
    public int getCustomerCountryId(String customerCountryName) {
        String sqlStatement = "SELECT Country_ID FROM Countries WHERE Country = ?;";

        try {
            DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlStatement);
            PreparedStatement sqlPreparedStatement = DBPreparedStatement.getPreparedStatement();

            sqlPreparedStatement.setString(1, customerCountryName);

            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            try {
                sqlResult.next();

                int countryId = sqlResult.getInt("Country_ID");

                return countryId;

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
     * returns the customer division
     * @return
     */
    public String getCustomerDivision() {
        return customerDivision;
    }

    /**
     * returns the customer division id. This method takes in a string name of a division and
     * returns the division id.
     * @param customerDivisionName
     * @return
     */
    public int getCustomerDivisionId(String customerDivisionName) {
        String sqlStatement = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?;";

        try {
            DBPreparedStatement.setPreparedStatement(DBConnection.startConnection(), sqlStatement);
            PreparedStatement sqlPreparedStatement = DBPreparedStatement.getPreparedStatement();

            sqlPreparedStatement.setString(1, customerDivisionName);

            ResultSet sqlResult = sqlPreparedStatement.executeQuery();
            try {
                sqlResult.next();

                int divisionId = sqlResult.getInt("Division_ID");

                return divisionId;

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
