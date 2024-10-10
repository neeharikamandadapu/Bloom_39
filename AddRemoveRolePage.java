package asuHelloWorldJavaFX;

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

/*******
 * <p> AddRemoveRolePage Class </p>
 * 
 * <p> Description: A JavaFX demonstration application and baseline for a sequence of projects </p>
 * 
 * 
 * <p> This class defines a page for adding or removing user roles in a simple JavaFX application.
 * It provides a graphical interface where administrators can enter a username, select a role,
 * and update the role in the database. The class handles user input validation, database
 * interactions for checking user existence, and updating the roles accordingly. It also includes
 * alert dialogs for error handling and feedback to the user.
 * </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00 2024-10-09 inital implementation of add and remove role page 
 */

public class AddRemoveRolePage {

    private Stage primaryStage;

    public AddRemoveRolePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Label titleLabel = new Label("Add/Remove User Role");
        titleLabel.setStyle("-fx-font-size: 24px;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("ADMIN", "STUDENT", "INSTRUCTOR");
        roleComboBox.setPromptText("Select Role");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleRoleUpdate(usernameField.getText(), roleComboBox.getValue()));

        Button backButton = new Button("Back to Admin Menu");
        backButton.setOnAction(e -> navigateToAdminMenu());

        layout.getChildren().addAll(titleLabel, usernameField, roleComboBox, submitButton, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add/Remove Role");
        primaryStage.show();
    }

    private void handleRoleUpdate(String username, String role) {
        if (username.isEmpty() || role == null) {
            showAlert("Input Error", "Please enter a username and select a role.");
            return;
        }

        if (isUsernameExists(username)) {
            updateRoleInDatabase(username, role);
        } else {
            showAlert("Username Not Found", "No user found with the username: " + username);
        }
    }

    private boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Check if count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if there was an error
    }

    private void updateRoleInDatabase(String username, String role) {
        String query = "UPDATE Users SET role = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showAlert("Success", "Role updated successfully for user: " + username);
            } else {
                showAlert("Error", "Failed to update role for user: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while updating the role.");
        }
    }

    private void navigateToAdminMenu() {
        // Assuming you have a class for the Admin Menu Page
        AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
        adminDashboard.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header); // Set the header text
        alert.setContentText(content);
        alert.showAndWait();
    }
}