package com.dz.myfinance.controllers.api;

import com.dz.myfinance.models.User;
import com.dz.myfinance.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test/login")
public class ApiLoginController {

    UserRepository userRepository ;

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<Map<String, Object>> processLogin(@RequestBody String username,
                                                            @RequestBody  String password) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            response.put("error", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        response.put("success", true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
