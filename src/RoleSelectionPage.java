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

    private Stage primaryStage;

    public RoleSelectionPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox roleLayout = new VBox(10);
        roleLayout.setPadding(new Insets(20));
        roleLayout.setAlignment(Pos.CENTER);
        roleLayout.setStyle("-fx-background-color: #FFFFFF;");

        Label appNameLabel = new Label("BLOOM");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Admin", "Student", "Instructor");
        roleDropdown.setValue("Student");

        Button selectRoleButton = new Button("Select Role");
        selectRoleButton.setOnAction(e -> handleRoleSelection(roleDropdown.getValue()));

        roleLayout.getChildren().addAll(appNameLabel, roleDropdown, selectRoleButton);

        Scene roleScene = new Scene(roleLayout, 600, 400);
        primaryStage.setScene(roleScene);
        primaryStage.setTitle("Role Selection");
        primaryStage.show();
    }

    private void handleRoleSelection(String role) {
        if (role.equals("Admin")) {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        } else {
            UserDashboard userDashboard = new UserDashboard(primaryStage);
            userDashboard.show();
        }
    }
}