package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/SunDevilBookSystem?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1987tyty";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database successfully.");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database. Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
