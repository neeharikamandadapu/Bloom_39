package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <p> AdminDashboard Class </p>
 * 
 * <p> Description: This class defines the Admin Dashboard page in the JavaFX application.
 * It serves as the central hub for administrators, providing them with various functionalities 
 * such as inviting users, resetting users, deleting users, adding/removing roles, listing all users, and logging out. 
 * </p>
 * 
 * <p> The dashboard displays buttons for each function, and clicking a button will either navigate to a new 
 * page (such as Invite User, Delete User, Add/Remove Role, or List Users), or execute an immediate action 
 * (such as resetting a user or logging out). </p>
 * 
 * <p> The main layout is a VBox that is centered, with padding and style applied to give the dashboard 
 * a clean appearance. Each button has its own event handler that determines what happens when the button is clicked. </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00 2024-10-09 Initial implementation of the Admin Dashboard page
 * 
 */
public class AdminDashboard {

    private Stage primaryStage;  // Reference to the primary stage of the application

    /**
     * Constructor to initialize the AdminDashboard with the given stage.
     */
    public AdminDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * This method sets up the Admin Dashboard layout and displays it.
     * It includes buttons for various admin functionalities and sets their respective actions.
     */
    public void show() {
        // Create a VBox layout with spacing, padding, and center alignment
        VBox adminLayout = new VBox(10);
        adminLayout.setPadding(new Insets(20));
        adminLayout.setAlignment(Pos.CENTER);
        adminLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Add a label for the dashboard title
        Label appNameLabel = new Label("Admin Dashboard");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        // Button to invite a new user
        Button inviteUserButton = new Button("Invite User");
        inviteUserButton.setOnAction(e -> {
            InviteUserPage inviteUserPage = new InviteUserPage(primaryStage);
            inviteUserPage.show();
        });

        // Button to reset a user (not yet implemented)
        Button resetUserButton = new Button("Reset User");
        resetUserButton.setOnAction(e -> System.out.println("Reset User Pressed"));

        // Button to delete a user
        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setOnAction(e -> {
            DeleteUserPage deleteUserPage = new DeleteUserPage(primaryStage);
            deleteUserPage.show();
        });

        // Button to add or remove a user's role
        Button addRemoveRoleButton = new Button("Add/Remove Role");
        addRemoveRoleButton.setOnAction(e -> {
            AddRemoveRolePage addRemoveRolePage = new AddRemoveRolePage(primaryStage);
            addRemoveRolePage.show();
        });

        // Button to list all users
        Button listUsersButton = new Button("List all users");
        listUsersButton.setOnAction(e -> {
            UserListPage userListPage = new UserListPage(primaryStage);
            userListPage.show();
        });

        // Button to log out of the admin dashboard
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new LoginPage(primaryStage).show());

        // Add all components to the layout
        adminLayout.getChildren().addAll(appNameLabel, inviteUserButton, resetUserButton, deleteUserButton, addRemoveRoleButton, listUsersButton, logoutButton);

        // Set the scene and display the dashboard
        Scene adminScene = new Scene(adminLayout, 600, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }
}
