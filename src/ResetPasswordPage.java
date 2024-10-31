package asuHelloWorldJavaFX;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResetPasswordPage {

    private Stage primaryStage;

    public ResetPasswordPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        // Layout and styling
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Username text field
        Label usernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Email text field
        Label emailLabel = new Label("Enter Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        // Reset Password button
        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();

            // Validate inputs
            if (username.isEmpty() || email.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
            } else {
                // Delete previous record associated with the username and email
                boolean deleted = UserService.deleteUser(username, email);

                // Create and send invitation code
                if (deleted) {
                    // Use the inviteUser method to generate and send the invitation code
                    String roles = "ROLE"; // Set a default role or get it from user input
                    InvitationCode.inviteUser(username, email, roles);
                    showAlert("Success", "Invitation code sent to " + email + " for user " + username);
                } else {
                    showAlert("Error", "No user found with the provided username and email.");
                }
            }
        });

        // Back button to go back to the Admin Dashboard
        Button backButton = new Button("Back to Admin Dashboard");
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        // Add elements to the layout
        layout.getChildren().addAll(usernameLabel, usernameField, emailLabel, emailField, resetPasswordButton, backButton);

        // Set up the scene and show it
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset Password");
        primaryStage.show();
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class UserService {
    public static boolean deleteUser(String username, String email) {
        String query = "DELETE FROM Users WHERE username = ? AND email = ?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Return true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
            return false;
        }
    }
}

class InvitationFunction {
    // Method to generate a random OTP (invitation code)
    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(999999);  // Generate a 6-digit random number
        return String.format("%06d", otp);
    }

    // Method to insert an invitation with a generated OTP into the Invitations table
    public static void inviteUser(String username, String email, String roles) {
        String otp = generateOTP();  // Generate OTP

        String insertInvitationSQL = "INSERT INTO Invitations (username, email, invitation_code, role, is_used) VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db");
             PreparedStatement pstmt = conn.prepareStatement(insertInvitationSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, otp);
            pstmt.setString(4, roles);  // Storing multiple roles as comma-separated string
            pstmt.executeUpdate();
            System.out.println("Invitation created for " + username + " with OTP: " + otp);

            // Optionally: Send OTP via email to the user here (you can implement your email sending logic)
            sendEmail(email, otp); // Implement this method to handle sending the email
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void sendEmail(String email, String invitationCode) {
        // Implement email sending logic here
        System.out.println("Sending invitation code: " + invitationCode + " to email: " + email);
    }
}