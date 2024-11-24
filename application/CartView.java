package application;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CartView {

    private Stage primaryStage;
    private ObservableList<CartItem> cartItems;
    private Main mainApp; 

    public CartView(Stage primaryStage, ObservableList<CartItem> cartItems, Main mainApp) {
        this.primaryStage = primaryStage;
        this.cartItems = cartItems;
        this.mainApp = mainApp;
    }

    public void showCartView() {
        // Table for cart items
        TableView<CartItem> cartTable = new TableView<>();
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CartItem, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<CartItem, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<CartItem, Button> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("removeButton"));

        cartTable.getColumns().addAll(titleColumn, priceColumn, actionColumn);
        cartTable.setItems(cartItems);

        // Checkout Button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        checkoutButton.setOnAction(e -> handleCheckout());

        // Back to Main Menu Button
        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        backToMenuButton.setOnAction(e -> mainApp.showMenuView(primaryStage));

        // Layout
        HBox buttonLayout = new HBox(20, checkoutButton, backToMenuButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, cartTable, buttonLayout);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222);");

        // Scene setup
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cart");
        primaryStage.show();
    }

    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert("Checkout Error", "Your cart is empty!", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String transactionQuery = "INSERT INTO transactions (user_id, book_title, price, transaction_date) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(transactionQuery);

            int userId = 1; 
            LocalDateTime now = LocalDateTime.now();
            String transactionDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            for (CartItem item : cartItems) {
                stmt.setInt(1, userId);
                stmt.setString(2, item.getTitle());
                stmt.setDouble(3, item.getPrice());
                stmt.setString(4, transactionDate);
                stmt.addBatch();
            }

            stmt.executeBatch();
            cartItems.clear(); 

            showAlert("Checkout Successful", "Your purchase is completed!", Alert.AlertType.INFORMATION);
            mainApp.showMenuView(primaryStage); 
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to complete the checkout. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
