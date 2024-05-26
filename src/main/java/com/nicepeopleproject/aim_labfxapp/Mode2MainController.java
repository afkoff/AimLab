package com.nicepeopleproject.aim_labfxapp;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Math;
import java.awt.Point;
import java.awt.MouseInfo;

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
import javafx.scene.input.MouseEvent;
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
    private Timeline programTimer;
    private Timeline checkMouseTimer;
    private int buttonSpeedX = 1;
    private int buttonSpeedY = 1;
    private Random random = new Random();
    private long totalHoldTime;
    private boolean isMouseInButton = false; // Флаг для проверки нахождения мыши на кнопке
    private boolean isMousePressed = false;
    private Point mouseCoordinates;
    private int releaseCount = 0;




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
        mouseCoordinates = new Point(0, 0);

        button.setOnMousePressed(e -> {
            isMousePressed = true;
            if (timeline == null ) {
                timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateHoldTime()));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });

        button.setOnMouseReleased(e -> {
            isMousePressed = false;
            if (timeline != null) {
                timeline.stop();
                timeline = null;
            }
            releaseCount++;
            if (releaseCount >= 3) {
                releaseCount = 0;
                resetGame();
            }
        });


        button.setOnMouseMoved(e -> {
            updateMouseCoordinates();
            isMouseInButton = isMouseInCircle(button, mouseCoordinates.getX(), mouseCoordinates.getY());
        });

        button.setOnMouseDragged(e -> {
            updateMouseCoordinates();
            isMouseInButton = isMouseInCircle(button, mouseCoordinates.getX(), mouseCoordinates.getY());
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
            resetGame();
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
        // Проверка нахождения мыши внутри круга каждую секунду
        checkMouseTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateMouseCoordinates();
            isMouseInButton = isMouseInCircle(button, mouseCoordinates.getX(), mouseCoordinates.getY());
        }));
        checkMouseTimer.setCycleCount(Timeline.INDEFINITE);
        checkMouseTimer.play();
        // Обновление времени удерживания каждую секунду
        Timeline holdTimeUpdateTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (isMouseInButton & isMousePressed) { // Если мышь зажата и находится внутри круга
                updateHoldTime();
            }
        }));
        holdTimeUpdateTimer.setCycleCount(Timeline.INDEFINITE);
        holdTimeUpdateTimer.play();


    }

    // Метод для обновления координат курсора мыши относительно кнопки
    private void updateMouseCoordinates() {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        mouseCoordinates = getMouseCoordinatesRelativeToButton(button, mouseLocation.getX(), mouseLocation.getY());
    }

    // Метод для получения координат мыши относительно кнопки
    private Point getMouseCoordinatesRelativeToButton(Button button, double mouseX, double mouseY) {
        double sceneX = mouseX;
        double sceneY = mouseY;

        double buttonSceneX = button.localToScene(button.getBoundsInLocal()).getMinX();
        double buttonSceneY = button.localToScene(button.getBoundsInLocal()).getMinY();

        double relativeX = sceneX - buttonSceneX;
        double relativeY = sceneY - buttonSceneY;

        return new Point((int) relativeX, (int) relativeY);
    }

    // Метод для проверки нахождения мыши в круге
    private boolean isMouseInCircle(Button button, double mouseX, double mouseY) {
        double centerX = button.getLayoutX() + button.getWidth() / 2;
        double centerY = button.getLayoutY() + button.getHeight() / 2;
        double radius = button.getWidth() / 2;
        double distance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
        return distance <= radius;
    }


    private void updateHoldTime() {

        totalHoldTime += 100;
        holdTimeLabel.setText("Время удерживания: " + totalHoldTime / 1000 + " сек");

    }
    private void resetGame() {

        // Перемещаем кнопку в случайное место на экране
        double sceneWidth = button.getScene().getWidth();
        double sceneHeight = button.getScene().getHeight();
        button.setLayoutX(random.nextDouble() * (sceneWidth - button.getWidth()));
        button.setLayoutY(random.nextDouble() * (sceneHeight - button.getHeight()));

        // Перезапускаем таймер
        programTimer.stop();
        programTimer.playFromStart();
    }

}

