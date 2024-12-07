package com.dz.myfinance.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "financial_goals")
public class FinancialGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goal_type")
    private String type;

    private BigDecimal targetAmount;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "user_id") // This should match your database schema
    private User user;}