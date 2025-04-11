package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.CurrencyConversion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CurrencyHistoryController {
    @FXML
    private TableView<CurrencyConversion> historyTable;
    @FXML private TableColumn<CurrencyConversion, String> usernameColumn;
    @FXML private TableColumn<CurrencyConversion, String> fromColumn;
    @FXML private TableColumn<CurrencyConversion, String> toColumn;
    @FXML private TableColumn<CurrencyConversion, Double> amountColumn;
    @FXML private TableColumn<CurrencyConversion, Double> resultColumn;
    private int currentUserId;
    private DatabaseHandler dbHandler = new DatabaseHandler();


    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        loadHistory();
    }

    private void loadHistory() {
        try {

            List<CurrencyConversion> history = dbHandler.getCurrencyConversionHistory(currentUserId);

            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromCurrency"));
            toColumn.setCellValueFactory(new PropertyValueFactory<>("toCurrency"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

            historyTable.setItems(FXCollections.observableArrayList(history));
            historyTable.refresh();

        } catch (SQLException e) {
            System.err.println("Ошибка загрузки истории: ");
            e.printStackTrace();
        }
    }


    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/currency_converter.fxml"));
            Parent root = loader.load();

            CurrencyConverterController controller = loader.getController();
            controller.setCurrentUserId(currentUserId);

            Stage stage = (Stage) historyTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
}}
