package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.CurrencyConversion;
import com.example.javafxcalculator.domain.ExchangeRate;
import com.example.javafxcalculator.domain.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CurrencyConverterController {
    @FXML private Button adminButton;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> fromCurrencyCombo;
    @FXML private ComboBox<String> toCurrencyCombo;
    @FXML private Label resultLabel;

    private DatabaseHandler dbHandler = new DatabaseHandler();
    private Integer currentUserId;
    private Users currentUser;

    @FXML
    public void initialize() {
        // Инициализация выпадающих списков валют
        fromCurrencyCombo.getItems().addAll("USD", "EUR", "GBP", "JPY", "RUB");
        toCurrencyCombo.getItems().addAll("USD", "EUR", "GBP", "JPY", "RUB");
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setCurrentUser(Users user) {
        this.currentUser = user;
        this.currentUserId = user != null ? user.getId() : null;
        // Скрываем/показываем кнопки админа
        if (adminButton != null) {
            adminButton.setVisible(user != null && user.isAdmin());
        }
    }

    @FXML
    private void convertCurrency() {

        if (currentUserId == null) {
            resultLabel.setText("Ошибка: войдите в систему");
            return;
        }


        try {
            String fromCurrency = fromCurrencyCombo.getValue();
            String toCurrency = toCurrencyCombo.getValue();
            double amount = Double.parseDouble(amountField.getText());

            System.out.println("Начало конвертации: " + fromCurrency + "->" + toCurrency);

            ExchangeRate rate = dbHandler.getExchangeRate(fromCurrency, toCurrency);
            System.out.println("Получен курс: " + (rate != null ? rate.getRate() : "null"));

            if (rate == null) {
                resultLabel.setText("Курс не найден");
                return;
            }

            double result = amount * rate.getRate();
            System.out.println("Результат расчета: " + result);

            CurrencyConversion conversion = new CurrencyConversion(
                    fromCurrency, toCurrency, amount, result, currentUserId, rate.getId()
            );

            dbHandler.saveCurrencyConversion(conversion);

            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, result, toCurrency));

        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата числа: " + e.getMessage());
            resultLabel.setText("Введите корректную сумму");
        } catch (SQLException e) {
            System.out.println("SQL Ошибка:");
            e.printStackTrace();
            resultLabel.setText("Ошибка базы данных: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Это покажет реальную ошибку
            resultLabel.setText("Ошибка: " + e.getMessage());
        }
    }

    @FXML
    public void showHistory() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/javafxcalculator/currency_history.fxml"));
            Parent root = loader.load();

            CurrencyHistoryController controller = loader.getController();
            controller.setCurrentUserId(currentUserId);

            Stage stage = new Stage();
            stage.setTitle("История операций");
            stage.setScene(new Scene(root));
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

            Stage stage = (Stage) amountField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Главное меню");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminPanel() {
        if (currentUser != null && currentUser.isAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/exchange_rate_admin.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Управление курсами");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}