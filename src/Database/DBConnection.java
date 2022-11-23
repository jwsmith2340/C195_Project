package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DBConnection class creates DB connections that can be called with a single line, greatly
 * reducing code clutter through the program.
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost:3306/";
    private static final String dbName = "client_schedule";

    public static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    private static final String MYSQLJBCDriver = "com.mysql.jdbc.Driver";

    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    private static Connection conn = null;

    /**
     * start connection opens a connection with the MySQL DB and allows for queries and insert statements to be performed
     * @return
     * @throws ClassNotFoundException
     */
    public static Connection startConnection() throws ClassNotFoundException {
        try {
            Class.forName(MYSQLJBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * This closes the connection to the DB
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException{
        conn.close();
        System.out.println("Connection closed.");
    }

}
