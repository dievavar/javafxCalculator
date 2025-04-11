package com.example.javafxcalculator.controller;
import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.OhmLawCalculation;
import com.example.javafxcalculator.service.OhmLawService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class OhmLawController {
    @FXML private TextField voltageField;
    @FXML private TextField currentField;
    @FXML private TextField resistanceField;
    @FXML private Label resultLabel;
    @FXML private Label errorLabel;

    private int currentUserId;
    private OhmLawService ohmLawService = new OhmLawService();

    public void setCurrentUserId(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("Invalid user ID");
        this.currentUserId = userId;
        System.out.println("OhmLawController: User ID получен - " + userId);
    }


    @FXML
    private void calculate() {
        try {
            // Проверка авторизации
            if (currentUserId <= 0) {
                errorLabel.setText("Ошибка: требуется авторизация");
                return;
            }

            // Проверка заполненных полей
            int filledFields = 0;
            if (!voltageField.getText().isEmpty()) filledFields++;
            if (!currentField.getText().isEmpty()) filledFields++;
            if (!resistanceField.getText().isEmpty()) filledFields++;

            if (filledFields != 2) {
                errorLabel.setText("Заполните ровно 2 поля!");
                return;
            }

            // Парсинг значений
            Double voltage = parseField(voltageField.getText());
            Double current = parseField(currentField.getText());
            Double resistance = parseField(resistanceField.getText());

            // Расчет недостающего параметра
            OhmLawCalculation calculation = new OhmLawCalculation();
            calculation.setUserId(currentUserId); // Устанавливаем ID пользователя

            if (voltage == null) {
                voltage = current * resistance;
                calculation.setCalculatedValue("V");
                resultLabel.setText(String.format("Напряжение: %.2f V", voltage));
            }
            else if (current == null) {
                current = voltage / resistance;
                calculation.setCalculatedValue("I");
                resultLabel.setText(String.format("Ток: %.2f A", current));
            }
            else {
                resistance = voltage / current;
                calculation.setCalculatedValue("R");
                resultLabel.setText(String.format("Сопротивление: %.2f Ω", resistance));
            }

            // Сохранение в БД
            calculation.setVoltage(voltage);
            calculation.setCurrent(current);
            calculation.setResistance(resistance);

            ohmLawService.saveCalculation(calculation);
            errorLabel.setText("");

        } catch (NumberFormatException e) {
            errorLabel.setText("Введите числовые значения!");
        } catch (SQLException e) {
            errorLabel.setText("Ошибка сохранения расчета");
            e.printStackTrace();
        }
    }

    private Double parseField(String value) {
        return value.isEmpty() ? null : Double.parseDouble(value);
    }

    @FXML
    private void showHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/ohm_history.fxml"));
            Parent root = loader.load();

            OhmHistoryController controller = loader.getController();
            controller.setCurrentUserId(currentUserId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("История расчетов");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) voltageField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Главное меню");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}