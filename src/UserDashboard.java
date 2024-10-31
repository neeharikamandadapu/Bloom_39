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
 * <p> Copyright: Group 39 Â© 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for the user dashboard functionality in JavaFX
 */

public class UserDashboard {

    private Stage primaryStage;

    public UserDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox userLayout = new VBox(10);
        userLayout.setPadding(new Insets(20));
        userLayout.setAlignment(Pos.CENTER);
        userLayout.setStyle("-fx-background-color: #FFFFFF;");

        Label appNameLabel = new Label("Instructor Dashboard");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new LoginPage(primaryStage).show());

        // Buttons for instructors to create and view articles
        Button createArticleButton = new Button("Create Help Article");
        createArticleButton.setOnAction(e -> {
            CreateHelpArticlePage createHelpArticlePage = new CreateHelpArticlePage(primaryStage, "Instrcutor"); // Remove role parameter
            createHelpArticlePage.show();
        });

        Button viewArticlesButton = new Button("View Help Articles");
        viewArticlesButton.setOnAction(e -> {
            ViewHelpArticlesPage viewHelpArticlesPage = new ViewHelpArticlesPage(primaryStage, "Instrcutor"); // Remove role parameter
            viewHelpArticlesPage.show();
        });

        Button restoreButton = new Button("Restore Articles");
        restoreButton.setOnAction(e -> {
            RestoreArticlesPage restoreArticlesPage = new RestoreArticlesPage(primaryStage,  "Instrcutor");
            Scene scene = new Scene(restoreArticlesPage, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Restore Articles");
            primaryStage.show();
        });

        userLayout.getChildren().addAll(appNameLabel, createArticleButton, viewArticlesButton, restoreButton, logoutButton);

        Scene userScene = new Scene(userLayout, 600, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("Instructor Dashboard");
        primaryStage.show();
    }
}