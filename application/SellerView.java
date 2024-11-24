package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SellerView {

    private Stage primaryStage;
    private Main mainMenu; 

    public SellerView(Stage primaryStage, Main mainMenu) {
        this.primaryStage = primaryStage;
        this.mainMenu = mainMenu;
    }

    public void showSellerView() {
        // Header with welcome message
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #8B0000;");

        Text welcomeText = new Text("Sell Your Book");
        welcomeText.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        welcomeText.setFill(javafx.scene.paint.Color.WHITE);

        Image logoImage = new Image("file:logo.png"); 
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(80);
        logoView.setFitHeight(80);

        header.getChildren().addAll(logoView, welcomeText);

        // Input fields for book 
        TextField bookNameField = new TextField();
        bookNameField.setPromptText("Enter the book name");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setPromptText("Select a category");
        categoryComboBox.getItems().addAll("Math", "Science", "Computer", "History", "Literature");

        ComboBox<String> conditionComboBox = new ComboBox<>();
        conditionComboBox.setPromptText("Select the condition");
        conditionComboBox.getItems().addAll("Used Like New", "Moderately Used", "Heavily Used");

        TextField originalPriceField = new TextField();
        originalPriceField.setPromptText("Enter the original price in USD");

        TextField buyingPriceField = new TextField();
        buyingPriceField.setPromptText("Generated Buying Price");
        buyingPriceField.setEditable(false);

        // Automatically calculate the buying price
        originalPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double originalPrice = Double.parseDouble(newValue);
                double buyingPrice = originalPrice * 0.75; // Example calculation logic
                buyingPriceField.setText(String.format("%.2f", buyingPrice));
            } catch (NumberFormatException e) {
                buyingPriceField.setText("Invalid Price");
            }
        });

        // List Book button
        Button listBookButton = new Button("List My Book");
        styleButton(listBookButton);
        listBookButton.setOnAction(e -> {
            String bookName = bookNameField.getText();
            String category = categoryComboBox.getValue();
            String condition = conditionComboBox.getValue();
            String originalPrice = originalPriceField.getText();
            String buyingPrice = buyingPriceField.getText();

            if (validateFields(bookName, category, condition, originalPrice)) {
                if (addBookToDatabase(bookName, category, condition, Double.parseDouble(buyingPrice))) {
                    showAlert("Success", "Book listed successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to list the book. Please try again.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Please fill in all fields correctly.", Alert.AlertType.ERROR);
            }
        });

        // Back to Menu button
        Button backButton = new Button("Back to Menu");
        styleButton(backButton);
        backButton.setOnAction(e -> mainMenu.showMenuView(primaryStage));

        // Input form layout
        VBox inputForm = new VBox(10,
                createLabeledField("Name of your book", bookNameField),
                createLabeledField("Category", categoryComboBox),
                createLabeledField("Condition", conditionComboBox),
                createLabeledField("Original Price", originalPriceField),
                createLabeledField("Buying Price", buyingPriceField)
        );
        inputForm.setAlignment(Pos.CENTER);
        inputForm.setPadding(new Insets(20));
        inputForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #8B0000; -fx-border-radius: 10; -fx-padding: 20px;");

        // Buttons layout
        HBox buttonsLayout = new HBox(20, listBookButton, backButton);
        buttonsLayout.setAlignment(Pos.CENTER);

        VBox mainContent = new VBox(20, inputForm, buttonsLayout);
        mainContent.setAlignment(Pos.CENTER);

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setTop(header);
        layout.setCenter(mainContent);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222);");

        // Scene and stage setup
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Seller's View");
        primaryStage.show();
    }

    private <T> HBox createLabeledField(String labelText, T field) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        HBox fieldLayout = new HBox(10, label, (field instanceof Control) ? (Control) field : new Label());
        fieldLayout.setAlignment(Pos.CENTER_LEFT);
        return fieldLayout;
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
    }

    private boolean validateFields(String bookName, String category, String condition, String originalPrice) {
        return !(bookName.isEmpty() || category == null || condition == null || originalPrice.isEmpty());
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean addBookToDatabase(String title, String category, String condition, double price) {
        String query = "INSERT INTO books (title, category, `condition`, price, seller_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, category);
            stmt.setString(3, condition);
            stmt.setDouble(4, price);
            stmt.setInt(5, 1); 

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            System.err.println("Error adding book to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
