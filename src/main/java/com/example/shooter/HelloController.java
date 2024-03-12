package com.example.shooter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;

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
    public Ellipse bigCircle;
    @FXML
    public Ellipse smallCircle;
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

    private int scoreCount;
    private int shotsCount;

    public void onStartButton(ActionEvent actionEvent) {

        if (gameStatus.equals(GameStatus.Play) || gameStatus.equals(GameStatus.Stop)) {
            gameStatus = GameStatus.Play;

            shotsCount = scoreCount = 0;
            scoreLabel.setText(String.valueOf(scoreCount));
            countShootLabel.setText(String.valueOf(shotsCount));
            startButton.setText((gameStatus.equals(GameStatus.Play)) ?
                    "Начать заново"
                    : "Новая игра");
        } else {
            gameStatus = GameStatus.Play;


        }

    }
}