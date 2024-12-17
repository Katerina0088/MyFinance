package com.dz.myfinance.dto;

import com.dz.myfinance.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String amount;
    private Date date;
    private String description;
    private TransactionType type;
    private String categoryName;
}