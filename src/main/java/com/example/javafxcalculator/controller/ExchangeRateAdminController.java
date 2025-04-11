package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.ExchangeRate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateAdminController {
    @FXML
    private TableView<ExchangeRate> ratesTable;
    @FXML private TableColumn<ExchangeRate, Integer> idColumn;
    @FXML private TableColumn<ExchangeRate, String> fromColumn;
    @FXML private TableColumn<ExchangeRate, String> toColumn;
    @FXML private TableColumn<ExchangeRate, Double> rateColumn;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromCurrency"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toCurrency"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));

        loadRates();
    }

    @FXML
    private void loadRates() {
        try {
            List<ExchangeRate> rates = dbHandler.getAllExchangeRates();
            ratesTable.setItems(FXCollections.observableArrayList(rates));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editRate() {
        ExchangeRate selected = ratesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog dialog = new TextInputDialog(selected.getRate().toString());
            dialog.setTitle("Редактирование курса валют");
            dialog.setHeaderText("Edit rate for " + selected.getFromCurrency() + " to " + selected.getToCurrency());
            dialog.setContentText("New rate:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newRate -> {
                try {
                    selected.setRate(Double.parseDouble(newRate));
                    dbHandler.updateExchangeRate(selected);
                    loadRates();
                } catch (NumberFormatException | SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void deleteRate() {
        ExchangeRate selected = ratesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                dbHandler.deleteExchangeRate(selected.getId());
                loadRates();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addRate() {
        Dialog<ExchangeRate> dialog = new Dialog<>();
        dialog.setTitle("Add New Exchange Rate");

        GridPane grid = new GridPane();
        TextField fromField = new TextField();
        TextField toField = new TextField();
        TextField rateField = new TextField();

        grid.add(new Label("From Currency:"), 0, 0);
        grid.add(fromField, 1, 0);
        grid.add(new Label("To Currency:"), 0, 1);
        grid.add(toField, 1, 1);
        grid.add(new Label("Rate:"), 0, 2);
        grid.add(rateField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                ExchangeRate rate = new ExchangeRate();
                rate.setFromCurrency(fromField.getText().toUpperCase());
                rate.setToCurrency(toField.getText().toUpperCase());
                rate.setRate(Double.parseDouble(rateField.getText()));
                return rate;
            }
            return null;
        });

        Optional<ExchangeRate> result = dialog.showAndWait();
        result.ifPresent(rate -> {
            try {
                dbHandler.saveExchangeRate(rate);
                loadRates();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}