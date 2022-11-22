package Models;

public class Country {

    private final int apptTotal;
    private final String country;

    public Country(int apptTotal, String country) {
        this.apptTotal = apptTotal;
        this.country = country;
    }

    public int getApptTotal() {
        return apptTotal;
    }

    public String getCountry() {
        return country;
    }
}
