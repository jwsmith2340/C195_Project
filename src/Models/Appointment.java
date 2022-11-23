package Models;

public class Appointment {

    private int appointmentId;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String contactsName;
    private String appointmentType;
    private String appointmentStart;
    private String appointmentEnd;
    private int customerId;
    private int userId;

    /**
     * The appointment constructor, takes in arguments to create instances of the appointment class
     * @param appointmentId
     * @param appointmentTitle
     * @param appointmentDescription
     * @param appointmentLocation
     * @param contactsName
     * @param appointmentType
     * @param appointmentStart
     * @param appointmentEnd
     * @param customerId
     * @param userId
     */
    public Appointment(int appointmentId, String appointmentTitle, String appointmentDescription, String appointmentLocation, String contactsName, String appointmentType, String appointmentStart, String appointmentEnd, int customerId, int userId) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.contactsName = contactsName;
        this.appointmentType = appointmentType;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.customerId = customerId;
        this.userId = userId;
    }

    /**
     * returns the appointment ID
     * @return
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * returns the appointment title
     * @return
     */
    public String getAppointmentTitle() {
        return this.appointmentTitle;
    }

    /**
     * returns the appointment description
     * @return
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * returns the appointment location
     * @return
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * returns the appointment contact name
     * @return
     */
    public String getContactsName() {
        return contactsName;
    }

    /**
     * returns the appointment type
     * @return
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * returns the appointment start local date time
     * @return
     */
    public String getAppointmentStartLocalDT() {
        return appointmentStart;
    }

    /**
     * returns the appointment end local date time
     * @return
     */
    public String getAppointmentEndLocalDT() {
        return appointmentEnd;
    }

    /**
     * returns the appointment customer id
     * @return
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * returns the appointment user id
     * @return
     */
    public int getUserId() {
        return userId;
    }

}
