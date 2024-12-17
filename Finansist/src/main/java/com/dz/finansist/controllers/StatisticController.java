package com.dz.finansist.controllers;

import com.dz.finansist.api.CategoriesApi;
import com.dz.finansist.api.StatisticApi;
import com.dz.finansist.utils.AuthorizationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class StatisticController {
    private StatisticApi statisticsApi = new StatisticApi();

    private AuthorizationController authController = new AuthorizationController();
    private final ObservableMap<String, Double> map = FXCollections.observableHashMap();

    @FXML
    private PieChart statPieChart;

    @FXML
    private Label userName;

    @FXML
    public void initialize() {
        try {
            List<BigDecimal> array =  statisticsApi.getStatistics();
            ObservableList<PieChart.Data> statistData = FXCollections.observableArrayList();
            userName.setText(authController.getUserName());

            map.put("Доход",  array.get(0).doubleValue());
            map.put("Расход",  array.get(1).doubleValue());

            for (Map.Entry<String, Double> entry : map.entrySet()) {
                statistData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }


            statPieChart.setData(statistData);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTransaction(ActionEvent event) {
        try {
            Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            authStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("../fxml/transactions-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCategories(ActionEvent event) {
        try {
            Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            authStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("../fxml/categories-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
