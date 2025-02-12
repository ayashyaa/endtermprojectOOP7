package db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.CalculationHistory;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DBManager {
    private final Connection connection;

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
    public List<CalculationHistory> getHistory(int userId) throws SQLException {
        String query = "SELECT figure, area, volume, perimeter FROM calculations WHERE user_id = ?";
        List<CalculationHistory> historyList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                historyList.add(new CalculationHistory(
                        resultSet.getString("figure"),
                        resultSet.getDouble("area"),
                        resultSet.getDouble("volume"),
                        resultSet.getDouble("perimeter")
                ));
            }
        }
        return historyList;
    }

    public void showHistory(int userId, Stage stage, Consumer<Stage> onBack) throws SQLException {
        String query = "SELECT figure, area, volume, perimeter FROM calculations WHERE user_id = ?";

        ObservableList<CalculationHistory> historyList = FXCollections.observableArrayList();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                historyList.add(new CalculationHistory(
                        resultSet.getString("figure"),
                        resultSet.getDouble("area"),
                        resultSet.getDouble("volume"),
                        resultSet.getDouble("perimeter")
                ));
            }
        }

        TableView<CalculationHistory> table = new TableView<>();

        TableColumn<CalculationHistory, String> figureCol = new TableColumn<>("Фигура");
        figureCol.setCellValueFactory(new PropertyValueFactory<>("figure"));

        TableColumn<CalculationHistory, Double> areaCol = new TableColumn<>("Площадь");
        areaCol.setCellValueFactory(new PropertyValueFactory<>("area"));

        TableColumn<CalculationHistory, Double> volumeCol = new TableColumn<>("Объем");
        volumeCol.setCellValueFactory(new PropertyValueFactory<>("volume"));

        TableColumn<CalculationHistory, Double> perimeterCol = new TableColumn<>("Периметр");
        perimeterCol.setCellValueFactory(new PropertyValueFactory<>("perimeter"));

        table.getColumns().addAll(figureCol, areaCol, volumeCol, perimeterCol);
        table.setItems(historyList);

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> onBack.accept(stage));

        VBox layout = new VBox(10, table, backButton);
        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

}


