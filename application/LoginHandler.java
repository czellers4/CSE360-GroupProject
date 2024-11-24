package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginHandler {

    public void setupLoginAction(Stage primaryStage, TextField usernameField, PasswordField passwordField, Button loginButton, VBox sellerView) {
        // Action for login button
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // debugging
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            // Database connection
            String url = "jdbc:mysql://localhost:3306/SunDevilBookSystem"; 
            String dbUsername = "root"; 
            String dbPassword = "1987tyty"; 

            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                // SQL query to insert a new user
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.setString(3, "buyer"); 

                    // Execute the insertion
                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("User added successfully!");
                    } else {
                        System.out.println("Error: User could not be added.");
                    }
                }
            } catch (SQLException ex) {
                // Handle SQL exceptions
                ex.printStackTrace();
            }

           
            primaryStage.setScene(new Scene(sellerView, 800, 600)); 
        });
    }
}
