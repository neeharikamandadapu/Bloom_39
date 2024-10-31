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

public class ArticleDetailPage {

    private Stage primaryStage;
    private HelpArticle article;
    private String role; // Declare role variable

    public ArticleDetailPage(Stage primaryStage, HelpArticle article) {
        this.primaryStage = primaryStage;
        this.article = article;
        this.role = role; // Initialize role variable
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Create title label with the level in brackets
        Label titleLabel = new Label(article.getTitle() + " (" + article.getLevel() + ")");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Group label
        Label groupLabel = new Label("Group: " + article.getGroup());
        groupLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Body area
        TextArea contentArea = new TextArea();
        contentArea.setText(article.getBody());
        contentArea.setWrapText(true);
        contentArea.setEditable(false);
        contentArea.setPrefHeight(200);
        contentArea.setStyle("-fx-font-size: 14px;");

        // Links label
        Label linksLabel = new Label("Links: " + article.getLinks());
        linksLabel.setStyle("-fx-font-size: 16px;");

        // Back button to return to the articles list
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new ViewHelpArticlesPage(primaryStage, role).show(); // Pass role back to ViewHelpArticlesPage
        });

        // Update button
        Button updateButton = new Button("Update Article");
        updateButton.setOnAction(e -> updateArticle());

        // Delete button
        Button deleteButton = new Button("Delete Article");
        deleteButton.setOnAction(e -> deleteArticle(article.getId()));

        // Add all elements to the layout
        layout.getChildren().addAll(titleLabel, groupLabel, contentArea, linksLabel, backButton, updateButton, deleteButton);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Article Details");
        primaryStage.show();
    }

    private void updateArticle() {
        // Create an update dialog to edit article details
        Dialog<HelpArticle> dialog = new Dialog<>();
        dialog.setTitle("Update Article");

        // Set up the dialog's layout
        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Fields for updating article details
        TextField titleField = new TextField(article.getTitle());
        titleField.setPromptText("Title");

        TextField groupField = new TextField(article.getGroup());
        groupField.setPromptText("Group Name");

        TextArea bodyField = new TextArea(article.getBody());
        bodyField.setWrapText(true);
        bodyField.setPromptText("Body");

        TextField linksField = new TextField(article.getLinks());
        linksField.setPromptText("Links (comma-separated)");

        TextField levelField = new TextField(article.getLevel());
        levelField.setPromptText("Level (e.g., beginner, intermediate)");

        TextArea descriptionField = new TextArea(article.getDescription());
        descriptionField.setWrapText(true);
        descriptionField.setPromptText("Description");

        // Keywords field for updating
        TextField keywordsField = new TextField(article.getKeywords());
        keywordsField.setPromptText("Keywords");

        // Add fields to the dialog layout
        dialogLayout.getChildren().addAll(new Label("Title:"), titleField,
                new Label("Group:"), groupField,
                new Label("Level:"), levelField,
                new Label("Description:"), descriptionField,
                new Label("Body:"), bodyField,
                new Label("Links:"), linksField,
                new Label("Keywords:"), keywordsField);

        dialog.getDialogPane().setContent(dialogLayout);

        // Add OK and Cancel buttons
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Handle button actions
        dialog.setResultConverter(button -> {
            if (button == updateButtonType) {
                // Update article in database
                String newTitle = titleField.getText();
                String newGroup = groupField.getText();
                String newLevel = levelField.getText();
                String newDescription = descriptionField.getText();
                String newBody = bodyField.getText();
                String newLinks = linksField.getText();
                String newKeywords = keywordsField.getText();

                // Update by matching id
                updateArticleInDatabase(article.getId(), newTitle, newGroup, newLevel, newDescription, newBody, newLinks, newKeywords);
                showAlert("Success", "Article updated successfully.");
                return new HelpArticle(newTitle, newDescription, newKeywords, newGroup, newBody, newLevel, newLinks, article.getId());
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Method to mark an article as deleted
    public void deleteArticle(long id) {
        // Call the method to mark the article as deleted
        if (showConfirmationDialog("Delete Article", "Are you sure you want to delete this article?")) {
            markArticleAsDeleted(id);
            showAlert("Success", "Article marked as deleted successfully.");
            
            // Go back to ViewHelpArticlesPage after deletion
            new ViewHelpArticlesPage(primaryStage, role).show(); // Pass role back after deletion
        }
    }

    private void markArticleAsDeleted(long id) {
        String sql = "UPDATE HelpArticles SET deleted = 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateArticleInDatabase(long id, String newTitle, String group, String level, String description, String body, String links, String keywords) {
        // SQL logic to update the article
        String updateSQL = "UPDATE HelpArticles SET title = ?, group_name = ?, level = ?, description = ?, body = ?, links = ?, keywords = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, group);
            pstmt.setString(3, level);
            pstmt.setString(4, description);
            pstmt.setString(5, body);
            pstmt.setString(6, links);
            pstmt.setString(7, keywords);
            pstmt.setLong(8, id);
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

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }
}