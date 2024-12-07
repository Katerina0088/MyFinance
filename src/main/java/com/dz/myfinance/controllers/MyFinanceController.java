package com.dz.myfinance.controllers;

import com.dz.myfinance.enums.TransactionType;
import com.dz.myfinance.models.Category;
import com.dz.myfinance.models.Transaction;
import com.dz.myfinance.models.User;
import com.dz.myfinance.repositories.CategoryRepository;
import com.dz.myfinance.repositories.TransactionRepository;
import com.dz.myfinance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/")
public class MyFinanceController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Метод для отображения главной страницы с категориями и транзакциями пользователя
    @GetMapping("/")
    public String homePage(Model model) {
        User currentUser = userService.getCurrentUser();

        if (currentUser != null) {
            List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
            List<Transaction> transactions = transactionRepository.findAllByUserId(currentUser.getId());

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1);
            Date lastMonthStart = cal.getTime();
            Date lastMonthEnd = new Date();


            BigDecimal lastMonthIncome = transactionRepository.sumIncomeByUserAndDateRange(
                    currentUser.getId(),
                    TransactionType.INCOME,
                    lastMonthStart,
                    lastMonthEnd
            );

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("categories", categories);
            model.addAttribute("transactions", transactions);
            model.addAttribute("lastMonthIncome", lastMonthIncome);
        } else {
            model.addAttribute("message", "Please log in to access this page.");
        }
        return "myfinance";
    }

    // Метод для добавления новой категории
    @PostMapping("/add-category")
    public String addCategory(@RequestParam String name, @RequestParam Long userId) {
        Category category = new Category();
        category.setName(name);
        category.setUser(userService.getUserById(userId));
        categoryRepository.save(category);
        return "redirect:/";
    }

    // Метод для удаления категории
    @GetMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/";
    }

    // Метод для редактирования категории
    @PostMapping("/edit-category")
    public String editCategory(@RequestParam Long id, @RequestParam String newName) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category cat = category.get();
            cat.setName(newName);
            categoryRepository.updateCategoryName(cat.getId(), newName);
        }
        return "redirect:/";
    }

    // Метод для добавления новой транзакции
    @PostMapping("/add-transaction")
    public String addTransaction(@RequestParam BigDecimal amount,
                                 @RequestParam String description,
                                 @RequestParam Long categoryId) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        transaction.setCategory(category);
        transaction.setUser(userService.getCurrentUser());
        transactionRepository.save(transaction);
        return "redirect:/";
    }

    // Метод для удаления транзакции
    @GetMapping("/delete-transaction/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
        return "redirect:/";
    }

    // Метод для редактирования транзакции
    @PostMapping("/edit-transaction")
    public String editTransaction(@RequestParam Long id,
                                  @RequestParam BigDecimal newAmount,
                                  @RequestParam String newDescription) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            Transaction tr = transaction.get();
            tr.setAmount(newAmount);
            tr.setDescription(newDescription);
            transactionRepository.save(tr);
        }
        return "redirect:/";
    }
}

