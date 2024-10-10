package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <p> UserDashboard Class </p>
 * 
 * <p> Description: This class represents the user dashboard page for users with different roles such as Admin, Student, or Instructor.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for the user dashboard functionality in JavaFX
 */

public class UserDashboard {

    // The primary stage that holds the UI for the user dashboard
    private Stage primaryStage;

    // Stores the role of the user (e.g., Admin, Student, Instructor)
    private String role;

    /**
     * Constructor that initializes the user dashboard with the stage and the user's role.
     * 
     * @param primaryStage The primary stage used to display the dashboard.
     * @param role The role of the user (Admin, Student, or Instructor).
     */
    public UserDashboard(Stage primaryStage, String role) {
        // Set the primary stage
        this.primaryStage = primaryStage;

        // Set the role of the user
        this.role = role;
    }

    /**
     * Displays the User Dashboard page where users can see their dashboard and log out.
     */
    public void show() {
        // Create a VBox layout with 10px spacing between elements
        VBox userLayout = new VBox(10);

        // Add padding of 20px around the layout
        userLayout.setPadding(new Insets(20));

        // Center align all elements within the layout
        userLayout.setAlignment(Pos.CENTER);

        // Set the background color to white
        userLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Create a label for the dashboard, which includes the user's role in the label text
        Label appNameLabel = new Label(role + " User Dashboard");

        // Set the font size for the dashboard label
        appNameLabel.setStyle("-fx-font-size: 24px;");

        // Create a button for logging out
        Button logoutButton = new Button("Logout");

        // Set the action for the logout button to redirect the user back to the login page
        logoutButton.setOnAction(e -> new LoginPage(primaryStage).show());

        // Add the dashboard label and logout button to the layout
        userLayout.getChildren().addAll(appNameLabel, logoutButton);

        // Create a new scene with the layout and set it on the primary stage
        Scene userScene = new Scene(userLayout, 600, 400);

        // Set the scene on the primary stage
        primaryStage.setScene(userScene);

        // Set the title of the primary stage, including the user's role in the title
        primaryStage.setTitle(role + " Dashboard");

        // Show the primary stage
        primaryStage.show();
    }
}
