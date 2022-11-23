package Models;

/**
 * The UserLocalTime class allows for global static values to be set so the user's local time zone value can be accessed
 */
public class UserLocalTime {

    public static String userTimeZone;
    public static String businessTimeZone;

    /**
     * sets the user's timezone
     * @param timeZone
     */
    public static void setUserName(String timeZone) {
        UserLocalTime.userTimeZone = timeZone;
    }

    /**
     * sets the business's time zone, ET in this program
     * @param businessTimeZone
     */
    public static void setBusinessTime(String businessTimeZone) {
        UserLocalTime.businessTimeZone = businessTimeZone;
    }

}
