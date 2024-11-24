package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookFetcher {

    // Method to fetch books and return as a list
    public static List<Book> fetchBooks() {
        String query = "SELECT id, title, category, `condition`, price, seller_id FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String category = rs.getString("category");
                String condition = rs.getString("condition");
                double price = rs.getDouble("price");
                int sellerId = rs.getInt("seller_id");

                // Create a new Book object and add to the list
                books.add(new Book(id, title, category, condition, price, sellerId));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch books. Error: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    public static void main(String[] args) {
        List<Book> books = fetchBooks();
        
        // Print the fetched books
        books.forEach(book -> {
            System.out.println("Book ID: " + book.getId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Category: " + book.getCategory());
            System.out.println("Condition: " + book.getCondition());
            System.out.println("Price: $" + book.getPrice());
            System.out.println("Seller ID: " + book.getSellerId());
            System.out.println("---------------------------");
        });
    }

    // Book class to represent book objects
    public static class Book {
        private final int id;
        private final String title;
        private final String category;
        private final String condition;
        private final double price;
        private final int sellerId;

        public Book(int id, String title, String category, String condition, double price, int sellerId) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.condition = condition;
            this.price = price;
            this.sellerId = sellerId;
        }

        public int getId() {
            return id;
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

        public int getSellerId() {
            return sellerId;
        }
    }
}