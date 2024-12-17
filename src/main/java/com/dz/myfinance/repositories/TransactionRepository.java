package com.dz.myfinance.repositories;

import com.dz.myfinance.dto.TransactionDto;
import com.dz.myfinance.enums.TransactionType;
import com.dz.myfinance.models.Transaction;
import com.dz.myfinance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);


    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user")
    long countTransactionsByUser(User user);


    List<Transaction> findByCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Transaction t SET t.description = :description WHERE t.id = :id")
    int updateDescription(@Param("id") Long id, @Param("description") String description);

    // Method to sum expenses by user and date range

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    BigDecimal sumAmountByUserAndType(@Param("userId") Long userId, @Param("type") TransactionType type);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.id = :userId AND t.date >= :startDate AND t.date < :endDate")
    long countTransactionsByDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :transactionType AND t.date >= :startDate AND t.date < :endDate")
    BigDecimal sumIncomeByUserAndDateRange(@Param("userId") Long userId,
                                           @Param("transactionType") TransactionType transactionType,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal sumExpensesByUserAndDateRange(@Param("userId") Long userId,
                                             @Param("type") TransactionType type,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);
}