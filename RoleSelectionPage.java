package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <p> RoleSelectionPage Class </p>
 * 
 * <p> Description: This class represents the role selection page where users can choose a role such as Admin, Student, or Instructor.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for role selection functionality in JavaFX
 */

public class RoleSelectionPage {

    // The primary stage that holds the UI for the role selection page
    private Stage primaryStage;

    /**
     * Constructor that initializes the primary stage of the application.
     * 
     * @param primaryStage The primary stage used to display the role selection page.
     */
    public RoleSelectionPage(Stage primaryStage) {
        // Set the primary stage
        this.primaryStage = primaryStage;
    }

    /**
     * Displays the Role Selection page where users can select a role.
     */
    public void show() {
        // VBox layout with 10px spacing between elements
        VBox roleLayout = new VBox(10);

        // Padding of 20px around the layout
        roleLayout.setPadding(new Insets(20));

        // Center all elements within the layout
        roleLayout.setAlignment(Pos.CENTER);

        // Set background color to white
        roleLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Label for the application name
        Label appNameLabel = new Label("BLOOM");

        // Set font size for the application name
        appNameLabel.setStyle("-fx-font-size: 24px;");

        // ComboBox for selecting a role
        ComboBox<String> roleDropdown = new ComboBox<>();

        // Add role options to the ComboBox
        roleDropdown.getItems().addAll("Admin", "Student", "Instructor");

        // Set default role selection to "Student"
        roleDropdown.setValue("Student");

        // Button to confirm the role selection
        Button selectRoleButton = new Button("Select Role");

        // Set event handler for the button
        selectRoleButton.setOnAction(e -> handleRoleSelection(roleDropdown.getValue()));

        // Add all UI elements to the layout
        roleLayout.getChildren().addAll(appNameLabel, roleDropdown, selectRoleButton);

        // Create a scene with a size of 600x400 pixels
        Scene roleScene = new Scene(roleLayout, 600, 400);

        // Set the scene on the primary stage
        primaryStage.setScene(roleScene);

        // Set the title of the stage
        primaryStage.setTitle("Role Selection");

        // Show the stage
        primaryStage.show();
    }

    /**
     * Handles the role selection and navigates to the appropriate dashboard.
     * 
     * @param role The role selected by the user.
     */
    private void handleRoleSelection(String role) {
        // Check if the selected role is "Admin" and redirect to the Admin Dashboard
        if (role.equals("Admin")) {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        } else {
            // For other roles, redirect to the User Dashboard
            UserDashboard userDashboard = new UserDashboard(primaryStage, role);
            userDashboard.show();
        }
    }
}
