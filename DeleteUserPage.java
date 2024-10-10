package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p> DeleteUserPage Class </p>
 * 
 * <p> Description: This class represents a JavaFX page for deleting users from the database.
 * The page includes a form where the admin can enter the username of the user they wish to delete.
 * It features confirmation dialogs for deletion and interacts with the database to remove the
 * specified user from the Users table. </p>
 * 
 * <p> The `show` method sets up the UI, including buttons for deletion and returning to the admin
 * dashboard. The `handleDeleteUser` method validates user input and displays a confirmation dialog.
 * The `deleteUserFromDatabase` method deletes the user from the database. </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 *  
 * @version 1.00 2024-10-09 Initial implementation of user deletion functionality.
 */
public class DeleteUserPage {

    private Stage primaryStage;  // Field for the primary stage

    /**
     * Constructor that sets the primary stage for this page.
     */
    public DeleteUserPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Displays the delete user page with input fields and buttons.
     * This method sets up the UI layout for deleting a user.
     */
    public void show() {
        VBox layout = new VBox(10);  // VBox layout for vertical alignment with 10px spacing
        layout.setPadding(new Insets(20));  // Add padding around the layout
        layout.setAlignment(Pos.CENTER);  // Center all elements
        layout.setStyle("-fx-background-color: #FFFFFF;");  // Set background color

        // Label for the page title
        Label titleLabel = new Label("Delete User");
        titleLabel.setStyle("-fx-font-size: 24px;");  // Set font size for the title

        // Text field for entering the username of the user to be deleted
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username to Delete");

        // Button for confirming user deletion
        Button deleteButton = new Button("Delete");

        // Set the action for the delete button
        deleteButton.setOnAction(e -> handleDeleteUser(usernameField.getText()));

        // Button for going back to the Admin Dashboard
        Button backButton = new Button("Back to Admin Dashboard");
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);  // Navigate back to the admin dashboard
            adminDashboard.show();
        });

        // Add all components (title, input field, buttons) to the layout
        layout.getChildren().addAll(titleLabel, usernameField, deleteButton, backButton);

        // Set up the scene and show it
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Delete User");
        primaryStage.show();
    }

    /**
     * Handles the deletion process by validating input and confirming the action.
     */
    private void handleDeleteUser(String username) {
        if (username.isEmpty()) {  // Check if the username field is empty
            showAlert("Input Error", "Please enter a username.");  // Show error if no username is provided
            return;
        }

        // Show a confirmation dialog before deleting the user
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("Are you sure you want to delete user: " + username + "?");

        // Handle the result of the confirmation dialog
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // If the user confirms, proceed with deletion
                deleteUserFromDatabase(username);
            } else {
                // If the user cancels, show a message and do nothing
                showAlert("Cancelled", "User deletion cancelled.");
            }
        });
    }

    /**
     * Deletes the user from the database based on the provided username.
     */
    private void deleteUserFromDatabase(String username) {
        String deleteQuery = "DELETE FROM Users WHERE username = ?";  // SQL query for deleting a user

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, username);  // Set the username parameter in the query

            int rowsAffected = stmt.executeUpdate();  // Execute the query and get the number of affected rows
            if (rowsAffected > 0) {
                showAlert("Success", "User deleted successfully.");  // Show success message if deletion was successful
            } else {
                showAlert("Error", "User not found.");  // Show error if the user was not found in the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while deleting the user.");  // Show error if a database issue occurred
        }
    }

    /**
     * Displays an alert dialog with the specified title and content.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  // Create an information alert
        alert.setTitle(title);  // Set the alert's title
        alert.setContentText(content);  // Set the alert's content message
        alert.showAndWait();  // Display the alert and wait for the user's response
    }
}
