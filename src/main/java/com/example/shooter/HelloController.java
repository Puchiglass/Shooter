package com.example.shooter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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


    private enum GameStatus {
        Play,
        Pause,
        Stop
    }

    private GameStatus gameStatus = GameStatus.Stop;
    private int scoreCount = 0;
    private int shotsCount = 0;
    private boolean bigUp = true;
    private boolean smallUp = false;
    private boolean isDoneArrow = false;
    private Timeline timelineCircle;
    private Timeline timelineArrow;
    private Line arrow;


    public void onStartButton() {

        shotsCount = scoreCount = 0;
        scoreLabel.setText(String.valueOf(scoreCount));
        countShootLabel.setText(String.valueOf(shotsCount));
        killAnimate();
        goToStartPosition();
        gameStatus = GameStatus.Play;
        startButton.setText("Начать заново");
        animateCircles();
    }

    public void onPauseGameButtonClick() {

        if (gameStatus.equals(GameStatus.Play)) {
            gameStatus = GameStatus.Pause;
            pauseButton.setText("Продолжить");
            timelineCircle.stop();
            timelineArrow.stop();
        } else if (gameStatus.equals(GameStatus.Pause)) {
            gameStatus = GameStatus.Play;
            pauseButton.setText("Пауза");
            timelineCircle.play();
            timelineArrow.play();
        }
    }

    public void onStopGameButtonClick() {

        killAnimate();
        setToDefaultButton();
        goToStartPosition();
        gameStatus = GameStatus.Stop;
    }

    public void onShotButtonClick() {

        if (arrow != null) return;
        if (!gameStatus.equals(GameStatus.Play)) return;
        shotsCount++;
        countShootLabel.setText(String.valueOf(shotsCount));

        arrow = createNewLine();

        timelineArrow = new Timeline(new KeyFrame(Duration.millis(25), event -> {
            if (isDoneArrow) {
                isDoneArrow = false;
                if (arrow != null) {
                    arrow.setStartX(- 1000);
                    arrow.setEndX(- 1000);
                }
                arrow = null;
                timelineArrow.stop();
            } else {
                double head = arrow.getEndX();
                double yArrow = arrow.getEndY();
                if (head >= 480) {
                    mainWindow.getChildren().remove(arrow);
                    isDoneArrow = true;
                } else {
                    int res = hit(head, yArrow);
                    if (res != 0) {
                        int currentRes = Integer.parseInt(scoreLabel.getText());
                        scoreLabel.setText(String.valueOf(currentRes + res));
                        isDoneArrow = true;
                    } else {
                        arrow.setStartX(arrow.getStartX() + 15);
                        arrow.setEndX(arrow.getEndX() + 15);
                    }
                }
            }
        }));
        timelineArrow.setCycleCount(Timeline.INDEFINITE);
        timelineArrow.play();
    }

    private int hit(double head, double yArrow) {
        int res = 0;

        yArrow = 0;
        double distanceToBigCircle = Math.sqrt(Math.pow(334 - head, 2) +
                Math.pow(bigCircle.getCenterY() - yArrow, 2));
        double distanceToSmallCircle = Math.sqrt(Math.pow(417 - head, 2) +
                Math.pow(smallCircle.getCenterY() - yArrow, 2));

        if (distanceToBigCircle <= bigCircle.getRadius() && head <= 334)
            res = 1;
        else if (distanceToSmallCircle <= smallCircle.getRadius() && head <= 417)
            res = 2;
        return res;
    }

    private Line createNewLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(playingField.getHeight() / 2);
        line.setEndX(50);
        line.setEndY(playingField.getHeight() / 2);
        line.setId("arrow");
        playingField.getChildren().add(line);
        return line;
    }

    private void setToDefaultButton() {
        startButton.setText("Начало игры");
        pauseButton.setText("Пауза");
    }

    private void animateCircles() {

        timelineCircle = new Timeline(new KeyFrame(Duration.millis(25), event -> {
            // Получаем текущие координаты центров кругов
            double bigCircleCenterY = bigCircle.getCenterY();
            double smallCircleCenterY = smallCircle.getCenterY();

            if (bigUp) {
                if (bigCircleCenterY > bigCircleLine.getStartY() + bigCircle.getRadius()) {
                    bigCircle.setCenterY(bigCircleCenterY - 1.75);
                } else {
                    bigUp = false;
                }
            } else {
                if (bigCircleCenterY < bigCircleLine.getEndY() - bigCircle.getRadius()) {
                    bigCircle.setCenterY(bigCircleCenterY + 1.75);
                } else {
                    bigUp = true;
                }
            }

            if (smallUp) {
                if (smallCircleCenterY > smallCircleLine.getStartY() + smallCircle.getRadius()) {
                    smallCircle.setCenterY(smallCircleCenterY - 3.5);
                } else {
                    smallUp = false;
                }
            } else {
                if (smallCircleCenterY < smallCircleLine.getEndY() - smallCircle.getRadius()) {
                    smallCircle.setCenterY(smallCircleCenterY + 3.5);
                } else {
                    smallUp = true;
                }
            }
        }));
        timelineCircle.setCycleCount(Timeline.INDEFINITE);
        timelineCircle.play();
    }

    private void killAnimate() {
        if (timelineCircle != null) {
            timelineCircle.stop();
            timelineCircle = null;
        }
        if (timelineArrow != null) {
            timelineArrow.stop();
            timelineArrow = null;
        }
    }

    private void goToStartPosition() {
        bigCircle.setCenterY(0);
        smallCircle.setCenterY(0);
    }

}