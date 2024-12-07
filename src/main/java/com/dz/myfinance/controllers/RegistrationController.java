package com.dz.myfinance.controllers;



import com.dz.myfinance.models.User;
import com.dz.myfinance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String showRegPage(Model model) {

        model.addAttribute("headerHidde", true );
        return "register";
    }

    @PostMapping
    public String registerUser(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password

    ) {
        // Проверяем, что пользователь хочет зарегистрироваться
        if (username.equals("!@") && password.equals("!@")) {
            return "redirect:/login";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);


        // Сохраняем пользователя
        if (userService.saveUser(user)) {
            return "redirect:/login";
        } else {
            return "redirect:/register";
        }
    }}
