package com.example.javafxcalculator.domain;

import java.time.LocalDateTime;

public class OhmLawCalculation {
    private Integer id;
    private Double voltage;
    private Double current;
    private Double resistance;
    private String calculatedValue;
    private Integer userId;
    private String username;

    public OhmLawCalculation() {}

    public OhmLawCalculation(Double voltage, Double current, Double resistance,
                             String calculatedValue, Integer userId) {
        this.voltage = voltage;
        this.current = current;
        this.resistance = resistance;
        this.calculatedValue = calculatedValue;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getResistance() {
        return resistance;
    }

    public void setResistance(Double resistance) {
        this.resistance = resistance;
    }

    public String getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(String calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}