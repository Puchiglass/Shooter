package com.example.shooter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class HelloController {
    @FXML
    public AnchorPane mainWindow;
    @FXML
    public VBox shooterBox;
    @FXML
    public ToolBar buttonBar;
    @FXML
    public Button startButton;
    @FXML
    public Button stopButton;
    @FXML
    public Button pauseButton;
    @FXML
    public Button shootButton;
    @FXML
    public AnchorPane playingField;
    @FXML
    public Circle bigCircle;
    @FXML
    public Line bigCircleLine;
    @FXML
    public Circle smallCircle;
    @FXML
    public Line smallCircleLine;
    @FXML
    public VBox resultBox;
    @FXML
    public Label scoreLabel;
    @FXML
    public Label countShootLabel;


    public void onShotButtonClick(ActionEvent actionEvent) {
    }

    private enum GameStatus {
        Play,
        Pause,
        Stop
    }

    private GameStatus gameStatus = GameStatus.Stop;
    private int scoreCount;
    private int shotsCount;
    private boolean bigUp = true;
    private boolean smallUp = false;
    private Timeline timeline;


    public void onStartButton() {

        if (gameStatus.equals(GameStatus.Play) || gameStatus.equals(GameStatus.Pause)) {
            shotsCount = scoreCount = 0;
            scoreLabel.setText(String.valueOf(scoreCount));
            countShootLabel.setText(String.valueOf(shotsCount));
            killAnimate();
            goToStartPosition();
        }
        gameStatus = GameStatus.Play;
        startButton.setText("Начать заново");
        animateCircles();
    }

    public void onPauseGameButtonClick() {
        if (gameStatus.equals(GameStatus.Play)) {
            gameStatus = GameStatus.Pause;
            pauseButton.setText("Продолжить");
            killAnimate();
        } else if (gameStatus.equals(GameStatus.Pause)) {
            gameStatus = GameStatus.Play;
            pauseButton.setText("Пауза");
            timeline.play();
        }

    }

    public void onStopGameButtonClick(ActionEvent actionEvent) {
        killAnimate();
        setToDefaultButton();
        goToStartPosition();
        gameStatus = GameStatus.Stop;
    }

    private void setToDefaultButton() {
        startButton.setText("Начало игры");
        pauseButton.setText("Пауза");
    }
    private void animateCircles() {
        timeline = new Timeline(new KeyFrame(Duration.millis(25), event -> {
            // Получаем текущие координаты центров кругов
            double bigCircleCenterY = bigCircle.getCenterY();
            double smallCircleCenterY = smallCircle.getCenterY();

            if (bigUp) {
                if (bigCircleCenterY > bigCircleLine.getStartY() + bigCircle.getRadius()) {
                    bigCircle.setCenterY(bigCircleCenterY - 1);
                } else {
                    bigUp = false;
                }
            } else {
                if (bigCircleCenterY < bigCircleLine.getEndY() - bigCircle.getRadius()) {
                    bigCircle.setCenterY(bigCircleCenterY + 1);
                } else {
                    bigUp = true;
                }
            }

            if (smallUp) {
                if (smallCircleCenterY > smallCircleLine.getStartY() + smallCircle.getRadius()) {
                    smallCircle.setCenterY(smallCircleCenterY - 2);
                } else {
                    smallUp = false;
                }
            } else {
                if (smallCircleCenterY < smallCircleLine.getEndY() - smallCircle.getRadius()) {
                    smallCircle.setCenterY(smallCircleCenterY + 2);
                } else {
                    smallUp = true;
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void killAnimate() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
    private void goToStartPosition() {
        bigCircle.setCenterY(0);
        smallCircle.setCenterY(0);
    }

}