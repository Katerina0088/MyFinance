package com.dz.finansist;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Здесь должна быть логика авторизации
        // Например, вызов метода из AuthController
        boolean isAuthenticated = authenticateUser(username, password);

        if (isAuthenticated) {
            // Успешная авторизация, можно закрыть окно авторизации
            Stage authStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            authStage.close();

            // Здесь можно открыть основное окно приложения
            openMainApplicationWindow();
        } else {
            showErrorMessage("Ошибка авторизации");
        }
    }

    private boolean authenticateUser(String username, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Создаем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.0.15:8080/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(username, password)))
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Обрабатываем ответ
            if (response.statusCode() == 200) {
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

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openMainApplicationWindow() {
        // Здесь можно создать и показать основное окно приложения
        // Например, загрузить новый FXML файл
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRegisterButtonClick(ActionEvent actionEvent) {
    }

    public void onShowLinkMyFinance(ActionEvent actionEvent) {
    }
}



/*public class LoginController  {

    public void initialize() {
        // Инициализация компонентов после загрузки FXML
    }


    @FXML
    public Hyperlink linkMyFinance;

    @FXML
    public Button onLoginButtonClick;


    @FXML
    private Label welcomeText;


    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        welcomeText.setText("Hello, World!");
    }

    @FXML
    public void onShowLinkMyFinance(ActionEvent event) {
        String url = "http://192.168.0.115:8080";
        Platform.runLater(() -> openUrl(url));    }

    private void openUrl(String url) {
        try {
            Runtime.getRuntime().exec("cmd /c start " + url);
        } catch (Exception e) {
            System.out.println("Ошибка при открытии ссылки: " + e.getMessage());
        }
    }

    public void showLoginWindow() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/login-view.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onRegisterButtonClick(ActionEvent actionEvent) {

    }
}*/