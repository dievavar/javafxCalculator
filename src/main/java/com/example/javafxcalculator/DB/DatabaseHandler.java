package com.example.javafxcalculator.DB;

import com.example.javafxcalculator.domain.CurrencyConversion;
import com.example.javafxcalculator.domain.ExchangeRate;
import com.example.javafxcalculator.domain.OhmLawCalculation;
import com.example.javafxcalculator.domain.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Calculator";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Метод для регистрации нового пользователя
    public boolean signUpUser(Users user) {
        String insert = "INSERT INTO Users(username, password, role) VALUES(?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для проверки существования пользователя
    public boolean userExists(String username) {
        String query = "SELECT count(*) FROM Users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) >= 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && password.equals(rs.getString("password"));
        }
    }


    public int getUserId(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("User not found");
        }
    }

    public List<OhmLawCalculation> getOhmLawHistory(int userId) throws SQLException {
        List<OhmLawCalculation> history = new ArrayList<>();
        String sql = "SELECT id, voltage, current, resistance, calculated_value, user_id " +
                "FROM ohmlawcalculation WHERE user_id = ? ORDER BY id DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OhmLawCalculation calc = new OhmLawCalculation();
                calc.setId(rs.getInt("id"));
                calc.setVoltage(rs.getDouble("voltage"));
                calc.setCurrent(rs.getDouble("current"));
                calc.setResistance(rs.getDouble("resistance"));
                calc.setCalculatedValue(rs.getString("calculated_value")); // Важно использовать правильное имя столбца
                calc.setUserId(rs.getInt("user_id"));
                history.add(calc);
            }
        }
        return history;
    }

    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws SQLException {
        String sql = "SELECT id, fromCurrency, toCurrency, rate FROM exchangerate " +
                "WHERE fromCurrency = ? AND toCurrency = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fromCurrency);
            stmt.setString(2, toCurrency);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ExchangeRate rate = new ExchangeRate();
                rate.setId(rs.getInt("id"));
                rate.setFromCurrency(rs.getString("fromCurrency"));
                rate.setToCurrency(rs.getString("toCurrency"));
                rate.setRate(rs.getDouble("rate"));
                return rate;
            }
            return null;
        }
    }

    public void saveCurrencyConversion(CurrencyConversion conversion) throws SQLException {

        if (conversion.getUserIdd() == null) {
            throw new IllegalArgumentException("User ID cannot be null"); // Проверка на null
        }

        String sql = "INSERT INTO currencyconversion (fromcurrency, tocurrency, amount, " +
                "result, userid, exchangerateid) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conversion.getFromCurrency());
            stmt.setString(2, conversion.getToCurrency());
            stmt.setDouble(3, conversion.getAmount());
            stmt.setDouble(4, conversion.getResult());
            stmt.setInt(5, conversion.getUserIdd());
            stmt.setInt(6, conversion.getExchangeRateId());

            stmt.executeUpdate();
        }
    }

    public List<CurrencyConversion> getCurrencyConversionHistory(int userId) throws SQLException {
        List<CurrencyConversion> history = new ArrayList<>();
//        String sql = "SELECT id, fromcurrency, tocurrency, amount, result, exchangerateid " +
//                "FROM currencyconversion WHERE userid = ?";

        String sql = "SELECT cc.id, cc.fromcurrency, cc.tocurrency, cc.amount, " +
                "cc.result, cc.exchangerateid, u.username " +
                "FROM currencyconversion cc " +
                "JOIN users u ON cc.userid = u.id " +
                "WHERE cc.userid = ? ";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CurrencyConversion conversion = new CurrencyConversion();
                conversion.setUsername(rs.getString("username"));
                conversion.setId(rs.getInt("id"));
                conversion.setFromCurrency(rs.getString("fromcurrency"));
                conversion.setToCurrency(rs.getString("tocurrency"));
                conversion.setAmount(rs.getDouble("amount"));
                conversion.setResult(rs.getDouble("result"));
                conversion.setUserIdd(userId);
                conversion.setExchangeRateId(rs.getInt("exchangerateid"));
                history.add(conversion);
            }
        }
        return history;
    }

    public Users getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }
        return null;
    }

    public List<Users> getAllUsers() throws SQLException {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void updateUser(Users user) throws SQLException {
        String sql = "UPDATE users SET username = ?, role = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        }
    }

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        List<ExchangeRate> rates = new ArrayList<>();
        String sql = "SELECT * FROM exchangerate";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ExchangeRate rate = new ExchangeRate();
                rate.setId(rs.getInt("id"));
                rate.setFromCurrency(rs.getString("fromCurrency"));
                rate.setToCurrency(rs.getString("toCurrency"));
                rate.setRate(rs.getDouble("rate"));
                rates.add(rate);
            }
        }
        return rates;
    }

    public void updateExchangeRate(ExchangeRate rate) throws SQLException {
        String sql = "UPDATE exchangerate SET rate = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, rate.getRate());
            stmt.setInt(2, rate.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteExchangeRate(int id) throws SQLException {
        String sql = "DELETE FROM exchangerate WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void saveExchangeRate(ExchangeRate rate) throws SQLException {
        String sql = "INSERT INTO exchangerate (fromCurrency, toCurrency, rate) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rate.getFromCurrency());
            stmt.setString(2, rate.getToCurrency());
            stmt.setDouble(3, rate.getRate());
            stmt.executeUpdate();
        }
    }


}