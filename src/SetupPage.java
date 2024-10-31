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

/**
 * <p> SetupPage Class </p>
 * 
 * <p> Description: This class represents the account setup page for users where they can fill in personal information and complete their setup process.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for user setup functionality in JavaFX
 */

public class SetupPage {

    private Stage primaryStage;
    private String username; // Store the username for the setup process
    private String email; // Store the email from the invitation
    private String loginPassword; // Store the password from LoginPage for confirmation
    private String role; // Store the user role from the invitation
    private boolean isAdmin; // Track if the user is an admin

    // Constructor now requires primaryStage, username, loginPassword, and isAdmin
    public SetupPage(Stage primaryStage, String username, String loginPassword, String invitationCode) {
        this.primaryStage = primaryStage;
        this.username = username; // Receive the username from the previous page
        this.loginPassword = loginPassword; // Store the password from the login page

        // Check if the invitation code is provided
        if (invitationCode == null || invitationCode.isEmpty()) {
            this.role = "ADMIN"; // Set role to ADMIN if no invitation code
            this.email = null; // You can set it to null or handle it as per your logic
        } else {
            String[] invitationDetails = fetchEmailAndRoleFromInvitation(invitationCode); // Fetch email and role from the invitation
            this.email = invitationDetails[0]; // First element is email
            this.role = invitationDetails[1]; // Second element is role
        }

        this.isAdmin = (invitationCode == null || invitationCode.isEmpty()); // Check if activation code is absent
    }




    public void show() {
        VBox setupLayout = new VBox(10);
        setupLayout.setPadding(new Insets(20));
        setupLayout.setAlignment(Pos.CENTER);
        setupLayout.setStyle("-fx-background-color: #FFFFFF;");

        Label appNameLabel = new Label("BLOOM");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name (Required)");

        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Middle Name (Optional)");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name (Required)");

        TextField preferredNameField = new TextField();
        preferredNameField.setPromptText("Preferred Name (Optional)");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password (Required)");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");

        // Only set the email field if the user is not an admin
        if (!isAdmin) {
            emailField.setText(email); // Pre-fill email field with the fetched email
        }

        Button finishSetupButton = new Button("Finish Setup");
        finishSetupButton.setOnAction(e -> {
            // Collect user information
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String middleName = middleNameField.getText(); // Optional
            String preferredName = preferredNameField.getText(); // Optional
            String confirmPassword = confirmPasswordField.getText();

            // Ensure all required fields are filled and passwords match
            if (!firstName.isEmpty() && !lastName.isEmpty() && !confirmPassword.isEmpty()) {
                if (confirmPassword.equals(loginPassword)) {
                    // Determine the role based on invitation usage
                    String assignedRole = isAdmin ? "Admin" : role;

                    // Insert the user info into the database
                    if (insertUserIntoDatabase(username, loginPassword, firstName, lastName, middleName, preferredName, emailField.getText(), assignedRole, isAdmin)) {
                        // Mark the invitation as used
                        markUsedAfterActivation(username); // Pass the username to mark the invitation as used

                        // Redirect based on admin status
                        if (isAdmin) {
                            // Redirect to Admin Dashboard
                            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
                            adminDashboard.show();
                        } else {
                            // Redirect to Role Selection Page after successful update
                            RoleSelectionPage roleSelectionPage = new RoleSelectionPage(primaryStage);
                            roleSelectionPage.show();
                        }
                    } else {
                        showAlert("Setup Error", "Could not complete the setup.", "Please try again.");
                    }
                } else {
                    showAlert("Password Error", "Passwords do not match.", "Please make sure the confirmed password matches the entered password.");
                }
            } else {
                showAlert("Input Error", "Please fill in all required fields.", "All required fields must be filled.");
            }
        });

        setupLayout.getChildren().addAll(appNameLabel, firstNameField, lastNameField, middleNameField, preferredNameField, confirmPasswordField, emailField, finishSetupButton);

        Scene setupScene = new Scene(setupLayout, 600, 400);
        primaryStage.setScene(setupScene);
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }

    private String[] fetchEmailAndRoleFromInvitation(String invitationCode) {
        String[] details = new String[2]; // Array to hold email and role
        String query = "SELECT email, role FROM Invitations WHERE invitation_code = ?";

        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, invitationCode);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                details[0] = rs.getString("email"); // Email
                details[1] = rs.getString("role");  // Role
            } else {
                // Handle case where no results are found
                details[0] = null; 
                details[1] = null; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return details; // Return the array with fetched details
    }


    private boolean insertUserIntoDatabase(String username, String password, String firstName, String lastName, String middleName, String preferredName, String email, String role, boolean isAdmin) {
        String insertUserSQL = "INSERT INTO Users (username, password, first_name, last_name, middle_name, preferred_name, email, role, is_admin, is_set_up) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(insertUserSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, middleName);
            stmt.setString(6, preferredName);
            stmt.setString(7, email);
            stmt.setString(8, role); // Set the role in the prepared statement
            stmt.setBoolean(9, isAdmin); // Set is_admin in the prepared statement
            stmt.executeUpdate();
            return true; // Successful insertion
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Insertion failed
        }
    }

    private void markUsedAfterActivation(String username) {
        String updateInvitationSQL = "UPDATE Invitations SET is_used = 1 WHERE email = ?"; // Ensure to fetch the correct invitation based on email or other unique identifier
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(updateInvitationSQL)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserAdmin() {
        // Implement logic to check if the user is an admin
        // For demonstration purposes, returning the actual admin status
        return isAdmin; 
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}