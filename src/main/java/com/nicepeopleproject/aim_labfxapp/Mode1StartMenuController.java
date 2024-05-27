package com.nicepeopleproject.aim_labfxapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mode1StartMenuController {

    @FXML
    private ResourceBundle resources;
    private Stage stage;
    @FXML
    private URL location;

    @FXML
    void switchToMode1MainScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mode1_main_scene_view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void switchToModesMenuScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("modes_menu_view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {

    }

}
