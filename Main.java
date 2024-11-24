package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Username and password fields
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle(
            "-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px;"
        );

        // Layout for the username and password fields with the login button
        VBox loginLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        loginLayout.setStyle(
            "-fx-padding: 20; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222); " +
            "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #8B0000; -fx-border-width: 2;"
        );

        // Styling input fields and labels
        usernameField.setStyle(
            "-fx-padding: 10; -fx-border-color: #8B0000; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;"
        );
        passwordField.setStyle(
            "-fx-padding: 10; -fx-border-color: #8B0000; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;"
        );

        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8B0000;");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8B0000;");

        // Action for login button
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            // Prints the entered values
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        });

        // Outer container to add padding around login layout
        StackPane outerLayout = new StackPane(loginLayout);
        outerLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #8B0000, #FFFFFF);"
        );

        // Setup the scene and stage
        Scene scene = new Scene(outerLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
