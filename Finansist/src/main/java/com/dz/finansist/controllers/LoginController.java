package com.dz.finansist.controllers;

import com.dz.finansist.api.AuthenticateApi;
import com.dz.finansist.api.CategoriesApi;
import com.dz.finansist.utils.AlertController;
import com.dz.finansist.utils.AuthorizationController;
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
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {
    private AuthenticateApi authenticateApi = new AuthenticateApi();


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        try {


            String username = usernameField.getText();
            String password = passwordField.getText();

            // Здесь должна быть логика авторизации
            // Например, вызов метода из AuthController
            boolean isAuthenticated = authenticateApi.authenticateUser(username, password);

            if (isAuthenticated) {
                // Успешная авторизация, можно закрыть окно авторизации
                Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                authStage.close();

                // Здесь можно открыть основное окно приложения
                openMainApplicationWindow();
            } else {
                AlertController.showAlertInfo("Ошибка", "Ошибка авторизации");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            AlertController.showAlertInfo("Ошибка", "Ошибка открытия главного окна.");
        }
    }

    private void openMainApplicationWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/transactions-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlertInfo("Ошибка", "Ошибка открытия главного окна.");
        }
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
}


