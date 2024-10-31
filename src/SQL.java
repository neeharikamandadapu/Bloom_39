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

    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:appDatabase.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:appDatabase.db";

        // SQL statement for creating a new table
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

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
        createNewTable();
    }
}