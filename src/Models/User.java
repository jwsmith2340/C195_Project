package Models;

/**
 * The User class allows for global static values to be set so the logged in user name can be accessed everywhere.
 */
public class User {
    public static String userName;

    public static String getUserName() {
        return userName;
    }

    /**
     * Sets the static username
     * @param userName
     */
    public static void setUserName(String userName) {
        User.userName = userName;
    }

    /**
     * The user constructor, takes in the name and creates a simple user object
     * @param userName
     */
    public User(String userName) {
        this.userName = userName;
    }
}
