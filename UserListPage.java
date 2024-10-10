package asuHelloWorldJavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p> UserListPage Class </p>
 * 
 * <p> Description: This class represents the user list page where admin can view a list of all users and their roles.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for user list functionality in JavaFX
 */

public class UserListPage {

    // The primary stage that holds the UI for the user list page
    private Stage primaryStage;

    /**
     * Constructor that initializes the user list page with the primary stage.
     * 
     * @param primaryStage The primary stage used to display the user list page.
     */
    public UserListPage(Stage primaryStage) {
        // Assign the primary stage
        this.primaryStage = primaryStage;
    }

    /**
     * Displays the User List page where the admin can view users and navigate back to the Admin Dashboard.
     */
    public void show() {
        // Create a VBox layout with 10px spacing between elements
        VBox layout = new VBox(10);

        // Add padding of 20px around the layout
        layout.setPadding(new Insets(20));

        // Center align all elements within the layout
        layout.setAlignment(Pos.CENTER);

        // Set the background color to white
        layout.setStyle("-fx-background-color: #FFFFFF;");

        // Create and style a label for the page title
        Label titleLabel = new Label("User List");
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Create a TableView for displaying user data
        TableView<UserData> userTable = new TableView<>();

        // Set preferred width and height for the table
        userTable.setPrefWidth(400);
        userTable.setPrefHeight(300);

        // Set the table to automatically resize columns
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Define the "Username" column
        TableColumn<UserData, String> usernameColumn = new TableColumn<>("Username");

        // Set the value for the "Username" column from UserData
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        // Define the "Role" column
        TableColumn<UserData, String> roleColumn = new TableColumn<>("Role");

        // Set the value for the "Role" column from UserData
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        // Add the username and role columns to the TableView
        userTable.getColumns().addAll(usernameColumn, roleColumn);

        // Load the users from the database and populate the table
        loadUsers(userTable);

        // Create a button to navigate back to the Admin Dashboard
        Button backButton = new Button("Back to Admin Dashboard");

        // Set the action for the back button to show the Admin Dashboard
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        // Add the title label, user table, and back button to the layout
        layout.getChildren().addAll(titleLabel, userTable, backButton);

        // Create a new scene with the layout and set it on the primary stage
        Scene scene = new Scene(layout, 600, 400);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Set the title of the primary stage to "User List"
        primaryStage.setTitle("User List");

        // Show the primary stage
        primaryStage.show();
    }

    /**
     * Loads the list of users from the database and populates the TableView.
     * 
     * @param userTable The TableView to populate with user data.
     */
    private void loadUsers(TableView<UserData> userTable) {
        // SQL query to select the username and role from the Users table
        String query = "SELECT username, role FROM Users";

        // Try-with-resources block for connecting to the database and executing the query
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Loop through the result set and add each user to the TableView
            while (rs.next()) {
                String username = rs.getString("username");
                String role = rs.getString("role");

                // Add the user data to the TableView
                userTable.getItems().add(new UserData(username, role));
            }

        } catch (SQLException e) {
            // Print stack trace in case of SQL exceptions and show an error alert
            e.printStackTrace();
            showAlert("Error", "Could not load users.");
        }
    }

    /**
     * Displays an alert dialog with the provided title and content.
     * 
     * @param title The title of the alert dialog.
     * @param content The content of the alert dialog.
     */
    private void showAlert(String title, String content) {
        // Create an alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR);

        // Set the title of the alert dialog
        alert.setTitle(title);

        // Set the content of the alert dialog
        alert.setContentText(content);

        // Show the alert dialog and wait for user response
        alert.showAndWait();
    }

    /**
     * Inner class to represent user data (username and role) for display in the TableView.
     */
    public static class UserData {
        // Stores the username of the user
        private final String username;

        // Stores the role of the user
        private final String role;

        /**
         * Constructor that initializes UserData with a username and role.
         * 
         * @param username The username of the user.
         * @param role The role of the user.
         */
        public UserData(String username, String role) {
            // Assign the username
            this.username = username;

            // Assign the role
            this.role = role;
        }

        /**
         * Getter method for the username.
         * 
         * @return The username of the user.
         */
        public String getUsername() {
            return username;
        }

        /**
         * Getter method for the role.
         * 
         * @return The role of the user.
         */
        public String getRole() {
            return role;
        }
    }
}
