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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteUserPage {

    private Stage primaryStage;

    public DeleteUserPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox(10);  // VBox layout for vertical alignment with 10px spacing
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Label titleLabel = new Label("Delete User");
        titleLabel.setStyle("-fx-font-size: 24px;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username to Delete");

        Button deleteButton = new Button("Delete");

        // Set action for the delete button
        deleteButton.setOnAction(e -> handleDeleteUser(usernameField.getText()));

        // Add back button to return to Admin Dashboard
        Button backButton = new Button("Back to Admin Dashboard");
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        // Add components to layout
        layout.getChildren().addAll(titleLabel, usernameField, deleteButton, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Delete User");
        primaryStage.show();
    }

    private void handleDeleteUser(String username) {
        if (username.isEmpty()) {
            showAlert("Input Error", "Please enter a username.");
            return;
        }

        // Show a confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("Are you sure you want to delete user: " + username + "?");

        // Handle the result of the confirmation dialog
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // If confirmed, delete the user from the database
                deleteUserFromDatabase(username);
            } else {
                // If Cancelled, do nothing
                showAlert("Cancelled", "User deletion cancelled.");
            }
        });
    }

    private void deleteUserFromDatabase(String username) {
        String deleteQuery = "DELETE FROM Users WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "User deleted successfully.");
            } else {
                showAlert("Error", "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while deleting the user.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}