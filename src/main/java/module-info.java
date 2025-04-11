module com.example.javafxcalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.javafxcalculator to javafx.fxml;
    exports com.example.javafxcalculator;
    exports com.example.javafxcalculator.domain;
    opens com.example.javafxcalculator.domain to javafx.fxml;
    exports com.example.javafxcalculator.controller;
    opens com.example.javafxcalculator.controller to javafx.fxml;
    exports com.example.javafxcalculator.DB;
    opens com.example.javafxcalculator.DB to javafx.fxml;
}