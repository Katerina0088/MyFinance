package com.dz.finansist.controllers;

import com.dz.finansist.api.CategoriesApi;
import com.dz.finansist.api.TransactionsApi;
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
import com.dz.finansist.utils.AlertController;

import java.io.IOException;
import java.util.List;

public class TransactionsController {
    private AuthorizationController authController = new AuthorizationController();

    private TransactionsApi transactionsApi = new TransactionsApi();
    private ObservableList<Transaction> observableList;

    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, String> transactionsType;
    @FXML
    private TableColumn<Transaction, String> transactionsDescription;
    @FXML
    private TableColumn<Transaction, String> transactionsDate;
    @FXML
    private TableColumn<Transaction, String> categoriesActionsColumn;
    @FXML
    private TableColumn<Transaction, String> transactionsSum;
    @FXML
    private TableColumn<Transaction, String> transactionsCategory;

    @FXML
    private Label userName;

    @FXML
    public void initialize() {
        userName.setText(authController.getUserName());

        observableList = FXCollections.observableArrayList();
        transactionsTable.setItems(observableList);


        transactionsType.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getType())));
        transactionsDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        transactionsDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        transactionsSum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount()));
        transactionsCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryName() != null ? cellData.getValue().getCategoryName() : "Без категории"));

        categoriesActionsColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Удалить"));
        categoriesActionsColumn.setCellFactory(col -> new TableCell<Transaction, String>() {
            private final Button deleteButton = new Button("Удалить");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                    deleteButton.setOnAction(event -> {
                        Transaction transaction = getTableView().getItems().get(getIndex());
                        deleteTransaction(transaction);
                    });
                }
            }
        });


        loadTransactions();
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionsApi.getTransactions();

            observableList.clear();
            observableList.addAll(transactions);
        } catch (Exception e) {
            AlertController.showAlertInfo("Ошибка", "Не удалось загрузить транзакции");
            e.printStackTrace();
        }
    }


    @FXML
    public void deleteTransaction(Transaction transaction) {
        try {
            if (transaction == null) {
                    AlertController.showAlertInfo("Ошибка", "Выберите транзакцию для удаления");
                    return;
                }

                String responseMessage= transactionsApi.deleteTransaction( transaction.getId());
                AlertController.showAlertInfo("Результат", responseMessage);
                loadTransactions();
            } catch (Exception e) {
                AlertController.showAlertInfo("Ошибка", "Не удалось удалить транзакцию");
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

    @FXML
    private void openStatistic(ActionEvent event) {
        try {
            Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            authStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("../fxml/tables-statistic.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

