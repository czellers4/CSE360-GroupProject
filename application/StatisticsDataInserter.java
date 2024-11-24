package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class StatisticsDataInserter {

    public static boolean insertStatistic(String category, double value, Timestamp lastUpdated) {
        String query = "INSERT INTO statistics (category, value, last_updated) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category);
            stmt.setDouble(2, value);
            stmt.setTimestamp(3, lastUpdated);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            System.err.println("Error inserting statistics data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Example usage
        boolean result = insertStatistic("Books Sold", 150.75, new Timestamp(System.currentTimeMillis()));
        if (result) {
            System.out.println("Statistic inserted successfully!");
        } else {
            System.out.println("Failed to insert statistic.");
        }
    }
}
