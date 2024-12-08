package com.dz.finansist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class FinansistApplication extends Application{

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void init() throws Exception {

        System.out.println("Application inits");
        super.init();
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            LoginController loginController = new LoginController();

            // Загружаем FXML для окна авторизации
            Parent root = FXMLLoader.load(getClass().getResource("fxml/login-view.fxml"));
            Scene scene = new Scene(root);
            Stage authStage = new Stage();
            authStage.setScene(scene);
            authStage.show();

            // Добавляем обработчик закрытия окна авторизации
            authStage.setOnCloseRequest(e -> System.exit(0));






            //LoginController loginController = new LoginController();

            // Создаем окно авторизации
           //loginController.showLoginWindow();

            // Добавляем обработчик закрытия окна
            //Stage stage = (Stage)((Parent)primaryStage.getScene().getRoot()).getScene().getWindow();
            //stage.setOnCloseRequest(event -> System.exit(0));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {

        System.out.println("Application stops");
        super.stop();
    }
}