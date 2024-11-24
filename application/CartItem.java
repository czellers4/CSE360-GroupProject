package application;

import javafx.scene.control.Button;

public class CartItem {
    private final String title;
    private final double price;
    private final Button removeButton;

    public CartItem(String title, double price) {
        this.title = title;
        this.price = price;
        this.removeButton = new Button("Remove");

        // Configure the button's action
        this.removeButton.setOnAction(e -> {
            System.out.println("Remove clicked for: " + title);
            
        });
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
}
