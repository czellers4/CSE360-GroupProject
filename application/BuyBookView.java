package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyBookView {

    private Stage primaryStage;
    private Main mainApp; 
    private static ObservableList<CartItem> cartItems = FXCollections.observableArrayList(); // Shared cart items
    private ObservableList<Book> books = FXCollections.observableArrayList();

    public BuyBookView(Stage primaryStage, Main mainApp) {
        this.primaryStage = primaryStage;
        this.mainApp = mainApp;
    }

    public void showBuyBookView() {
        loadBooksFromDatabase(); 

        // Top area: Logo and filters
        HBox topArea = new HBox(20);
        topArea.setPadding(new Insets(10));
        topArea.setAlignment(Pos.CENTER_LEFT);
        topArea.setStyle("-fx-background-color: #8B0000;");

        // Logo
        Image logoImage = new Image("file:logo.png"); 
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setFitHeight(100);

        // Filters
        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.setPromptText("Category");
        categoryFilter.setItems(FXCollections.observableArrayList("Math", "Computer", "Natural Science"));

        ComboBox<String> conditionFilter = new ComboBox<>();
        conditionFilter.setPromptText("Condition");
        conditionFilter.setItems(FXCollections.observableArrayList("Used Like New", "Moderately Used", "Heavily Used"));

        Button filterButton = new Button("Apply Filters");
        styleButton(filterButton);
        filterButton.setOnAction(e -> applyFilters(categoryFilter, conditionFilter));

        Button clearFilterButton = new Button("Clear Filters");
        styleButton(clearFilterButton);
        clearFilterButton.setOnAction(e -> clearFilters(categoryFilter, conditionFilter));

        topArea.getChildren().addAll(logoView, categoryFilter, conditionFilter, filterButton, clearFilterButton);

        // Cart Button
        Button cartButton = new Button("View Cart");
        styleButton(cartButton);
        cartButton.setOnAction(e -> new CartView(primaryStage, cartItems, mainApp).showCartView());
        topArea.getChildren().add(cartButton);

        // Central area: Table for book listings
        TableView<Book> bookTable = new TableView<>();
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columns for Title, Category, Condition, and Price
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Book, String> conditionColumn = new TableColumn<>("Condition");
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Book, Button> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("addToCartButton"));

        bookTable.getColumns().addAll(titleColumn, categoryColumn, conditionColumn, priceColumn, actionColumn);
        bookTable.setItems(books);

        // Back to Main Menu Button
        Button backButton = new Button("Back to Menu");
        styleButton(backButton);
        backButton.setOnAction(e -> mainApp.showMenuView(primaryStage));

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(topArea);
        layout.setCenter(bookTable);
        layout.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222);");

        // Scene and stage setup
        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Buy Textbooks");
        primaryStage.show();
    }

    private void loadBooksFromDatabase() {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT id, title, category, `condition`, price FROM books"; 
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            books.clear();
            while (rs.next()) {
                String title = rs.getString("title");
                String category = rs.getString("category");
                String condition = rs.getString("condition");
                double price = rs.getDouble("price");

                books.add(new Book(title, category, condition, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "Failed to load books from the database.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void applyFilters(ComboBox<String> categoryFilter, ComboBox<String> conditionFilter) {
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList(books);

        String selectedCategory = categoryFilter.getValue();
        String selectedCondition = conditionFilter.getValue();

        if (selectedCategory != null) {
            filteredBooks.removeIf(book -> !book.getCategory().equalsIgnoreCase(selectedCategory));
        }

        if (selectedCondition != null) {
            filteredBooks.removeIf(book -> !book.getCondition().equalsIgnoreCase(selectedCondition));
        }

        books.setAll(filteredBooks);
    }

    private void clearFilters(ComboBox<String> categoryFilter, ComboBox<String> conditionFilter) {
        categoryFilter.setValue(null);
        conditionFilter.setValue(null);
        loadBooksFromDatabase();
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px;");
    }

    public static class Book {
        private final String title;
        private final String category;
        private final String condition;
        private final double price;
        private final Button addToCartButton;

        public Book(String title, String category, String condition, double price) {
            this.title = title;
            this.category = category;
            this.condition = condition;
            this.price = price;
            this.addToCartButton = new Button("Add to cart");
            styleButton(this.addToCartButton);
            this.addToCartButton.setOnAction(e -> {
                cartItems.add(new CartItem(title, price));
                System.out.println("Added " + title + " to cart at $" + price);
            });
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }

        public String getCondition() {
            return condition;
        }

        public double getPrice() {
            return price;
        }

        public Button getAddToCartButton() {
            return addToCartButton;
        }

        private void styleButton(Button button) {
            button.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        }
    }
}
