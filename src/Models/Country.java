package Models;

/**
 * The country class creates instances of the country class
 */
public class Country {

    private final int apptTotal;
    private final String country;

    /**
     * The country constructor takes in the total number of appointments and the name of the country, this is
     * used to populate table views in the reports class
     * @param apptTotal
     * @param country
     */
    public Country(int apptTotal, String country) {
        this.apptTotal = apptTotal;
        this.country = country;
    }

    /**
     * returns the name of country in an instance of the Country class
     * @return
     */
    public String getCountry() {
        return country;
    }
}
