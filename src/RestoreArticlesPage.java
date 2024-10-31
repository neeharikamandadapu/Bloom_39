package asuHelloWorldJavaFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestoreArticlesPage extends VBox {

    private TableView<HelpArticle> articleTable;
    private TextField searchField;
    private Button restoreButton;
    private Button backButton; // Back button
    private String role; // Store user role

    public RestoreArticlesPage(Stage primaryStage, String role) {
        this.role = role; // Store the role passed to the constructor

        Label title = new Label("Restore Articles");
        searchField = new TextField();
        searchField.setPromptText("Enter the title of the article to restore...");

        articleTable = new TableView<>();
        setupTable();

        restoreButton = new Button("Restore Article");
        restoreButton.setOnAction(e -> restoreSelectedArticle());

        // Back button to return to the articles list
        backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if ("Admin".equals(role)) { // Check if the role is admin
                new AdminDashboard(primaryStage).show(); // Show Admin Dashboard
            } else {
                new UserDashboard(primaryStage).show(); // Show User Dashboard with role
            }
        });

        this.getChildren().addAll(title, searchField, articleTable, restoreButton, backButton);

        // Add a listener to the search field
        searchField.setOnAction(e -> searchForArticle());
    }

    private void setupTable() {
        TableColumn<HelpArticle, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<HelpArticle, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        articleTable.getColumns().addAll(idColumn, titleColumn);
    }

    private void searchForArticle() {
        String titleQuery = searchField.getText().trim();
        if (!titleQuery.isEmpty()) {
            loadDeletedArticles(titleQuery);
        } else {
            // Optionally, clear the table if no title is provided
            articleTable.getItems().clear();
        }
    }

    private void loadDeletedArticles(String titleQuery) {
        // SQL query to select deleted articles that match the given title
        String sql = "SELECT * FROM HelpArticles WHERE deleted = 1 AND title = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the title query parameter
            pstmt.setString(1, titleQuery);
            
            // Execute the query and obtain the result set
            ResultSet rs = pstmt.executeQuery();
            
            // Create a list to hold the retrieved articles
            ObservableList<HelpArticle> articles = FXCollections.observableArrayList();
            
            // Loop through the result set
            while (rs.next()) {
                // Retrieve values from the current row
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String level = rs.getString("level");
                String description = rs.getString("description");
                String body = rs.getString("body");
                String links = rs.getString("links");
                String groupName = rs.getString("group_name");
                String keywords = rs.getString("keywords");
                // Create a new HelpArticle instance with the retrieved values
                articles.add(new HelpArticle(title, description, keywords, groupName, body, level, links, id));
            }
            
            // Set the items in the articleTable to the list of articles
            articleTable.setItems(articles);
            
            // Show an alert if no articles were found
            if (articles.isEmpty()) {
                showAlert("No articles found with the given title.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void restoreSelectedArticle() {
        HelpArticle selectedArticle = articleTable.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            String sql = "UPDATE HelpArticles SET deleted = 0 WHERE id = ?";
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, selectedArticle.getId());
                pstmt.executeUpdate();
                showAlert("Article restored successfully.");
                articleTable.getItems().clear(); // Clear the table after restoring
                searchField.clear(); // Clear the search field
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select an article to restore.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}