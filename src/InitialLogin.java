package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <p> InitialLogin Class </p>
 * 
 * <p> Description: This class represents the initial admin account setup page in the BLOOM application.
 * It allows the creation of the first admin account for the system, providing username and password fields
 * and storing the account in the database. </p>
 * 
 * <p> The page includes a company logo, and handles database interactions to store admin details,
 * ensuring proper privileges are granted. It also displays success and error alerts based on
 * the outcome of the account creation process. </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00 2024-10-09 Initial implementation of the initial admin setup functionality.
 */
public class InitialLogin {

    private Stage primaryStage;

    public InitialLogin(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        // Create VBox layout
        VBox initialLoginLayout = new VBox(10);
        initialLoginLayout.setPadding(new Insets(20));
        initialLoginLayout.setAlignment(Pos.CENTER);
        initialLoginLayout.setStyle("-fx-background-color: #FFFFFF;");

        // App name label
        Label appNameLabel = new Label("BLOOM - Admin Account Set up");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        // Load the image
        ImageView logoImageView = loadImage();

        // Create text fields for username and password
        TextField usernameField = new TextField();
        usernameField.setPromptText("Admin Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Admin Password");

        // Create the account button
        Button createAccountButton = new Button("Create Admin Account");
        createAccountButton.setOnAction(e -> handleCreateAccount(usernameField.getText(), passwordField.getText()));
        
        // Back to Dashboard button
//        Button backToDashboardButton = new Button("Back to Dashboard");
//        backToDashboardButton.setOnAction(e -> {
//            // Show the Admin Dashboard
//            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
//            adminDashboard.show();
//        });

        // Add all elements to the layout
        initialLoginLayout.getChildren().addAll(logoImageView, appNameLabel, usernameField, passwordField, createAccountButton);

        // Set the scene and show the stage
        Scene initialLoginScene = new Scene(initialLoginLayout, 600, 400);
        primaryStage.setScene(initialLoginScene);
        primaryStage.setTitle("Initial Admin Setup");
        primaryStage.show();
    }
    private ImageView loadImage() {
        // Load image from resources
        InputStream imageStream = getClass().getResourceAsStream("/Resources/Bloom_logo.png");

        // Debugging check
        if (imageStream == null) {
            System.out.println("Image not found!");
            return new ImageView(); // return an empty ImageView if image is not found
        } else {
            System.out.println("Image loaded successfully!");
        }

        // Create the Image and ImageView
        Image logoImage = new Image(imageStream);
        ImageView logoImageView = new ImageView(logoImage);

        // Set the image size (optional)
        logoImageView.setFitWidth(300); // width of the image
        logoImageView.setPreserveRatio(true); // maintain aspect ratio

        return logoImageView;
    }

    private void handleCreateAccount(String username, String password) {
        String query = "INSERT INTO Users (username, password, is_admin, is_set_up, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, true); // Set as admin
            stmt.setBoolean(4, false);// Mark as set up
            stmt.setString(5, "ADMIN");
            

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Admin Account Created", "You can now log in with the admin account.");
                // Redirect to the original login page
                LoginPage loginPage = new LoginPage(primaryStage);
                loginPage.show();
            } else {
                showAlert("Error", "Account Creation Failed", "Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while creating the account.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}