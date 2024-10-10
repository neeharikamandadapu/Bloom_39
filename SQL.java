package asuHelloWorldJavaFX; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * <p> SQL Class </p>
 * 
 * <p> Description: This class handles SQLite database connection and table creation operations.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for SQLite connection and table creation functionality.
 */

public class SQL {

    /**
     * Establishes a connection to the SQLite database.
     */
    public static void connect() {
        // Initialize the connection object
        Connection conn = null;
        try {
            // SQLite database connection URL
            String url = "jdbc:sqlite:appDatabase.db";

            // Establish the connection to the database
            conn = DriverManager.getConnection(url);

            // Print success message on successful connection
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            // Print the exception message if a connection error occurs
            System.out.println(e.getMessage());
        } finally {
            try {
                // Close the connection if it is not null
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                // Print the exception message if an error occurs while closing the connection
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Creates a new "Users" table in the SQLite database if it does not already exist.
     */
    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:appDatabase.db";

        // SQL statement for creating the "Users" table
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "    user_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    username TEXT NOT NULL UNIQUE,\n"
                + "    password TEXT NOT NULL,\n"
                + "    first_name TEXT,\n"
                + "    middle_name TEXT,\n"
                + "    last_name TEXT,\n"
                + "    preferred_name TEXT,\n"
                + "    email TEXT,\n"
                + "    is_admin INTEGER DEFAULT 0,\n"
                + "    is_set_up INTEGER DEFAULT 0\n"
                + ");";

        // Try-with-resources block to automatically close the connection and statement
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Execute the SQL statement to create the "Users" table
            stmt.execute(sql);

            // Print success message on successful table creation
            System.out.println("Table created successfully");

        } catch (SQLException e) {
            // Print the exception message if an error occurs during table creation
            System.out.println(e.getMessage());
        }
    }

    /**
     * Main method to connect to the database and create the "Users" table.
     */
    public static void main(String[] args) {
        // Establish connection to the SQLite database
        connect();

        // Create the "Users" table in the database
        createNewTable();
    }
}
