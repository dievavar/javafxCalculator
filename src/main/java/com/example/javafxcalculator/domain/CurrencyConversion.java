package com.example.javafxcalculator.domain;

public class CurrencyConversion {
    private Integer id;
    private String fromCurrency;
    private String toCurrency;
    private Double amount;
    private Double result;
    private Integer currentUserId;
    private Integer exchangeRateId;
    private String username;

    // Конструкторы
    public CurrencyConversion() {
    }

    public CurrencyConversion(String fromCurrency, String toCurrency, Double amount, Double result, Integer currentUserId, Integer exchangeRateId) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.result = result;
        this.currentUserId = currentUserId;
        this.exchangeRateId = exchangeRateId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserIdd() {
        return currentUserId;
    }

    public void setUserIdd(Integer userId) {
        this.currentUserId = userId;
    }

    public Integer getExchangeRateId() {
        return exchangeRateId;
    }

    public void setExchangeRateId(Integer exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }
}