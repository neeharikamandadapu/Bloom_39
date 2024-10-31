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

    private Stage primaryStage;


    public AdminDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
 
    }

    public void show() {
        VBox adminLayout = new VBox(10);
        adminLayout.setPadding(new Insets(20));
        adminLayout.setAlignment(Pos.CENTER);
        adminLayout.setStyle("-fx-background-color: #FFFFFF;");

        Label appNameLabel = new Label("Admin Dashboard");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        Button inviteUserButton = new Button("Invite User");
        inviteUserButton.setOnAction(e -> {
            InviteUserPage inviteUserPage = new InviteUserPage(primaryStage);
            inviteUserPage.show();
        });

        Button resetUserButton = new Button("Reset User");
        resetUserButton.setOnAction(e -> {
            ResetPasswordPage resetPasswordPage = new ResetPasswordPage(primaryStage);
            resetPasswordPage.show();
        });

        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setOnAction(e -> {
            DeleteUserPage deleteUserPage = new DeleteUserPage(primaryStage);
            deleteUserPage.show();
        });

        Button addRemoveRoleButton = new Button("Add/Remove Role");
        addRemoveRoleButton.setOnAction(e -> {
            AddRemoveRolePage addRemoveRolePage = new AddRemoveRolePage(primaryStage);
            addRemoveRolePage.show();
        });

        Button listUsersButton = new Button("List all users");
        listUsersButton.setOnAction(e -> {
            UserListPage userListPage = new UserListPage(primaryStage);
            userListPage.show();
        });

        Button createHelpArticleButton = new Button("Create Help Article");
        createHelpArticleButton.setOnAction(e -> {
            // Pass the role to the CreateHelpArticlePage
            CreateHelpArticlePage createHelpArticlePage = new CreateHelpArticlePage(primaryStage, "Admin"); // userRole should be defined in your context
            createHelpArticlePage.show();
        });
        // New button to list help articles
        Button viewHelpArticlesButton = new Button("View Help Articles");
        viewHelpArticlesButton.setOnAction(e -> {
            ViewHelpArticlesPage viewHelpArticlesPage = new ViewHelpArticlesPage(primaryStage, "Admin"); // Pass "Admin" as the role
            viewHelpArticlesPage.show();
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new LoginPage(primaryStage).show());

        // Adding all buttons to the layout
        adminLayout.getChildren().addAll(
                appNameLabel,
                inviteUserButton,
                resetUserButton,
                deleteUserButton,
                addRemoveRoleButton,
                listUsersButton,
                createHelpArticleButton,
                viewHelpArticlesButton,
                logoutButton
        );

        Scene adminScene = new Scene(adminLayout, 600, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }
}