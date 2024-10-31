package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <p> InviteUserPage Class </p>
 * 
 * <p> Description: This class represents the "Invite User" page of the application where users can be invited by entering their username, email, and selecting their roles.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for user invitation functionality in JavaFX
 */
public class InviteUserPage {

    private Stage primaryStage;

    public InviteUserPage(Stage primaryStage) {
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

        // Label for Role checkboxes
        Label roleLabel = new Label("Select Role(s):");

        // Create CheckBoxes for each role
        CheckBox adminCheckBox = new CheckBox("ADMIN");
        CheckBox studentCheckBox = new CheckBox("STUDENT");
        CheckBox instructorCheckBox = new CheckBox("INSTRUCTOR");

        // VBox to hold checkboxes
        VBox roleCheckboxes = new VBox(5);
        roleCheckboxes.getChildren().addAll(adminCheckBox, studentCheckBox, instructorCheckBox);

        // Invite button
        Button inviteButton = new Button("Invite User");
        inviteButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();

            // Collect selected roles
            StringBuilder rolesString = new StringBuilder();
            if (adminCheckBox.isSelected()) rolesString.append("ADMIN,");
            if (studentCheckBox.isSelected()) rolesString.append("STUDENT,");
            if (instructorCheckBox.isSelected()) rolesString.append("INSTRUCTOR,");

            // Remove trailing comma if any roles were selected
            if (rolesString.length() > 0) rolesString.setLength(rolesString.length() - 1);

            // Validate inputs
            if (username.isEmpty() || email.isEmpty() || rolesString.length() == 0) {
                showAlert("Error", "Please fill in all fields and select at least one role.");
            } else {
                // Call InvitationService to generate and store the invitation
                InvitationCode.inviteUser(username, email, rolesString.toString());
                showAlert("Success", "Invitation sent to " + username + " with the role(s): " + rolesString.toString());
            }
        });

        // Back button to go back to the Admin Dashboard
        Button backButton = new Button("Back to Admin Dashboard");
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        // Add elements to the layout
        layout.getChildren().addAll(usernameLabel, usernameField, emailLabel, emailField, roleLabel, roleCheckboxes, inviteButton, backButton);

        // Set up the scene and show it
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Invite User");
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