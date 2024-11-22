package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Main extends Application {
    double originalPrice = 0;

    @Override
    public void start(Stage primaryStage) {
        // SellerView setup
        BorderPane sellerView = new BorderPane();
        GridPane centerpane = new GridPane();
        centerpane.setAlignment(Pos.CENTER);
        centerpane.setMaxHeight(500);
        centerpane.setMaxWidth(300);
        centerpane.setHgap(10);
        centerpane.setVgap(10);
        centerpane.setPadding(new Insets(10));
        sellerView.setStyle("-fx-background-color: linear-gradient(to bottom,  #FFFFFF, #8B0000);");
        centerpane.setStyle("-fx-background-color: grey;");
        Text scenetitle = new Text("Sell Your Book");
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        centerpane.add(scenetitle, 0, 0);
        
        // Add fields for book information
        Text ititle = new Text("Name of your book");
        TextField bookName = new TextField();
        centerpane.add(bookName, 0, 2);
        centerpane.add(ititle, 0, 1);
        
        ititle = new Text("Category");
        TextField bookCategory = new TextField();
        centerpane.add(bookCategory, 0, 5);
        centerpane.add(ititle, 0, 4);
        
        ititle = new Text("Condition");
        TextField bookCondition = new TextField();
        centerpane.add(bookCondition, 0, 8);
        centerpane.add(ititle, 0, 7);
        
        ititle = new Text("Original Price");
        TextField bookOriginalPrice = new TextField();
        centerpane.add(bookOriginalPrice, 0, 11);
        centerpane.add(ititle, 0, 10);
        
        ititle = new Text("Buying Price");
        centerpane.add(ititle, 0, 13);
        Label bookBuyingPrice = new Label("$0.00");
        bookBuyingPrice.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));;
        centerpane.add(bookBuyingPrice, 0, 14);
        
        // Calculate buying price when original price is entered
        bookOriginalPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (bookOriginalPrice.getText().isEmpty()) {
                    originalPrice = 0;
                } else {
                    originalPrice = Integer.parseInt(bookOriginalPrice.getText());
                }
                bookBuyingPrice.setText("$" + originalPrice * 0.75);
            } catch (NumberFormatException ex) {
                bookBuyingPrice.setText("Invalid value entered for original price");
            }
        });
        
        // Seller buttons
        Button sellConfirm = new Button("List My Book");
        HBox sHBox = new HBox();
        sHBox.setSpacing(60);
        Button sHome = new Button("Home");
        sHome.setOnAction((ActionEvent e) -> {
            sHome.getScene().setRoot(sellerView);
        });
        sHBox.getChildren().addAll(sellConfirm, sHome);
        centerpane.add(sHBox, 0, 16);
        sellerView.setCenter(centerpane);
        
        // Login screen setup
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.setStyle(
            "-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px;"
        );

        VBox loginLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        loginLayout.setStyle(
            "-fx-padding: 20; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222); " +
            "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #8B0000; -fx-border-width: 2;"
        );

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

            // Database connection details
            String url = "jdbc:mysql://localhost:3306/SunDevilBookSystem"; 
            String dbUsername = "root"; 
            String dbPassword = "password"; 

            try {
                // Establish connection to the database
                Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                
                
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
          
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, "buyer"); // Default role set as 'buyer'
                
                // Execute the insertion
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User added successfully!");
                } else {
                    System.out.println("Error: User could not be added.");
                }

                // Close the connection
                conn.close();

            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle any SQL exceptions
            }

            // Change scene to seller's screen after login
            primaryStage.setScene(new Scene(sellerView, 800, 600)); // Set seller screen size
        });

        StackPane outerLayout = new StackPane(loginLayout);
        outerLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #8B0000, #FFFFFF);"
        );

        Scene scene = new Scene(outerLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

