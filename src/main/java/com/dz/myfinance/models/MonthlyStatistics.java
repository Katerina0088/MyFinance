package com.dz.myfinance.models;

import java.math.BigDecimal;

public class MonthlyStatistics {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal cleanSavings;

    public MonthlyStatistics(String month, BigDecimal income, BigDecimal expenses) {
        this.month = month;
        this.income = (income != null) ? income : BigDecimal.ZERO; // Handle null income
        this.expenses = (expenses != null) ? expenses : BigDecimal.ZERO; // Handle null expenses
        this.cleanSavings = this.income.subtract(this.expenses); // Calculate clean savings
    }

    // Getters and setters
    public String getMonth() {
        return month;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public BigDecimal getCleanSavings() {
        return cleanSavings;
    }
}