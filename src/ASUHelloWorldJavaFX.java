package asuHelloWorldJavaFX;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * <p> ASUHelloWorldJavaFX Class </p>
 * 
 * <p> Description: This class serves as the entry point for a JavaFX application. 
 * It initializes the database connection and sets up the initial login screen for the user. 
 * The `start` method is overridden to define what happens when the application is launched. 
 * </p>
 * 
 * <p> The primaryStage field is used to maintain a reference to the main stage of the application, 
 * which is passed to various other screens, starting with the login page. </p>
 * 
 * <p> The main method calls `launch`, which starts the JavaFX application. </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00 2024-10-09 Initial implementation of the JavaFX application structure
 * 
 */

public class ASUHelloWorldJavaFX extends Application {

    private Stage primaryStage;  // Field for primary stage

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;  // Assign the local parameter to the class field
        DatabaseConnection.initializeDatabase();
        
        
        // Show the login page initially, using the primaryStage field
        InitialLogin initialLogin = new InitialLogin(primaryStage);  // Use primaryStage here
        initialLogin.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
