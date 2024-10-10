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

    private Stage primaryStage;  // Field for the primary stage

    /**
     * Constructor that sets the primary stage for this page.
     */
    public InitialLogin(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Displays the initial admin setup page with input fields and buttons.
     * This method sets up the UI layout for creating the first admin account.
     */
    public void show() {
        // Create a VBox layout for vertical alignment with 10px spacing
        VBox initialLoginLayout = new VBox(10);
        initialLoginLayout.setPadding(new Insets(20));  // Add padding around the layout
        initialLoginLayout.setAlignment(Pos.CENTER);  // Center all elements
        initialLoginLayout.setStyle("-fx-background-color: #FFFFFF;");  // Set background color

        // Label for the app name
        Label appNameLabel = new Label("BLOOM - Admin Account Set up");
        appNameLabel.setStyle("-fx-font-size: 24px;");  // Set font size for the title

        // Load and display the company logo
        ImageView logoImageView = loadImage();

        // Text fields for entering the admin username and password
        TextField usernameField = new TextField();
        usernameField.setPromptText("Admin Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Admin Password");

        // Button to create the admin account
        Button createAccountButton = new Button("Create Admin Account");
        createAccountButton.setOnAction(e -> handleCreateAccount(usernameField.getText(), passwordField.getText()));

        // Add all components to the layout
        initialLoginLayout.getChildren().addAll(logoImageView, appNameLabel, usernameField, passwordField, createAccountButton);

        // Set up the scene and display it
        Scene initialLoginScene = new Scene(initialLoginLayout, 600, 400);
        primaryStage.setScene(initialLoginScene);
        primaryStage.setTitle("Initial Admin Setup");
        primaryStage.show();
    }

    /**
     * Loads the company logo from the resources folder and returns an ImageView.
     * 
     * @return ImageView containing the logo image.
     */
    private ImageView loadImage() {
        // Load the image from the resources folder
        InputStream imageStream = getClass().getResourceAsStream("/Resources/Bloom_logo.png");

        // Debugging check to ensure the image is found
        if (imageStream == null) {
            System.out.println("Image not found!");
            return new ImageView();  // Return an empty ImageView if the image is not found
        } else {
            System.out.println("Image loaded successfully!");
        }

        // Create the Image and ImageView
        Image logoImage = new Image(imageStream);
        ImageView logoImageView = new ImageView(logoImage);

        // Set the image size (optional)
        logoImageView.setFitWidth(300);  // Set the width of the image
        logoImageView.setPreserveRatio(true);  // Maintain the image's aspect ratio

        return logoImageView;
    }

    /**
     * Handles the process of creating the admin account by storing it in the database.
     */
    private void handleCreateAccount(String username, String password) {
        String query = "INSERT INTO Users (username, password, is_admin, is_set_up, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, true);  // Set the user as admin
            stmt.setBoolean(4, false); // Mark the account as not set up
            stmt.setString(5, "ADMIN"); // Set the role as ADMIN

            // Execute the query and check if the account was created
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Show success alert and redirect to the login page
                showAlert("Success", "Admin Account Created", "You can now log in with the admin account.");
                LoginPage loginPage = new LoginPage(primaryStage);  // Navigate to the login page
                loginPage.show();
            } else {
                // Show error alert if account creation failed
                showAlert("Error", "Account Creation Failed", "Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while creating the account.");  // Show database error alert
        }
    }

    /**
     * Displays an alert dialog with the specified title, header, and content.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  // Create an information alert
        alert.setTitle(title);  // Set the alert's title
        alert.setHeaderText(header);  // Set the alert's header
        alert.setContentText(content);  // Set the alert's content message
        alert.showAndWait();  // Display the alert and wait for the user's response
    }
}
s