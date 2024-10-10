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
 * <p> LoginPage Class </p>
 * 
 * <p> Description: This class represents the login page of the application, where users can log in with their username, password, and optionally an admin key.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for login functionality in JavaFX
 */

public class LoginPage {

    // The primary stage that holds the UI for the login page
    private Stage primaryStage;

    /**
     * Constructor that initializes the primary stage of the application.
     * 
     * @param primaryStage The primary stage used to display the login page.
     */
    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Displays the Login page where users can input their credentials and log in.
     */
    public void show() {
        // Layout and styling for the login page
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Application name label
        Label appNameLabel = new Label("BLOOM");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        // Username text field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Admin key field (optional)
        TextField adminKeyField = new TextField();
        adminKeyField.setPromptText("Admin Invitation Key (Optional)");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), adminKeyField.getText()));

        // Forgot password button
        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setOnAction(e -> System.out.println("Forgot Password Pressed"));

        // Add all UI elements to the layout
        loginLayout.getChildren().addAll(appNameLabel, usernameField, passwordField, adminKeyField, loginButton, forgotPasswordButton);

        // Set up the scene and show it
        Scene loginScene = new Scene(loginLayout, 600, 400);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    /**
     * Handles the login process when the login button is clicked.
     * 
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param adminKey The admin key entered by the user, if any.
     */
    private void handleLogin(String username, String password, String adminKey) {
        // Check if the password field is empty
        if (password.isEmpty()) {
            showAlert("Login Error", "Password Required", "Please enter your password to proceed.");
            return; // Exit the method early if the password is empty
        }

        // Query to select user data from the Users table based on the username
        String query = "SELECT * FROM Users WHERE username = ?";

        // Attempt to connect to the database and execute the query
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the username parameter in the query
            stmt.setString(1, username);
            
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if the user exists
            if (rs.next()) {
                // User found, retrieve the stored password
                String storedPassword = rs.getString("password");

                // Compare the stored password with the entered password
                if (storedPassword.equals(password)) {
                    // Check if the user is an admin and if their account is set up
                    boolean isAdmin = rs.getBoolean("is_admin");
                    boolean isSetUp = rs.getBoolean("is_set_up");
                    String userName = rs.getString("username");

                    // If the account is not set up, redirect to the Setup Page
                    if (!isSetUp) {
                        SetupPage setupPage = new SetupPage(primaryStage, userName, password, adminKey);
                        setupPage.show();
                    } else if (isAdmin) {
                        // If the user is an admin, redirect to the Admin Dashboard
                        AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
                        adminDashboard.show();
                    } else {
                        // If the user is not an admin, redirect to the Role Selection page
                        RoleSelectionPage roleSelectionPage = new RoleSelectionPage(primaryStage);
                        roleSelectionPage.show();
                    }
                } else {
                    // If the password is incorrect, show an error alert
                    showAlert("Login Error", "Incorrect Password", "Please check your credentials.");
                }
            } else {
                // If the user is not found, check for an admin invitation key
                if (!adminKey.isEmpty()) {
                    handleInvitation(adminKey, username, password, conn);
                } else {
                    showAlert("Login Error", "Username Not Found", "Please enter a valid Admin Invitation Key.");
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }

    /**
     * Handles the process of validating and using an admin invitation key.
     * 
     * @param adminKey The admin invitation key entered by the user.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param conn The database connection.
     */
    private void handleInvitation(String adminKey, String username, String password, Connection conn) {
        // Query to check if the invitation exists and has not been used
        String invitationQuery = "SELECT * FROM Invitations WHERE invitation_code = ? AND is_used = 0";

        // Attempt to validate the invitation code
        try (PreparedStatement stmt = conn.prepareStatement(invitationQuery)) {
            // Set the admin key parameter in the query
            stmt.setString(1, adminKey);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if the invitation exists and has not been used
            if (rs.next()) {
                // Invitation found, retrieve the user's role and email
                String role = rs.getString("role");
                String email = rs.getString("email");

                // Redirect to Account Setup page with the retrieved role and email
                SetupPage setupPage = new SetupPage(primaryStage, username, password, adminKey);
                setupPage.show();
            } else {
                // If the invitation code is invalid or already used, show an error alert
                showAlert("Invalid Invitation Key", "The invitation key is either incorrect or has already been used.", "Please try another invitation code.");
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the provided title, header, and content.
     * 
     * @param title   The title of the alert dialog.
     * @param header  The header text of the alert dialog.
     * @param content The content message of the alert dialog.
     */
    private void showAlert(String title, String header, String content) {
        // Create an alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR);
        
        // Set the title, header, and content of the alert
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
}
