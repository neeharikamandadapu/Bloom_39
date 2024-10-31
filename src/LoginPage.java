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

    private Stage primaryStage;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #FFFFFF;");

        Label appNameLabel = new Label("BLOOM");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField adminKeyField = new TextField();
        adminKeyField.setPromptText("Admin Invitation Key (Optional)");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), adminKeyField.getText()));

        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setOnAction(e -> System.out.println("Forgot Password Pressed"));

        loginLayout.getChildren().addAll(appNameLabel, usernameField, passwordField, adminKeyField, loginButton, forgotPasswordButton);

        Scene loginScene = new Scene(loginLayout, 600, 400);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    private void handleLogin(String username, String password, String adminKey) {
        // Check if the password field is empty
        if (password.isEmpty()) {
            showAlert("Login Error", "Password Required", "Please enter your password to proceed.");
            return; // Exit the method early if the password is empty
        }

        String query = "SELECT * FROM Users WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User found, handle login as before
                String storedPassword = rs.getString("password");

                // Compare the stored password with the entered password
                if (storedPassword.equals(password)) {
                    boolean isAdmin = rs.getBoolean("is_admin");
                    boolean isSetUp = rs.getBoolean("is_set_up");
                    String userName = rs.getString("username");

                    if (!isSetUp) {
                        // Redirect to Setup Page
                        SetupPage setupPage = new SetupPage(primaryStage, userName, password, adminKey); // Pass password to SetupPage
                        setupPage.show();
                    } else if (isAdmin) {
                        // Redirect to Admin Dashboard
                        AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
                        adminDashboard.show();
                    } else {
                        // Redirect to Role Selection if multiple roles
                        RoleSelectionPage roleSelectionPage = new RoleSelectionPage(primaryStage);
                        roleSelectionPage.show();
                    }
                } else {
                    showAlert("Login Error", "Incorrect Password", "Please check your credentials.");
                }
            } else {
                if (!adminKey.isEmpty()) {
                    handleInvitation(adminKey, username, password, conn); // Pass password here
                } else {
                    showAlert("Login Error", "Username Not Found", "Please enter a valid Admin Invitation Key.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleInvitation(String adminKey, String username, String password, Connection conn) {
        String invitationQuery = "SELECT * FROM Invitations WHERE invitation_code = ? AND is_used = 0";

        try (PreparedStatement stmt = conn.prepareStatement(invitationQuery)) {
            stmt.setString(1, adminKey);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Invitation found, retrieve the user's role and email
                String role = rs.getString("role");
                String email = rs.getString("email");

                // Redirect to Account Setup page with the retrieved role and email
                SetupPage setupPage = new SetupPage(primaryStage, username, password, adminKey); // Pass password to SetupPage
                setupPage.show();
            } else {
                showAlert("Invalid Invitation Key", "The invitation key is either incorrect or has already been used.", "Please try another invitation code.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
