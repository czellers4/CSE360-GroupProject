package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminView {

    private Stage primaryStage;
    private Main mainApp;

    public AdminView(Stage primaryStage, Main mainApp) {
        this.primaryStage = primaryStage;
        this.mainApp = mainApp;
    }

    public void showAdminView() {
        // Header
        HBox header = createHeader("Admin Dashboard");

        // Tabs for User Management, Book Management, Session Management, and Statistics
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab userTab = new Tab("Manage Users", createUserManagementTab());
        Tab bookTab = new Tab("Manage Books", createBookManagementTab());
        Tab sessionTab = new Tab("Active Sessions", createSessionManagementTab());
        Tab statisticsTab = new Tab("Statistics", createStatisticsTab());

        tabPane.getTabs().addAll(userTab, bookTab, sessionTab, statisticsTab);

        // Back to Menu Button
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        backButton.setOnAction(e -> mainApp.showMenuView(primaryStage));

        VBox content = new VBox(20, tabPane, backButton);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(header);
        layout.setCenter(content);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #B22222);");

        Scene scene = new Scene(layout, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }

    private VBox createUserManagementTab() {
        Label usersLabel = new Label("Manage Users:");
        usersLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

        TableView<User> usersTable = new TableView<>();
        setupUsersTable(usersTable);
        loadUsers(usersTable);

        VBox layout = new VBox(10, usersLabel, usersTable);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER);

        return layout;
    }

    private VBox createBookManagementTab() {
        Label booksLabel = new Label("Manage Books:");
        booksLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

        TableView<Book> booksTable = new TableView<>();
        setupBooksTable(booksTable);
        loadBooks(booksTable);

        VBox layout = new VBox(10, booksLabel, booksTable);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER);

        return layout;
    }

    private VBox createSessionManagementTab() {
        Label sessionsLabel = new Label("Active Sessions:");
        sessionsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

        TableView<Session> sessionsTable = new TableView<>();
        setupSessionsTable(sessionsTable);
        loadSessions(sessionsTable);

        Button refreshButton = new Button("Refresh Sessions");
        refreshButton.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setOnAction(e -> loadSessions(sessionsTable));

        VBox layout = new VBox(10, sessionsLabel, sessionsTable, refreshButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER);

        return layout;
    }

    private VBox createStatisticsTab() {
        Label statisticsLabel = new Label("Statistics:");
        statisticsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

        TableView<Statistic> statisticsTable = new TableView<>();
        setupStatisticsTable(statisticsTable);
        loadStatistics(statisticsTable);

        VBox layout = new VBox(10, statisticsLabel, statisticsTable);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER);

        return layout;
    }

    private void setupUsersTable(TableView<User> table) {
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idColumn, usernameColumn, roleColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupBooksTable(TableView<Book> table) {
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Book, String> conditionColumn = new TableColumn<>("Condition");
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(idColumn, titleColumn, categoryColumn, conditionColumn, priceColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupSessionsTable(TableView<Session> table) {
        TableColumn<Session, Integer> sessionIdColumn = new TableColumn<>("Session ID");
        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));

        TableColumn<Session, Integer> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Session, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Session, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionStart"));

        TableColumn<Session, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionEnd"));

        table.getColumns().addAll(sessionIdColumn, userIdColumn, statusColumn, startTimeColumn, endTimeColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupStatisticsTable(TableView<Statistic> table) {
        TableColumn<Statistic, Integer> statIdColumn = new TableColumn<>("Stat ID");
        statIdColumn.setCellValueFactory(new PropertyValueFactory<>("statId"));

        TableColumn<Statistic, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Statistic, Double> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        TableColumn<Statistic, String> lastUpdatedColumn = new TableColumn<>("Last Updated");
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        table.getColumns().addAll(statIdColumn, categoryColumn, valueColumn, lastUpdatedColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadUsers(TableView<User> table) {
        ObservableList<User> users = FXCollections.observableArrayList();

        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT id, username, role FROM users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                users.add(new User(id, username, role));
            }
            table.setItems(users);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "Failed to load users.");
        }
    }

    private void loadBooks(TableView<Book> table) {
        ObservableList<Book> books = FXCollections.observableArrayList();

        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT id, title, category, `condition`, price FROM books";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String category = rs.getString("category");
                String condition = rs.getString("condition");
                double price = rs.getDouble("price");
                books.add(new Book(id, title, category, condition, price));
            }
            table.setItems(books);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "Failed to load books.");
        }
    }

    private void loadSessions(TableView<Session> table) {
        ObservableList<Session> sessions = FXCollections.observableArrayList();

        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT session_id, user_id, status, session_start, session_end FROM session";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int sessionId = rs.getInt("session_id");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                String sessionStart = rs.getString("session_start");
                String sessionEnd = rs.getString("session_end");
                sessions.add(new Session(sessionId, userId, status, sessionStart, sessionEnd));
            }
            table.setItems(sessions);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "Failed to load sessions.");
        }
    }

    private void loadStatistics(TableView<Statistic> table) {
        ObservableList<Statistic> statistics = FXCollections.observableArrayList();

        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT stat_id, category, value, last_updated FROM statistics";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int statId = rs.getInt("stat_id");
                String category = rs.getString("category");
                double value = rs.getDouble("value");
                String lastUpdated = rs.getString("last_updated");
                statistics.add(new Statistic(statId, category, value, lastUpdated));
            }
            table.setItems(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "Failed to load statistics.");
        }
    }

    private HBox createHeader(String title) {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #8B0000;");

        Text headerText = new Text(title);
        headerText.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        headerText.setFill(javafx.scene.paint.Color.WHITE);

        header.getChildren().add(headerText);
        return header;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class User {
        private final int id;
        private final String username;
        private final String role;

        public User(int id, String username, String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }
    }

    public static class Book {
        private final int id;
        private final String title;
        private final String category;
        private final String condition;
        private final double price;

        public Book(int id, String title, String category, String condition, double price) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.condition = condition;
            this.price = price;
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
    }

    public static class Session {
        private final int sessionId;
        private final int userId;
        private final String status;
        private final String sessionStart;
        private final String sessionEnd;

        public Session(int sessionId, int userId, String status, String sessionStart, String sessionEnd) {
            this.sessionId = sessionId;
            this.userId = userId;
            this.status = status;
            this.sessionStart = sessionStart;
            this.sessionEnd = sessionEnd;
        }

        public int getSessionId() {
            return sessionId;
        }

        public int getUserId() {
            return userId;
        }

        public String getStatus() {
            return status;
        }

        public String getSessionStart() {
            return sessionStart;
        }

        public String getSessionEnd() {
            return sessionEnd;
        }
    }

    public static class Statistic {
        private final int statId;
        private final String category;
        private final double value;
        private final String lastUpdated;

        public Statistic(int statId, String category, double value, String lastUpdated) {
            this.statId = statId;
            this.category = category;
            this.value = value;
            this.lastUpdated = lastUpdated;
        }

        public int getStatId() {
            return statId;
        }

        public String getCategory() {
            return category;
        }

        public double getValue() {
            return value;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }
    }
}
