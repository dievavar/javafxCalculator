package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.domain.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML private Button adminButton;
    private Users currentUser;
    private Integer currentUserId;
    private String username;


    public void setCurrentUserId(Integer currentUserId) {
        if (currentUserId <= 0) throw new IllegalArgumentException("Invalid user ID");
        this.currentUserId = currentUserId;
    }
    public void setCurrentUser(String username) {
        this.username = username;

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
    private void openAdminPanel() {
        if (currentUser != null && currentUser.isAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/admin_panel.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Управления пользователями");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void openOhmLawCalculator(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/ohm_calculator.fxml"));
            Parent root = loader.load();

            // Передаем ID в контроллер калькулятора
            OhmLawController controller = loader.getController();
            controller.setCurrentUserId(this.currentUserId);
            System.out.println("Передаем User ID в калькулятор: " + this.currentUserId);

            // Переход на сцену
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Калькулятор закона Ома");
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка загрузки калькулятора: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openCurrencyConverter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/currency_converter.fxml"));
            Parent root = loader.load();

            CurrencyConverterController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Ключевая строка!

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Вход в систему");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}