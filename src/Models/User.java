package Models;

public class User {
    public static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public User(String userName) {
        this.userName = userName;
    }
}
