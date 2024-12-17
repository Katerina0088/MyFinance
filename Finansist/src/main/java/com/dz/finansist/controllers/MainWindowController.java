package com.dz.finansist.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {
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
