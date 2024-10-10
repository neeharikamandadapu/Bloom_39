package asuHelloWorldJavaFX;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p> DatabaseConnection Class </p>
 * 
 * <p> Description: This class handles the connection to the SQLite database used by the application.
 * It provides methods to establish the database connection and initialize the necessary tables. 
 * These include the Users table, which stores user information and roles, and the Invitations table, 
 * which handles user invitations. </p>
 * 
 * <p> The `connect` method establishes the database connection, and the `createUsersTable` and 
 * `createInvitationsTable` methods ensure the existence of the necessary tables. 
 * The `initializeDatabase` method is used to initialize the database and create these tables 
 * when the application starts. </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00 2024-10-09 Initial implementation of database connection and table creation.
 */
public class DatabaseConnection {

    // Connection string to the SQLite database
    private static final String URL = "jdbc:sqlite:appDatabase.db";

    /**
     * Establishes a connection to the SQLite database.
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Creates the Users table if it does not already exist.
     * This table stores user information such as username, password, names, email, role, 
     * and boolean flags for admin status and setup status.
     */
    public static void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "first_name TEXT, "
                + "middle_name TEXT, "
                + "last_name TEXT, "
                + "preferred_name TEXT, "
                + "email TEXT, "
                + "role TEXT NOT NULL, "
                + "is_admin BOOLEAN NOT NULL, "
                + "is_set_up BOOLEAN NOT NULL);";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);  // Execute the SQL command to create the Users table
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace if there is a database error
        }
    }

    /**
     * Creates the Invitations table if it does not already exist.
     * This table handles the storage of user invitations, including the username, email,
     * invitation code, role, and whether the invitation has been used.
     */
    public static void createInvitationsTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Invitations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "email TEXT NOT NULL, "
                + "invitation_code TEXT NOT NULL, "
                + "role TEXT NOT NULL, "
                + "is_used BOOLEAN DEFAULT 0);";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);  // Execute the SQL command to create the Invitations table
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace if there is a database error
        }
    }

    /**
     * Initializes the database by creating the required tables (Users and Invitations).
     * This method is typically called when the application starts to ensure that all tables are present.
     */
    public static void initializeDatabase() {
        try (Connection conn = connect()) {
            if (conn != null) {
                createUsersTable();  // Create the Users table
                createInvitationsTable();  // Create the Invitations table
                System.out.println("Database initialization complete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace if there is a database error
        }
    }
}
