package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main extends Application {

    private static final double WINDOW_WIDTH = 800;
    private static final double WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        showLoginView(primaryStage);
    }

    public void showLoginView(Stage primaryStage) {
        // Header
        HBox header = createHeader("Welcome to SunDevils Book Store");

        // Username and password fields
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefWidth(10); // Set smaller width

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(10); // Set smaller width

        Button loginButton = new Button("Login");
        styleButton(loginButton);

        VBox loginLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        styleVBox(loginLayout);

        styleTextField(usernameField);
        styleTextField(passwordField);
        styleLabel(usernameLabel);
        styleLabel(passwordLabel);

        // Footer
        HBox footer = createFooter("© 2024 SunDevils Book Store. All rights reserved.");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try (Connection conn = DatabaseHelper.getConnection()) {
                String query = "SELECT role FROM users WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");
                    switch (role.toLowerCase()) {
                        case "admin":
                            showAdminView(primaryStage);
                            break;
                        case "buyer":
                            showBuyBookView(primaryStage);
                            break;
                        case "seller":
                            showSellBookView(primaryStage);
                            break;
                        default:
                            showErrorDialog("Login Error", "Invalid role specified.");
                    }
                } else {
                    showErrorDialog("Login Error", "Invalid username or password.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorDialog("Database Error", "Unable to connect to the database.");
            }
        });

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(loginLayout);
        mainLayout.setBottom(footer);

        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

  public void showMenuView(Stage primaryStage) {
        // Header
        HBox header = createHeader("Main Menu");

        // Menu Buttons
        Button adminButton = new Button("Admin Dashboard");
        Button buyerButton = new Button("Buy Books");
        Button sellerButton = new Button("Sell Books");
        Button logoutButton = new Button("Logout");

        double buttonWidth = 200; // Adjust this value as needed
        adminButton.setPrefWidth(buttonWidth);
        buyerButton.setPrefWidth(buttonWidth);
        sellerButton.setPrefWidth(buttonWidth);
        logoutButton.setPrefWidth(buttonWidth);
        
        adminButton.setOnAction(e -> showAdminView(primaryStage));
        buyerButton.setOnAction(e -> showBuyBookView(primaryStage));
        sellerButton.setOnAction(e -> showSellBookView(primaryStage));
        logoutButton.setOnAction(e -> showLoginView(primaryStage));

        styleButton(adminButton);
        styleButton(buyerButton);
        styleButton(sellerButton);
        styleButton(logoutButton);

        VBox menuLayout = new VBox(20, adminButton, buyerButton, sellerButton, logoutButton);
        menuLayout.setPadding(new Insets(20));
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222);");

        // Footer
        HBox footer = createFooter("© 2024 SunDevils Book Store. All rights reserved.");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(menuLayout);
        mainLayout.setBottom(footer);

        Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }

    public void showBuyBookView(Stage primaryStage) {
        BuyBookView buyBookView = new BuyBookView(primaryStage, this);
        buyBookView.showBuyBookView();
    }

    public void showSellBookView(Stage primaryStage) {
        SellerView sellerView = new SellerView(primaryStage, this);
        sellerView.showSellerView();
    }

    public void showAdminView(Stage primaryStage) {
        AdminView adminView = new AdminView(primaryStage, this);
        adminView.showAdminView();
    }

    private HBox createHeader(String title) {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #8B0000;");

        Text welcomeText = new Text(title);
        welcomeText.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        welcomeText.setFill(javafx.scene.paint.Color.WHITE);

        header.getChildren().add(welcomeText);
        return header;
    }

    private HBox createFooter(String footerText) {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #8B0000;");

        Text footerLabel = new Text(footerText);
        footerLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        footerLabel.setFill(javafx.scene.paint.Color.WHITE);

        footer.getChildren().add(footerLabel);
        return footer;
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
    }

    private void styleVBox(VBox vbox) {
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222); " +
                "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #8B0000; -fx-border-width: 2;");
    }

    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-padding: 10; -fx-border-color: #8B0000; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
    }

    private void styleLabel(Label label) {
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #8B0000;");
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
