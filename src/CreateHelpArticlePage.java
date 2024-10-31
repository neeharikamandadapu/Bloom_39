package asuHelloWorldJavaFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateHelpArticlePage {
    private Stage primaryStage;
    private String role; // Add role variable

    public CreateHelpArticlePage(Stage primaryStage, String role) {
        this.primaryStage = primaryStage;
        this.role = role; // Store the role
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Form fields for creating a help article
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea bodyField = new TextArea();
        bodyField.setPromptText("Help article content");

        TextField keywordsField = new TextField();
        keywordsField.setPromptText("Keywords (comma-separated)");

        TextField groupField = new TextField();
        groupField.setPromptText("Group");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField levelField = new TextField();
        levelField.setPromptText("Level (e.g., Beginner, Intermediate, Expert)");

        TextArea linksField = new TextArea();
        linksField.setPromptText("Links (comma-separated)");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String body = bodyField.getText();
            String keywords = keywordsField.getText();
            String group = groupField.getText();
            String description = descriptionField.getText();
            String level = levelField.getText();
            String links = linksField.getText();
            
            // Call a method to save the article to the database
            saveHelpArticle(title, body, keywords, group, description, level, links);
            showAlert("Success", "Help article created successfully.");
            new AdminDashboard(primaryStage).show();
        });

        // Back button to return to the appropriate dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if ("Admin".equals(role)) {
                new AdminDashboard(primaryStage).show();
            } else {
                new UserDashboard(primaryStage).show(); // or create a specific instructor dashboard
            }
        });
        
        layout.getChildren().addAll(
            new Label("Create Help Article"), 
            titleField, 
            bodyField, 
            keywordsField, 
            groupField, 
            descriptionField, 
            levelField, 
            linksField, 
            saveButton, 
            backButton // Add back button here
        );

        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create Help Article");
        primaryStage.show();
    }

    private void saveHelpArticle(String title, String body, String keywords, String group, String description, String level, String links) {
        long randomId = ArticleUtils.generateRandomId();
        String insertSQL = "INSERT INTO HelpArticles (title, body, keywords, group_name, description, level, links, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, body);
            pstmt.setString(3, keywords);
            pstmt.setString(4, group);
            pstmt.setString(5, description);
            pstmt.setString(6, level);
            pstmt.setString(7, links);
            pstmt.setLong(8, randomId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}