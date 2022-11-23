package Models;

/**
 * The Month class creates instances of the Month class
 */
public class Month {
    private final int monthTotal;
    private final String month;
    private final String appointmentType;

    /**
     * The month constructor creates instances of Month with a month total, month name, and appt type. This
     * constructor is primarily used to populate the month table view in the reports view.
     * @param monthTotal
     * @param month
     * @param appointmentType
     */
    public Month(int monthTotal, String month, String appointmentType) {
        this.monthTotal = monthTotal;
        this.month = month;
        this.appointmentType = appointmentType;
    }

    /**
     * returns a Month appointment type
     * @return
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * returns a Month total number of appointments
     * @return
     */
    public int getMonthTotal() {
        return monthTotal;
    }

    /**
     * returns a Month name
     * @return
     */
    public String getMonth() {
        return month;
    }
}
