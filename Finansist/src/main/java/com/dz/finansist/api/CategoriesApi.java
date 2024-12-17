package com.dz.finansist.api;

import com.dz.finansist.model.Category;
import com.dz.finansist.model.UserDataResponse;
import com.dz.finansist.utils.AuthorizationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CategoriesApi {
    private AuthorizationController authController;
    public String BASE_URL = ("http://localhost:8080/api/");

    public   List<Category> getCategories() {
        try {
            authController = new AuthorizationController();
            String token = authController.getAuthToken();
            HttpClient client = HttpClient.newHttpClient();

            // Создаем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/categories"))
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
                UserDataResponse resultCatigories = objectMapper.readValue(resultData, UserDataResponse.class);
                List<Category> categories = resultCatigories.getCategories();
                return categories;
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


    public String deleteCategories(String categoryName) throws IOException {
        try {

            authController = new AuthorizationController();

            String token = authController.getAuthToken();
            HttpClient client = HttpClient.newHttpClient();

            // Создаем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "categories/" + categoryName))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)

                    .DELETE()
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                return "Успех";
            } else {
                System.out.println("Error: " + response.statusCode());
                return "Не успех";
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public  String addCategory(String categoryName) throws IOException {
        try {
            authController = new AuthorizationController();
            String token = authController.getAuthToken();
            HttpClient client = HttpClient.newHttpClient();

            // Создаем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "categories"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)

                    .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"" + categoryName + "\"}"))
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                return "Успех";
            } else {
                System.out.println("Error: " + response.statusCode());
                return "Не успех";
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
