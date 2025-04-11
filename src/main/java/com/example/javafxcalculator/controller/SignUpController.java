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

public class SignUpController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        // Заполняем ComboBox ролями
        roleComboBox.getItems().addAll("user", "admin");
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue();

        if (username

                // String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                .isEmpty() || password.isEmpty()) {
            errorLabel.setText("Заполните все поля!");
            return;
        }

        if (databaseHandler.userExists(username)) {
            errorLabel.setText("Пользователь с таким именем уже существует!");
            return;
        }
        Users user = new Users(username, password, role);

        if (databaseHandler.signUpUser(user)) {
            goToLogin();
            errorLabel.setText("Регистрация успешна!");
            errorLabel.setStyle("-fx-text-fill: green;");
            // Очищаем поля
            usernameField.clear();
            passwordField.clear();
        } else {
            errorLabel.setText("Ошибка при регистрации!");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxcalculator/login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Вход в систему");
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Ошибка загрузки формы входа");
        }
    }
}