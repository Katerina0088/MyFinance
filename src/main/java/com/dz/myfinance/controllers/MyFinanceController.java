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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
                    TransactionType.ДОХОД,
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

    public String addTransaction(Model model)
    {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        List<TransactionType> types = Arrays.asList(TransactionType.values());
        model.addAttribute("types", types);
        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        model.addAttribute("categories", categories);
        return "addTransaction"; // Возвращает имя HTML-шаблона
    }


    @GetMapping("/edit-transaction/{id}")
    public String editTransaction(@PathVariable Long id, Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        List<TransactionType> types = Arrays.asList(TransactionType.values());
        model.addAttribute("types", types);
        model.addAttribute("categories", categories);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));
        model.addAttribute("transaction", transaction);
        return "editTransaction";
    }

    @PostMapping("/edit-transaction")
    public String editTransaction(@RequestParam Long id,
                                  @RequestParam BigDecimal amount,
                                  @RequestParam String description,
                                  @RequestParam Long categoryId,
                                  @RequestParam String type,
                                  @RequestParam Date date,
                                  Model model) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));

        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setDescription(description);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        transaction.setCategory(category);

        // Убедимся, что тип транзакции корректен
        if (!type.equals(transaction.getType())) {
            transaction.setType(TransactionType.valueOf(type));
        }

        transactionRepository.save(transaction);

        // Рендеринг списка всех транзакций после редактирования
        model.addAttribute("transactions", transactionRepository.findAll());
        return "redirect:/"; // Шаблон для отображения обновленного списка
    }

    @PostMapping("/add-transaction")
    public String addTransaction(@RequestParam BigDecimal amount,
                                  @RequestParam String description,
                                  @RequestParam Long categoryId,
                                  @RequestParam String type,
                                 @RequestParam String dateString,
                                 @RequestParam Long user,
                                  Model model) throws ParseException {
        Transaction transaction = new Transaction();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateString);
        transaction.setDate(date);
        transaction.setAmount(amount);
        transaction.setUser(userService.getUserById(user));
        transaction.setDescription(description);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        transaction.setCategory(category);

        // Убедимся, что тип транзакции корректен
        if (!type.equals(transaction.getType())) {
            transaction.setType(TransactionType.valueOf(type));
        }

        transactionRepository.save(transaction);

        // Рендеринг списка всех транзакций после редактирования
        model.addAttribute("transactions", transactionRepository.findAll());
        return "redirect:/"; // Шаблон для отображения обновленного списка
    }

    @PostMapping("/delete-transaction/{id}")
    public String deleteTransaction(@PathVariable Long id, Model model) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("не найден"));
        transactionRepository.delete(transaction);
        return "redirect:/";
    }



    @GetMapping("/add-category")

    public String addCategory(Model model)
    {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        model.addAttribute("categories", categories);
        return "addCategory"; // Возвращает имя HTML-шаблона
    }

    @GetMapping("/edit-category/{id}")
    public String editCategory(@PathVariable Long id, Model model) {

        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        model.addAttribute("categories", categories);
        model.addAttribute("currentUser", currentUser);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        model.addAttribute("category", category);
        return "editCategory";
    }





    //категории

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







