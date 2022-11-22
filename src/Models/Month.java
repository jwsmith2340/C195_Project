package Models;

public class Month {
    private final int monthTotal;
    private final String month;
    private final String appointmentType;

    public Month(int monthTotal, String month, String appointmentType) {
        this.monthTotal = monthTotal;
        this.month = month;
        this.appointmentType = appointmentType;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public int getMonthTotal() {
        return monthTotal;
    }

    public String getMonth() {
        return month;
    }
}
