package com.example.javafxcalculator.service;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.OhmLawCalculation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OhmLawService {
    public void saveCalculation(OhmLawCalculation calculation) throws SQLException {
        String sql = "INSERT INTO ohmlawcalculation (voltage, current, resistance, calculated_value, user_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, calculation.getVoltage());
            stmt.setDouble(2, calculation.getCurrent());
            stmt.setDouble(3, calculation.getResistance());
            stmt.setString(4, calculation.getCalculatedValue());
            stmt.setInt(5, calculation.getUserId());

            stmt.executeUpdate();
            System.out.println("Расчет сохранен для user_id=" + calculation.getUserId());
        }
    }

    // Получение истории расчетов пользователя
    public List<OhmLawCalculation> getUserHistory(int userId) throws SQLException {
        List<OhmLawCalculation> history = new ArrayList<>();
        String sql = "SELECT olc.id, olc.voltage, olc.current, olc.resistance, " +
                "olc.calculated_value, olc.user_id, u.username " +
                "FROM ohmlawcalculation olc " +
                "JOIN users u ON olc.user_id = u.id " +
                "WHERE olc.user_id = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(sql)) {
            stmt1.setInt(1, userId);
            ResultSet rs = stmt1.executeQuery();

            while (rs.next()) {
                OhmLawCalculation calc = new OhmLawCalculation();
                calc.setUsername(rs.getString("username"));
                calc.setId(rs.getInt("id"));
                calc.setVoltage(rs.getDouble("voltage"));
                calc.setCurrent(rs.getDouble("current"));
                calc.setResistance(rs.getDouble("resistance"));
                calc.setCalculatedValue(rs.getString("calculated_value"));
//                calc.setUserId(rs.getInt("user_id"));
                calc.setUserId(userId);
                history.add(calc);
            }
        }
        return history;
    }
}