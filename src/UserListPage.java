package asuHelloWorldJavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p> UserListPage Class </p>
 * 
 * <p> Description: This class represents the user list page where admin can view a list of all users and their roles.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for user list functionality in JavaFX
 */



public class UserListPage {

    private Stage primaryStage;

    public UserListPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Label titleLabel = new Label("User List");
        titleLabel.setStyle("-fx-font-size: 24px;");

        TableView<UserData> userTable = new TableView<>();
        userTable.setPrefWidth(400);
        userTable.setPrefHeight(300);
        
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Define columns
        TableColumn<UserData, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<UserData, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        // Add only the necessary columns to the TableView
        userTable.getColumns().addAll(usernameColumn, roleColumn);

        // Load users from the database
        loadUsers(userTable);

        Button backButton = new Button("Back to Admin Dashboard");
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        layout.getChildren().addAll(titleLabel, userTable, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User List");
        primaryStage.show();
    }

    private void loadUsers(TableView<UserData> userTable) {
        String query = "SELECT username, role FROM Users"; // Adjust your query based on your database schema

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String role = rs.getString("role");
                userTable.getItems().add(new UserData(username, role)); // Directly add data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load users.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Simple inner class to hold user data
    public static class UserData {
        private final String username;
        private final String role;

        public UserData(String username, String role) {
            this.username = username;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }
    }
}