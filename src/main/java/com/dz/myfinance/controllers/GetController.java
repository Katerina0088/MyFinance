package com.dz.myfinance.controllers;

import com.dz.myfinance.dto.TransactionDto;
import com.dz.myfinance.enums.TransactionType;
import com.dz.myfinance.models.Category;
import com.dz.myfinance.models.MonthlyStatistics;
import com.dz.myfinance.models.Transaction;
import com.dz.myfinance.models.User;
import com.dz.myfinance.repositories.CategoryRepository;
import com.dz.myfinance.repositories.TransactionRepository;
import com.dz.myfinance.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class GetController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/all-data1")
    public ResponseEntity<Map<String, Object>> getAllUserData() {
        User currentUser = userService.getCurrentUser();


        Map<String, Object> responseData = new HashMap<>();

        // Получаем все категории пользователя
        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        responseData.put("categories", categories);

        // Получаем все транзакции пользователя
        List<Transaction> transactions = transactionRepository.findAllByUserId(currentUser.getId());
        responseData.put("transactions", transactions);

        // Статистика по доходам и расходам за текущий месяц
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        BigDecimal totalIncome = transactionRepository.sumIncomeByUserAndDateRange(
                currentUser.getId(),
                TransactionType.ДОХОД,
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );
        responseData.put("totalIncome", totalIncome);

        BigDecimal totalExpenses = transactionRepository.sumExpensesByUserAndDateRange(
                currentUser.getId(),
                TransactionType.РАСХОД,
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );
        responseData.put("totalExpenses", totalExpenses);


        // Статистика по категориям
        List<MonthlyStatistics> monthlyStatistics = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = LocalDate.now().minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).withDayOfMonth(1);


            BigDecimal monthlyIncome = Optional.ofNullable(transactionRepository.sumIncomeByUserAndDateRange(
                    currentUser.getId(),
                    TransactionType.ДОХОД,
                    java.sql.Date.valueOf(monthStart),
                    java.sql.Date.valueOf(monthEnd)
            )).orElse(BigDecimal.ZERO);


            BigDecimal monthlyExpenses = Optional.ofNullable(transactionRepository.sumExpensesByUserAndDateRange(
                    currentUser.getId(),
                    TransactionType.РАСХОД,
                    java.sql.Date.valueOf(monthStart),
                    java.sql.Date.valueOf(monthEnd)
            )).orElse(BigDecimal.ZERO);
            monthlyStatistics.add(new MonthlyStatistics(monthStart.getMonth().toString(), monthlyIncome, monthlyExpenses));


            responseData.put("monthlyStatistics", monthlyStatistics);

        }
        return ResponseEntity.ok(responseData);
}
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategory() {
        User currentUser = userService.getCurrentUser();


        Map<String, Object> responseData = new HashMap<>();

        // Получаем все категории пользователя
        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        responseData.put("categories", categories);
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/transactions")
    public ResponseEntity<Map<String, Object>> getTransactions() throws JsonProcessingException {
        try {


        User currentUser = userService.getCurrentUser();


        Map<String, Object> responseData = new HashMap<>();

        // Получаем все категории пользователя
            List<Transaction> transactions = transactionRepository.findAllByUserId(currentUser.getId());
            List<TransactionDto> dtoTransactions = transactions.stream()
                    .map(t -> new TransactionDto(
                            t.getId(),
                            (t.getAmount()).toString(),
                            t.getDate(),
                            t.getDescription(),
                            t.getType(),
                            t.getCategoryName()
                    ))
                    .collect(Collectors.toList());
            // Явно сериализуем список транзакций с включением поля categoryName

            responseData.put("transactions", dtoTransactions);
            return ResponseEntity.ok(responseData);}
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        try {
            transactionRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Транзакция успешно удалена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении транзакции");
        }
    }

}