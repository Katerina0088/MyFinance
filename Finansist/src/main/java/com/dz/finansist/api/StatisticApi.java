package com.dz.finansist.api;

import com.dz.finansist.model.Transaction;
import com.dz.finansist.model.UserDataResponse;
import com.dz.finansist.utils.AuthorizationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StatisticApi {
    public String BASE_URL = ("http://localhost:8080/api/");

    private AuthorizationController authController;

    public List<BigDecimal> getStatistics() {
        try {
            authController = new AuthorizationController();
            String token = authController.getAuthToken();
            HttpClient client = HttpClient.newHttpClient();

            // Создаем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL+"statistics"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)

                    .GET()
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Обрабатываем ответ
            if (response.statusCode() == 200) {
                String resultData = response.body();

                ObjectMapper objectMapper = new ObjectMapper();
                UserDataResponse userDataResponse;
                userDataResponse = objectMapper.readValue(resultData, UserDataResponse.class);
                BigDecimal totalIncome = userDataResponse.getTotalIncome();
                BigDecimal totalExpenses = userDataResponse.getTotalExpenses();

                List<BigDecimal> combinedValues = new ArrayList<>();
                combinedValues.add(totalIncome);
                combinedValues.add(totalExpenses);

                return combinedValues;
            } else {
                System.out.println("Error: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
