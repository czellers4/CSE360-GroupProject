package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class SessionDataInserter {

    public static boolean insertSession(int userId, String status, Timestamp sessionStart, Timestamp sessionEnd) {
        String query = "INSERT INTO session (user_id, status, session_start, session_end) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, status);
            stmt.setTimestamp(3, sessionStart);
            stmt.setTimestamp(4, sessionEnd);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            System.err.println("Error inserting session data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Example usage
        boolean result = insertSession(1, "Active", new Timestamp(System.currentTimeMillis()), null);
        if (result) {
            System.out.println("Session inserted successfully!");
        } else {
            System.out.println("Failed to insert session.");
        }
    }
}

