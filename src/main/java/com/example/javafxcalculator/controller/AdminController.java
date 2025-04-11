package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.Users;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminController {
    @FXML
    private TableView<Users> usersTable;
    @FXML private TableColumn<Users, Integer> idColumn;
    @FXML private TableColumn<Users, String> usernameColumn;
    @FXML private TableColumn<Users, String> roleColumn;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUsers();
    }

    @FXML
    private void loadUsers() {
        try {
            List<Users> users = dbHandler.getAllUsers();
            usersTable.setItems(FXCollections.observableArrayList(users));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteUser() {
        Users selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                dbHandler.deleteUser(selected.getId());
                loadUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void editUser() {
        Users selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Диалог редактирования
                Dialog<Users> dialog = new Dialog<>();
                dialog.setTitle("Редактирование пользователя");

                // Создаем форму редактирования
                GridPane grid = new GridPane();
                TextField usernameField = new TextField(selected.getUsername());
                ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("admin", "user"));
                roleCombo.setValue(selected.getRole());

                grid.add(new Label("Username:"), 0, 0);
                grid.add(usernameField, 1, 0);
                grid.add(new Label("Role:"), 0, 1);
                grid.add(roleCombo, 1, 1);

                dialog.getDialogPane().setContent(grid);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.setResultConverter(button -> {
                    if (button == ButtonType.OK) {
                        selected.setUsername(usernameField.getText());
                        selected.setRole(roleCombo.getValue());
                        return selected;
                    }
                    return null;
                });

                Optional<Users> result = dialog.showAndWait();
                result.ifPresent(updatedUser -> {
                    try {
                        dbHandler.updateUser(updatedUser);
                        loadUsers();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}