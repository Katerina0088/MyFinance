package com.dz.finansist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserDataResponse {
    @JsonProperty("categories")
    private List<Category> categories;

    @JsonProperty("transactions")
    private List<Transaction> transactions;

    public List<Category> getCategories() {
        return categories;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @JsonProperty("totalIncome")
    private BigDecimal totalIncome;

    @JsonProperty("totalExpenses")
    private BigDecimal totalExpenses;

}
