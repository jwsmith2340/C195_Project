package Models;

public class Customer {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostal;
    private String customerPhone;
    private String customerCountry;
    private String customerDivision;

    public Customer(int customerId, String customerName, String customerAddress, String customerPostal, String customerPhone, String customerCountry, String customerDivision) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostal = customerPostal;
        this.customerPhone = customerPhone;
        this.customerCountry = customerCountry;
        this.customerDivision = customerDivision;
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

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerDivision() {
        return customerDivision;
    }

    public void setCustomerDivision(String customerDivision) {
        this.customerDivision = customerDivision;
    }
}
