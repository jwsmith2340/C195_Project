package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The prepared statement exists to create prepared statements to declutter code throughout the application
 */
public class DBPreparedStatement {

    private static PreparedStatement preparedStatement;

    /**
     * The setPreparedStatement method takes in the connection and the sql statement to create a prepared statement
     * with a valid connection to the DB. This method greatly reduces code clutter throughout the application.
     * @param conn
     * @param sqlStatement
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        preparedStatement = conn.prepareStatement(sqlStatement);
    }

    /**
     * A simple getter method for the prepared statement.
     * @return
     */
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
