package Models;

public class UserLocalTime {

    public static String userTimeZone;
    public static String businessTimeZone;

    public static void setUserName(String timeZone) {
        UserLocalTime.userTimeZone = timeZone;
    }

    public static void setBusinessTime(String businessTimeZone) {
        UserLocalTime.businessTimeZone = businessTimeZone;
    }

}
