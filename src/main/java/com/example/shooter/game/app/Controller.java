package com.example.shooter.game.app;

import com.example.shooter.game.model.Arrow;
import com.example.shooter.game.model.BigTarget;
import com.example.shooter.game.model.Score;
import com.example.shooter.game.model.SmallTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Controller {
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

    private Game game;


    public void onStartButton() {
        if (game == null) {
            BigTarget bigTarget = new BigTarget(bigCircle, bigCircleLine);
            SmallTarget smallTarget = new SmallTarget(smallCircle, smallCircleLine);
            Score score = new Score(scoreLabel, countShootLabel);
            game = new Game(mainWindow, bigTarget, smallTarget, score, startButton, pauseButton);
        }
        game.start();
    }

    public void onPauseGameButtonClick() {
        if (game == null) return;
        game.pause();
    }

    public void onStopGameButtonClick() {
        game.stop();
    }

    public void onShotButtonClick() {
        if (game.isShooting() || !game.getGameStatus().equals(GameStatus.RUN)) return;
        Arrow arrow = new Arrow();
        arrow.newArrow(mainWindow);
        game.setArrow(arrow);
        game.shoot();
    }
}