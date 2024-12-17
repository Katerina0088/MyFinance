package com.dz.myfinance.models;
import com.dz.myfinance.enums.TransactionType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = true)
    private String description;

    @Column(name = "transaction_type")
    private TransactionType type;

    @Transient
    private String categoryName;

    @PostLoad
    public void updateCategoryString() {
        if (this.category != null) {
            this.categoryName = this.category.getName();
        } else {
            this.categoryName = null;
        }
    }
}