package com.nicepeopleproject.aim_labfxapp;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Mode1MainController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    private Timeline gameTimer;
    private Stage stage;
    @FXML
    private Button button;
    @FXML
    private Button button1;
    private int score1 = 0;
    private int hardTime;
    private int ballsize;
    private int timeCount = 0;
    @FXML
    private Label timeLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private ComboBox<String> modeComboBox;
    private Point mouseCoordinates;
    private boolean isMouseInButton = false;

    @FXML
    void switchToMode1StartMenuScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mode1_start_menu_view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        startgame();
        mouseCoordinates = new Point(0, 0);
        button.setText("");
        button.setStyle("-fx-background-color: red; " + /* Цвет кнопки */
                "-fx-text-fill: white; " + /* Цвет текста на кнопке */
                "-fx-background-radius: 50; " + /* Задает радиус скругления кнопки (50% для круглой формы) */
                "-fx-min-width: 100px; " + /* Минимальная ширина кнопки */
                "-fx-min-height: 100px; " + /* Минимальная высота кнопки */
                "-fx-max-width: 100px; " + /* Максимальная ширина кнопки */
                "-fx-max-height: 100px; "); /* Максимальная высота кнопки */
        modeComboBox.getItems().addAll("Простой", "Средний", "Сложный");
        modeComboBox.setValue("Простой");
        hardTime = 4;
        modeComboBox.setOnAction(e -> setMode(modeComboBox.getSelectionModel().getSelectedIndex()));

        button.setOnAction(e -> {
            isMouseInButton = isMouseInCircle(button, mouseCoordinates.getX(), mouseCoordinates.getY());
            score1++;
            scoreLabel.setText("Счет: " + score1);
            moveBall();
        });
        button1.setOnAction(e -> {
            score1--;
            scoreLabel.setText("Счет: " + score1);
            moveBall();
            });
    }

    private void setMode(int mode) {
        switch (mode) {
            case 0:
                button.setStyle("-fx-background-color: red; " + /* Цвет кнопки */
                        "-fx-text-fill: white; " + /* Цвет текста на кнопке */
                        "-fx-background-radius: 50; " + /* Задает радиус скругления кнопки (50% для круглой формы) */
                        "-fx-min-width: 100px; " + /* Минимальная ширина кнопки */
                        "-fx-min-height: 100px; " + /* Минимальная высота кнопки */
                        "-fx-max-width: 100px; " + /* Максимальная ширина кнопки */
                        "-fx-max-height: 100px; "); /* Максимальная высота кнопки */
                ballsize = 50;
                hardTime = 4;
                break;
            case 1:
                button.setStyle("-fx-background-color: red; " + /* Цвет кнопки */
                        "-fx-text-fill: white; " + /* Цвет текста на кнопке */
                        "-fx-background-radius: 25; " + /* Задает радиус скругления кнопки (50% для круглой формы) */
                        "-fx-min-width: 50px; " + /* Минимальная ширина кнопки */
                        "-fx-min-height: 50px; " + /* Минимальная высота кнопки */
                        "-fx-max-width: 50px; " + /* Максимальная ширина кнопки */
                        "-fx-max-height: 50px; "); /* Максимальная высота кнопки */
                ballsize = 25;
                hardTime = 3;
                break;
            case 2:
                button.setStyle("-fx-background-color: red; " + /* Цвет кнопки */
                        "-fx-text-fill: white; " + /* Цвет текста на кнопке */
                        "-fx-background-radius: 15; " + /* Задает радиус скругления кнопки (50% для круглой формы) */
                        "-fx-min-width: 30px; " + /* Минимальная ширина кнопки */
                        "-fx-min-height: 30px; " + /* Минимальная высота кнопки */
                        "-fx-max-width: 30px; " + /* Максимальная ширина кнопки */
                        "-fx-max-height: 30px; "); /* Максимальная высота кнопки */
                ballsize = 15;
                hardTime = 2;
                break;
        }
    }

    private void moveBall() {
        int x = new Random().nextInt((int) (700 - ballsize));
        int y = new Random().nextInt((int) (400 - ballsize));

        button.setLayoutX(x + ballsize / 2);
        button.setLayoutY(y + ballsize / 2);
    }

    private void startgame() {
        timeLabel.setText("Время: 0");
        scoreLabel.setText("Счет: 0");
        Timeline programTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeCount += 1;
            timeLabel.setText("Время: " + timeCount);
            if(timeCount % hardTime == 0)
            {
                moveBall();
            }
        }));
        programTimer.setCycleCount(Animation.INDEFINITE);
        programTimer.play();
    }
    private boolean isMouseInCircle(Button button, double mouseX, double mouseY) {
        double centerX = button.getLayoutX() + button.getWidth() / 2;
        double centerY = button.getLayoutY() + button.getHeight() / 2;
        double radius = button.getWidth() / 2;
        double distance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
        return distance <= radius;
    }
}
