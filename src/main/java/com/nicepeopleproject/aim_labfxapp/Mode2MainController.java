package com.nicepeopleproject.aim_labfxapp;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Mode2MainController  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button;

    @FXML
    private Label holdTimeLabel;

    @FXML
    private Label timeLabel;

    private Timeline timeline;
    private int buttonSpeedX = 1;
    private int buttonSpeedY = 1;
    private long totalHoldTime;



    @FXML
    void initialize() {
        button.setText("");
        button.setStyle("-fx-background-color: red; " + /* Цвет кнопки */
                "-fx-text-fill: white; " + /* Цвет текста на кнопке */
                "-fx-background-radius: 50; " + /* Задает радиус скругления кнопки (50% для круглой формы) */
                "-fx-min-width: 100px; " + /* Минимальная ширина кнопки */
                "-fx-min-height: 100px; " + /* Минимальная высота кнопки */
                "-fx-max-width: 100px; " + /* Максимальная ширина кнопки */
                "-fx-max-height: 100px; "); /* Максимальная высота кнопки */
        Random random = new Random();

        button.setOnMousePressed(e -> {
            if (timeline == null) {
                timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateHoldTime()));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });

        button.setOnMouseReleased(e -> {
            if (timeline != null) {
                timeline.stop();
                timeline = null;
            }
        });

        AtomicInteger elapsedTime = new AtomicInteger();
        elapsedTime.set(2000);

        Timeline buttonMoveTimer = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            elapsedTime.addAndGet(15);

            if (elapsedTime.get() >= 2000) {
                double randomAngle = random.nextDouble() * 2 * Math.PI;
                int speed = 3;

                buttonSpeedX = (int) (speed * Math.cos(randomAngle));
                buttonSpeedY = (int) (speed * Math.sin(randomAngle));

                elapsedTime.set(0);
            }

            double newX = button.getLayoutX() + buttonSpeedX;
            double newY = button.getLayoutY() + buttonSpeedY;

            double sceneWidth = button.getScene().getWidth();
            double sceneHeight = button.getScene().getHeight();

            if (newX < 0 || newX > sceneWidth - button.getWidth()) {
                buttonSpeedX = -buttonSpeedX;
                newX = Math.max(0, Math.min(newX, sceneWidth - button.getWidth()));
            }

            if (newY < 0 || newY > sceneHeight - button.getHeight()) {
                buttonSpeedY = -buttonSpeedY;
                newY = Math.max(0, Math.min(newY, sceneHeight - button.getHeight()));
            }

            button.setLayoutX(newX);
            button.setLayoutY(newY);
        }));

        buttonMoveTimer.setCycleCount(Timeline.INDEFINITE);
        buttonMoveTimer.play();

        // Таймер для закрытия программы через 10 секунд
        Timeline programTimer = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            // Ваш код для перехода на сцену mode2_start_scene_view
            Platform.runLater(() -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mode2_start_menu_view.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    Stage stage = (Stage) timeLabel.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }));
        programTimer.setCycleCount(Animation.INDEFINITE);
        programTimer.play();

        // Обновление оставшегося времени раз в секунду
        Timeline timeUpdateTimer = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            long remainingTime = 10 - (long) Math.ceil(programTimer.getCurrentTime().toSeconds());
            timeLabel.setText("Оставшееся время: " + remainingTime + " сек");
        }));
        timeUpdateTimer.setCycleCount(Animation.INDEFINITE);
        timeUpdateTimer.play();
    }

    private void updateHoldTime() {
        totalHoldTime += 100;
        holdTimeLabel.setText("Время удерживания: " + totalHoldTime / 1000 + " сек");
    }

}
