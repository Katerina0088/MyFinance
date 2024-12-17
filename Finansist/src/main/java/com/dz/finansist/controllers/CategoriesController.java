package com.dz.finansist.controllers;

import com.dz.finansist.model.Category;

import com.dz.finansist.model.Transaction;
import com.dz.finansist.utils.AlertController;
import com.dz.finansist.utils.AuthorizationController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

import com.dz.finansist.api.CategoriesApi;

public class CategoriesController {
    private CategoriesApi categoriesApi = new CategoriesApi();
    private ObservableList<String> observableList;
    private AuthorizationController authController = new AuthorizationController();
    @FXML
    private Label userName;

    @FXML
    private TextField nameCategory;

    @FXML
    private TableView<String> categoriesTable;
    @FXML
    private TableColumn<String, String> categoriesNameColumn;

    @FXML
    private TableColumn<String, String> categoriesActionsColumn;

    @FXML
    public void initialize() {
        userName.setText(authController.getUserName());
        categoriesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        categoriesActionsColumn.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override
            public TableCell<String, String> call(TableColumn<String, String> param) {
                return new TableCell<String, String>() {
                    private final Button deleteButton = new Button("Удалить");

                    {
                        deleteButton.setStyle("-fx-background-color: #ff3030; -fx-text-fill: white;");
                        deleteButton.setOnAction(event -> {
                            String categoryName = getTableView().getItems().get(getIndex());
                            deleteCategories(categoryName);
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });

        observableList = FXCollections.observableArrayList();
        categoriesTable.setItems(observableList);
        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoriesApi.getCategories();
            observableList.clear();

            for (Category category : categories) {
                if (!observableList.contains(category.getName())) {
                    observableList.add(category.getName());
                }
            }
        } catch (Exception e) {
            AlertController.showAlertInfo("Ошибка", "Не удалось загрузить категории");
            e.printStackTrace();
        }
    }

    @FXML
    private void openStatistic(ActionEvent event) {
        try {
            Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            authStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("../fxml/statistic-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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
    public void deleteCategories(String categoryName) {
        try {
            if (categoryName == "" || categoryName == null) {
                AlertController.showAlertInfo("Ошибка", "Выберите категорию для удаления");
                return;
            }

            String responseMessage= categoriesApi.deleteCategories(categoryName);
            AlertController.showAlertInfo("Результат", responseMessage);
            loadCategories();
        } catch (Exception e) {
            AlertController.showAlertInfo("Ошибка", "Не удалось удалить категорию");
            e.printStackTrace();
        }
    }

    @FXML
    public void addCategory() {
        try {

            String categoryName = nameCategory.getText();
            if (categoryName == "" || categoryName == null) {
                AlertController.showAlertInfo("Ошибка", "Напишите категорию для добавления");
                return;
            }

            String responseMessage= categoriesApi.addCategory(categoryName);
            AlertController.showAlertInfo("Результат", responseMessage);
            loadCategories();
        } catch (Exception e) {
            AlertController.showAlertInfo("Ошибка", "Не удалось добавить категорию");
            e.printStackTrace();
        }
    }

}
