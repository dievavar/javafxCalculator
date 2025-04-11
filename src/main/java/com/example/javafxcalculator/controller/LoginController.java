package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            Users user = databaseHandler.getUser(usernameField.getText(), passwordField.getText());
            if (user != null) {
                // Загружаем главное меню
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/main.fxml"));
                Parent root = loader.load();

                // Устанавливаем пользователя в MainController
                MainController mainController = loader.getController();
                mainController.setCurrentUser(user);

                // Открываем главное меню
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Главное меню");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        try {
            // 1. Проверка аутентификации
            if (!databaseHandler.validateUser(username, password)) {
                errorLabel.setText("Неверные учетные данные");
                return;
            }

            // 2. Получение ID пользователя
            int userId = databaseHandler.getUserId(username);
            System.out.println("User ID получен: " + userId); // Отладка

            // 3. Загрузка главного окна
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/main.fxml"));
            Parent root = loader.load();

            // 4. Передача данных в MainController
            MainController mainController = loader.getController();
            mainController.setCurrentUserId(userId); // Критически важно!
            mainController.setCurrentUser(username);

            // 5. Переход на главное окно
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Главное меню");

        } catch (SQLException e) {
            errorLabel.setText("Ошибка базы данных: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            errorLabel.setText("Ошибка загрузки интерфейса");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSignUp() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxcalculator/signup.fxml"));

            Stage stage = (Stage) usernameField.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Регистрация");
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Ошибка загрузки формы регистрации");
        }
    }

}