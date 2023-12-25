package com.chess2.networking;

import java.sql.*;

public class DataBase {
    private static final String DB_URL = "jdbc:mysql://localhost/korisnici";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC driver!");
            e.printStackTrace();
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            String query = "SELECT * FROM `users` ";
            Statement st = (Statement) connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                System.out.println("DB_USER: ID(" + id + ") , USERNAME(" + username + ") , PASSWORD(" + password + ")");
            }
            st.close();
        } catch (SQLException ex) {
            System.err.println("Error connecting to the database!");
            ex.printStackTrace();
        }
    }
}
