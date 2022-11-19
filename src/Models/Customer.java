package Models;

import Database.DBConnection;
import Database.DBPreparedStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public Customer(int customerId, String customerName, String customerAddress, String customerPostal, String customerPhone, String customerCountry, String customerDivision) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostal = customerPostal;
        this.customerPhone = customerPhone;
        this.customerCountry = customerCountry;
        this.customerDivision = customerDivision;
    }

    public Customer() {

    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPostal() {
        return customerPostal;
    }

    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

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

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerDivision() {
        return customerDivision;
    }

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

    public void setCustomerDivision(String customerDivision) {
        this.customerDivision = customerDivision;
    }
}
