package asuHelloWorldJavaFX;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewHelpArticlesPage {

    private Stage primaryStage;
    private TableView<HelpArticle> tableView;
    private String role; // Store the role to determine navigation

    public ViewHelpArticlesPage(Stage primaryStage, String role) {
        this.primaryStage = primaryStage;
        this.tableView = new TableView<>();
        this.role = role; // Initialize the role
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        setupTableView(tableView);

        // Initial load of all articles
        List<HelpArticle> articles = fetchHelpArticles();
        tableView.getItems().addAll(articles);

        // Filter button
        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> showFilterDialog());

        // Remove All Filters button
        Button removeFiltersButton = new Button("Remove All Filters");
        removeFiltersButton.setOnAction(e -> resetFilters());

        // Back button to return to the appropriate dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if ("Admin".equals(role)) {
                new AdminDashboard(primaryStage).show();
            } else {
                new UserDashboard(primaryStage).show(); // or create a specific instructor dashboard
            }
        });

        HBox buttonBox = new HBox(10, filterButton, removeFiltersButton);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(new Label("Help Articles"), buttonBox, tableView, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("View Help Articles");
        primaryStage.show();
    }

    private void setupTableView(TableView<HelpArticle> tableView) {
        TableColumn<HelpArticle, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<HelpArticle, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<HelpArticle, String> groupColumn = new TableColumn<>("Group Name");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));

        TableColumn<HelpArticle, String> keywordsColumn = new TableColumn<>("Keywords");
        keywordsColumn.setCellValueFactory(new PropertyValueFactory<>("keywords"));

        tableView.getColumns().addAll(titleColumn, descriptionColumn, groupColumn, keywordsColumn);

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                HelpArticle selectedArticle = tableView.getSelectionModel().getSelectedItem();
                if (selectedArticle != null) {
                    new ArticleDetailPage(primaryStage, selectedArticle).show();
                }
            }
        });
    }

    private List<HelpArticle> fetchHelpArticles() {
        List<HelpArticle> articles = new ArrayList<>();
        String query = "SELECT title, description, group_name, keywords, body, level, links, id FROM HelpArticles WHERE deleted = 0";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String group = rs.getString("group_name");
                String keywords = rs.getString("keywords");
                String body = rs.getString("body");
                String level = rs.getString("level");
                String links = rs.getString("links");
                int id = rs.getInt("id");
                articles.add(new HelpArticle(title, description, keywords, group, body, level, links, id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }

    private void showFilterDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Filter Articles");

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        TextField titleField = new TextField();
        titleField.setPromptText("Enter title...");

        TextField groupField = new TextField();
        groupField.setPromptText("Enter group...");

        TextField keywordsField = new TextField();
        keywordsField.setPromptText("Enter keywords...");

        Button applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setOnAction(e -> {
            applyFilters(titleField.getText(), groupField.getText(), keywordsField.getText());
            dialog.close();
        });

        dialogLayout.getChildren().addAll(
                new Label("Search by Title"), titleField,
                new Label("Search by Group"), groupField,
                new Label("Search by Keywords"), keywordsField,
                applyFilterButton
        );

        Scene dialogScene = new Scene(dialogLayout, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void applyFilters(String titleQuery, String groupQuery, String keywordsQuery) {
        List<HelpArticle> filteredArticles = fetchHelpArticles().stream()
                .filter(article -> article.getTitle().toLowerCase().contains(titleQuery.toLowerCase()) &&
                        article.getGroup().toLowerCase().contains(groupQuery.toLowerCase()) &&
                        article.getKeywords().toLowerCase().contains(keywordsQuery.toLowerCase()))
                .toList();

        tableView.getItems().setAll(filteredArticles);
    }

    private void resetFilters() {
        tableView.getItems().setAll(fetchHelpArticles());
    }
}