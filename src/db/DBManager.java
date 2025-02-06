package db;

import models.User;

import java.sql.*;

public class DBManager {
    private Connection connection;

    public DBManager(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public boolean registerUser(String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        }
    }

    public User loginUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        }
        return null;
    }

    public void saveCalculation(int userId, String figure, double area, double volume, double perimeter) throws SQLException {
        String query = "INSERT INTO calculations (user_id, figure, area, volume, perimeter) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, figure);
            stmt.setDouble(3, area);
            stmt.setDouble(4, volume);
            stmt.setDouble(5, perimeter);
            stmt.executeUpdate();
        }
    }

    public void showHistory(int userId) throws SQLException {
        String query = "SELECT figure, area, volume, perimeter FROM calculations WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nИстория расчетов:");
            System.out.printf("%-15s %-15s %-15s %-15s%n", "Фигура", "Площадь", "Объем", "Периметр");
            System.out.println("----------------------------------------------------------------------");

            boolean hasHistory = false;

            while (resultSet.next()) {
                hasHistory = true;

                String figureName = resultSet.getString("figure");
                double area = resultSet.getDouble("area");
                double volume = resultSet.getDouble("volume");
                double perimeter = resultSet.getDouble("perimeter");

                System.out.printf("%-15s %-15.2f %-15.2f %-15.2f%n", figureName, area, volume, perimeter);
            }

            if (!hasHistory) {
                System.out.println("У вас пока нет сохраненных расчетов.");
            }
        }
    }

    }


