package com.dz.myfinance.models;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import java.util.Date;

@Data
@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    private Date date;

    @Column(name = "total_income")
    private BigDecimal totalIncome;

    @Column(name = "total_expenses")
    private BigDecimal totalExpenses;

    @Column(name = "net_savings")
    private BigDecimal netSavings;}