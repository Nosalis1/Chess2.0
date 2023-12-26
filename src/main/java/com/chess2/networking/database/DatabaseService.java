package com.chess2.networking.database;

import com.chess2.Console;
import javafx.util.Pair;

import java.sql.*;

public class DatabaseService {
    public static final DatabaseService instance = new DatabaseService();

    private static final String DB_URL = "jdbc:mysql://localhost/chess2.0";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    private Connection connection;
    private boolean connected;

    public final boolean isConnected() {
        return this.connected;
    }

    public DatabaseService() {
        this.connected = false;
    }

    public void connect() throws DatabaseServiceException {
        this.connected = false;
        try {
            Console.log(Console.INFO, "Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Thread.sleep(100);
        } catch (ClassNotFoundException e) {
            Console.log(Console.ERROR, "Error loading MySQL JDBC driver!");
            throw new DatabaseServiceException();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Console.log(Console.INFO, "Loaded MySQL JDBC driver!");

        try {
            Console.log(Console.INFO, "Connecting to the database...");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Thread.sleep(100);
            this.connected = true;
            Console.log(Console.INFO, "Connected to the database!");
        } catch (SQLException ex) {
            Console.log(Console.ERROR, "Error connecting to the database!");
            throw new DatabaseServiceException();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public final int validateLogin(final Pair<String, String> data) {
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, data.getKey());
            statement.setString(2, data.getValue());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            Console.log(Console.ERROR, e.getMessage());
            return -1;
        }
    }

    public final UserData readData(final int userId) {
        String query = "SELECT ud.*, u.username FROM user_data ud JOIN users u ON ud.user_id = u.id WHERE ud.user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserDataFromResultSet(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            Console.log(Console.ERROR, e.getMessage());
            return null;
        }
    }

    private UserData extractUserDataFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        Date dateCreated = resultSet.getDate("dateCreated");
        int gamesWon = resultSet.getInt("gamesWon");
        int gamesLost = resultSet.getInt("gamesLost");

        return new UserData(username, dateCreated, gamesWon, gamesLost);
    }

    private ResultSet getQuery(final String query) {
        if (this.isConnected() || query == null) return null;
        try {
            Statement st = (Statement) this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            st.close();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void terminateConnection() {
        if (!this.isConnected()) return;
        Console.log(Console.INFO, "Terminating DatabaseService...");
        try {
            this.connection.close();
            Console.log(Console.INFO, "DatabaseService terminated!");
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Failed to terminate DatabaseService!");
        }
        this.connected = false;
    }
}