package com.dz.finansist.api;

import com.dz.finansist.utils.AuthorizationController;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthenticateApi {
    private AuthorizationController authController = new AuthorizationController();


    public boolean authenticateUser(String username, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Создаем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(username, password)))
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Обрабатываем ответ
            if (response.statusCode() == 200) {
                authController = new AuthorizationController();
                authController.setAuthToken(response.body());
                authController.setUserName(username);

                return true; // Авторизация успешна
            } else {
                System.out.println("Error: " + response.statusCode());
                return false; // Авторизация неудачна
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return false;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String createRequestBody(String username, String password) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        return body.toString();
    }
}
