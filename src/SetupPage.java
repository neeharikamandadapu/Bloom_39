package asuHelloWorldJavaFX;

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
 * <p> SetupPage Class </p>
 * 
 * <p> Description: This class represents the account setup page for users where they can fill in personal information and complete their setup process.</p>
 * 
 * <p> Copyright: Group 39 © 2024 </p>
 * 
 * @author Group 39
 * 
 * @version 1.00        2024-10-10 Initial version for user setup functionality in JavaFX
 */

public class SetupPage {

    // The primary stage that holds the UI for the setup page
    private Stage primaryStage;

    // Stores the username for the setup process
    private String username; 

    // Stores the email from the invitation
    private String email;

    // Stores the password from LoginPage for confirmation
    private String loginPassword; 

    // Stores the user role from the invitation
    private String role;

    // Tracks if the user is an admin
    private boolean isAdmin;

    /**
     * Constructor that initializes the setup page with relevant data.
     * 
     * @param primaryStage The primary stage used to display the setup page.
     * @param username The username from the login page.
     * @param loginPassword The password from the login page.
     * @param invitationCode The invitation code provided during account setup.
     */
    public SetupPage(Stage primaryStage, String username, String loginPassword, String invitationCode) {
        // Assign the primary stage
        this.primaryStage = primaryStage;

        // Receive the username from the previous page
        this.username = username;

        // Store the password from the login page
        this.loginPassword = loginPassword;

        // Check if the invitation code is provided
        if (invitationCode == null || invitationCode.isEmpty()) {
            // Set role to ADMIN if no invitation code is provided
            this.role = "ADMIN";
            
            // Set email to null if no invitation code
            this.email = null;
        } else {
            // Fetch email and role from the invitation using the invitation code
            String[] invitationDetails = fetchEmailAndRoleFromInvitation(invitationCode);

            // Assign the fetched email
            this.email = invitationDetails[0];

            // Assign the fetched role
            this.role = invitationDetails[1];
        }

        // Check if the user is an admin based on the absence of an invitation code
        this.isAdmin = (invitationCode == null || invitationCode.isEmpty());
    }

    /**
     * Displays the Setup page where users can input personal details and complete the setup process.
     */
    public void show() {
        // Create a VBox layout with 10px spacing
        VBox setupLayout = new VBox(10);

        // Add padding of 20px around the layout
        setupLayout.setPadding(new Insets(20));

        // Center align all elements within the layout
        setupLayout.setAlignment(Pos.CENTER);

        // Set the background color to white
        setupLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Create and style a label for the application name
        Label appNameLabel = new Label("BLOOM");
        appNameLabel.setStyle("-fx-font-size: 24px;");

        // Create a text field for the first name with a prompt
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name (Required)");

        // Create a text field for the middle name with a prompt
        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Middle Name (Optional)");

        // Create a text field for the last name with a prompt
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name (Required)");

        // Create a text field for the preferred name with a prompt
        TextField preferredNameField = new TextField();
        preferredNameField.setPromptText("Preferred Name (Optional)");

        // Create a password field for confirming the password with a prompt
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password (Required)");

        // Create a text field for the email address with a prompt
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");

        // If the user is not an admin, pre-fill the email field with the fetched email
        if (!isAdmin) {
            emailField.setText(email);
        }

        // Create a button for finishing the setup process
        Button finishSetupButton = new Button("Finish Setup");

        // Set the action for the button to collect and validate user inputs
        finishSetupButton.setOnAction(e -> {
            // Collect the first name from the input field
            String firstName = firstNameField.getText();

            // Collect the last name from the input field
            String lastName = lastNameField.getText();

            // Collect the middle name (optional) from the input field
            String middleName = middleNameField.getText();

            // Collect the preferred name (optional) from the input field
            String preferredName = preferredNameField.getText();

            // Collect the confirmed password from the password field
            String confirmPassword = confirmPasswordField.getText();

            // Ensure all required fields are filled and passwords match
            if (!firstName.isEmpty() && !lastName.isEmpty() && !confirmPassword.isEmpty()) {
                if (confirmPassword.equals(loginPassword)) {
                    // Determine the role based on invitation usage
                    String assignedRole = isAdmin ? "Admin" : role;

                    // Insert the user info into the database
                    if (insertUserIntoDatabase(username, loginPassword, firstName, lastName, middleName, preferredName, emailField.getText(), assignedRole, isAdmin)) {
                        // Mark the invitation as used
                        markUsedAfterActivation(username);

                        // Redirect to the appropriate dashboard based on admin status
                        if (isAdmin) {
                            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
                            adminDashboard.show();
                        } else {
                            RoleSelectionPage roleSelectionPage = new RoleSelectionPage(primaryStage);
                            roleSelectionPage.show();
                        }
                    } else {
                        // Show an alert if setup fails
                        showAlert("Setup Error", "Could not complete the setup.", "Please try again.");
                    }
                } else {
                    // Show an alert if passwords do not match
                    showAlert("Password Error", "Passwords do not match.", "Please make sure the confirmed password matches the entered password.");
                }
            } else {
                // Show an alert if required fields are not filled
                showAlert("Input Error", "Please fill in all required fields.", "All required fields must be filled.");
            }
        });

        // Add all UI elements to the layout
        setupLayout.getChildren().addAll(appNameLabel, firstNameField, lastNameField, middleNameField, preferredNameField, confirmPasswordField, emailField, finishSetupButton);

        // Create a new scene with the layout and set it on the primary stage
        Scene setupScene = new Scene(setupLayout, 600, 400);
        primaryStage.setScene(setupScene);

        // Set the title of the primary stage
        primaryStage.setTitle("Account Setup");

        // Show the primary stage
        primaryStage.show();
    }

    /**
     * Fetches email and role from the invitation based on the provided invitation code.
     * 
     * @param invitationCode The invitation code to fetch details from the database.
     * @return An array containing the email and role from the invitation.
     */
    private String[] fetchEmailAndRoleFromInvitation(String invitationCode) {
        // Create an array to hold email and role
        String[] details = new String[2];

        // SQL query to select email and role from the Invitations table
        String query = "SELECT email, role FROM Invitations WHERE invitation_code = ?";

        // Try-with-resources block for executing the query
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the invitation code in the prepared statement
            stmt.setString(1, invitationCode);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            
            // Check if a result is found
            if (rs.next()) {
                // Fetch and assign the email from the result set
                details[0] = rs.getString("email");

                // Fetch and assign the role from the result set
                details[1] = rs.getString("role");
            } else {
                // Handle the case where no results are found
                details[0] = null;
                details[1] = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the array with fetched details
        return details;
    }

    /**
     * Inserts the user information into the Users table of the database.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param middleName The middle name of the user.
     * @param preferredName The preferred name of the user.
     * @param email The email address of the user.
     * @param role The role assigned to the user.
     * @param isAdmin Flag indicating whether the user is an admin.
     * @return true if the insertion was successful, false otherwise.
     */
    private boolean insertUserIntoDatabase(String username, String password, String firstName, String lastName, String middleName, String preferredName, String email, String role, boolean isAdmin) {
        // SQL query to insert user details into the Users table
        String insertUserSQL = "INSERT INTO Users (username, password, first_name, last_name, middle_name, preferred_name, email, role, is_admin, is_set_up) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        
        // Try-with-resources block for executing the SQL insertion
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(insertUserSQL)) {

            // Set the parameters for the prepared statement
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, middleName);
            stmt.setString(6, preferredName);
            stmt.setString(7, email);
            stmt.setString(8, role);
            stmt.setBoolean(9, isAdmin);

            // Execute the insertion
            stmt.executeUpdate();
            return true; // Successful insertion
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Insertion failed
        }
    }

    /**
     * Marks the invitation as used after activation by updating the invitation record in the database.
     * 
     * @param username The username of the user.
     */
    private void markUsedAfterActivation(String username) {
        // SQL query to update the invitation record and mark it as used
        String updateInvitationSQL = "UPDATE Invitations SET is_used = 1 WHERE email = ?";

        // Try-with-resources block for executing the update query
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(updateInvitationSQL)) {

            // Set the email parameter in the prepared statement
            stmt.setString(1, email);

            // Execute the update query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the provided title, header, and content.
     * 
     * @param title   The title of the alert dialog.
     * @param header  The header text of the alert dialog.
     * @param content The content message of the alert dialog.
     */
    private void showAlert(String title, String header, String content) {
        // Create a new information alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set the title, header, and content of the alert
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Display the alert and wait for the user to close it
        alert.showAndWait();
    }
}
