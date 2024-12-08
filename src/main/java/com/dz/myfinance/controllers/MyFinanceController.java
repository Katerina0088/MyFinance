package com.dz.myfinance.controllers;

import com.dz.myfinance.enums.TransactionType;
import com.dz.myfinance.models.Category;
import com.dz.myfinance.models.Transaction;
import com.dz.myfinance.models.User;
import com.dz.myfinance.repositories.CategoryRepository;
import com.dz.myfinance.repositories.TransactionRepository;
import com.dz.myfinance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

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

    @GetMapping("/add-transaction")
    public String addTransaction(Model model) {
        return "addTransaction"; // Возвращает имя HTML-шаблона
    }

    @GetMapping("/edit-transaction/{id}")
    public String editTransaction(@PathVariable Long id, Model model) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));
        model.addAttribute("transaction", transaction);
        return "editTransaction";
    }


    @GetMapping("/add-category")

    public String addCategory(Model model)
    {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "addCategory"; // Возвращает имя HTML-шаблона
    }

    @GetMapping("/edit-category/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        model.addAttribute("category", category);
        return "editCategory";
    }



    // Метод для добавления новой категории

    @PostMapping("/add-category")
    public String addCategory(@RequestParam String name, @RequestParam Long user, Model model) {
        Category category = new Category();
        category.setName(name);
        category.setUser(userService.getUserById(user)); // Set the user for the category
        categoryRepository.save(category); // Save the category, ID will be generated automatically
        return "redirect:/"; // Redirect to the desired page after adding
    }

    @PostMapping("/delete-category/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("не найден"));
        categoryRepository.delete(category);
        return "redirect:/";
    }
    // Метод для редактирования категории

    @PostMapping("/edit-category")
    public String saveEditedCategory(@RequestParam Long id, @RequestParam String name, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("не найдена"));
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/";
    }
}







