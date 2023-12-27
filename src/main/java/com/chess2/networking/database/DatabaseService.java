package com.chess2.networking.database;

import com.chess2.Console;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public abstract class DatabaseService {
    private static final String URL = "jdbc:mysql://localhost/chess2.0";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static final int INVALID_USER = -1;

    private static Connection connection;
    private static boolean connected = false;

    public static boolean isConnected() {
        return connected;
    }

    public static void connect() {
        if (isConnected()) {
            Console.log(Console.WARNING, "Can't connect to Database.Server is already connected to database!");
            return;
        }

        Console.log(Console.INFO, "Loading MySQL JDBC driver...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Console.log(Console.ERROR, "Error loading MySQL JDBC driver!");
            return;
        }
        Console.log(Console.INFO, "Loaded MySQL JDBC driver!");

        Console.log(Console.INFO, "Connecting to database...");
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Failed to connect to database!");
            return;
        }
        Console.log(Console.INFO, "Connected to database!");
        connected = true;
    }

    public static void disconnect() {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't disconnect from database.Database is already not connected!");
            return;
        }
        Console.log(Console.INFO, "Disconnecting from database...");
        try {
            connection.close();
            Console.log(Console.INFO, "Disconnected from database!");
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Failed to disconnect from database!");
        }
        connected = false;
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't execute query. Not connected to the database!");
            return null;
        }

        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static int executeUpdate(String query) throws SQLException {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't execute update. Not connected to the database!");
            return -1;
        }

        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static PreparedStatement prepareStatement(String query) throws SQLException {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't prepare statement. Not connected to the database!");
            return null;
        }

        return connection.prepareStatement(query);
    }

    public static void beginTransaction() throws SQLException {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't begin transaction. Not connected to the database!");
            return;
        }

        connection.setAutoCommit(false);
    }

    public static void commitTransaction() throws SQLException {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't commit transaction. Not connected to the database!");
            return;
        }

        connection.commit();
        connection.setAutoCommit(true);
    }

    public static void rollbackTransaction() throws SQLException {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't rollback transaction. Not connected to the database!");
            return;
        }

        connection.rollback();
        connection.setAutoCommit(true);
    }

    public static int validateLogin(String username, String password) {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't validate login. Not connected to the database!");
            return INVALID_USER;
        }

        String query = "SELECT * FROM User WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userID = resultSet.getInt("userID");
                    String storedPasswordHash = resultSet.getString("password_hash");

                    if (BCrypt.checkpw(password, storedPasswordHash)) {
                        Console.log(Console.INFO, "Login successful for user: " + username);
                        return userID;
                    } else {
                        Console.log(Console.WARNING, "Invalid password for user: " + username);
                        return INVALID_USER;
                    }
                } else {
                    Console.log(Console.WARNING, "User not found: " + username);
                    return INVALID_USER;
                }
            }
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Error validating login: " + e.getMessage());
            return INVALID_USER;
        }
    }

    public static boolean addUser(String username, String password) {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't add user. Not connected to the database!");
            return false;
        }

        if (userExists(username)) {
            Console.log(Console.WARNING, "Username already exists: " + username);
            return false;
        }

        String insertQuery = "INSERT INTO User (username, password_hash, date_created) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Console.log(Console.INFO, "User added successfully: " + username);
                return true;
            } else {
                Console.log(Console.ERROR, "Failed to add user: " + username);
                return false;
            }
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Error adding user: " + e.getMessage());
            return false;
        }
    }

    public static boolean userExists(String username) {
        String query = "SELECT * FROM User WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Error checking if user exists: " + e.getMessage());
            return false;
        }
    }

    public static UserData getUserDataById(int userId) {
        if (!isConnected()) {
            Console.log(Console.WARNING, "Can't retrieve user data. Not connected to the database!");
            return null;
        }

        if (userId == INVALID_USER)
            return null;

        String query = "SELECT u.userID, u.username, u.date_created, COUNT(g1.gameID) as games_won, COUNT(g2.gameID) as games_lost " +
                "FROM User u " +
                "LEFT JOIN Game g1 ON u.userID = g1.winnerID " +
                "LEFT JOIN Game g2 ON u.userID = g2.user1ID OR u.userID = g2.user2ID " +
                "WHERE u.userID = ? " +
                "GROUP BY u.userID, u.username, u.date_created";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int storedUserId = resultSet.getInt("userID");
                    String username = resultSet.getString("username");
                    String dateCreated = resultSet.getString("date_created");
                    int gamesWon = resultSet.getInt("games_won");
                    int gamesLost = resultSet.getInt("games_lost");

                    return new UserData(storedUserId, username, dateCreated, gamesWon, gamesLost);
                } else {
                    Console.log(Console.WARNING, "User not found with ID: " + userId);
                    return null;
                }
            }
        } catch (SQLException e) {
            Console.log(Console.ERROR, "Error retrieving user data: " + e.getMessage());
            return null;
        }
    }
}