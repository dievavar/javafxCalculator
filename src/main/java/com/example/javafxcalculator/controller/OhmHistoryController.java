package com.example.javafxcalculator.controller;

import com.example.javafxcalculator.DB.DatabaseHandler;
import com.example.javafxcalculator.domain.CurrencyConversion;
import com.example.javafxcalculator.domain.OhmLawCalculation;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OhmHistoryController {
    @FXML
    private TableView<OhmLawCalculation> historyTable;
    @FXML private TableColumn<OhmLawCalculation, String> usernameColumn;
    @FXML private TableColumn<OhmLawCalculation, Double> voltageColumn;
    @FXML private TableColumn<OhmLawCalculation, Double> currentColumn;
    @FXML private TableColumn<OhmLawCalculation, Double> resistanceColumn;
    @FXML private TableColumn<OhmLawCalculation, String> resultColumn;


    private int currentUserId;
    private DatabaseHandler dbHandler = new DatabaseHandler();

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        loadHistory();
    }

    public void loadHistory() {
        try {
            List<OhmLawCalculation> history = dbHandler.getOhmLawHistory(currentUserId);


            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            voltageColumn.setCellValueFactory(new PropertyValueFactory<>("voltage"));
            currentColumn.setCellValueFactory(new PropertyValueFactory<>("current"));
            resistanceColumn.setCellValueFactory(new PropertyValueFactory<>("resistance"));
            resultColumn.setCellValueFactory(new PropertyValueFactory<>("calculatedValue"));

            historyTable.setItems(FXCollections.observableArrayList(history));
            historyTable.refresh(); // Явное обновление таблицы

        } catch (SQLException e) {
            System.err.println("Ошибка загрузки истории: ");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxcalculator/ohm_history.fxml"));
            Parent root = loader.load();

            OhmLawController controller = loader.getController();
            controller.setCurrentUserId(currentUserId);

            Stage stage = (Stage) historyTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}