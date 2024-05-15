package com.example.shooter.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import com.example.shooter.client.visual.*;
import com.example.shooter.messages.MsgAction;
import com.example.shooter.messages.MsgData.ArrowData;
import com.example.shooter.messages.MsgData.Point;
import com.example.shooter.messages.SignalMsg;
import com.example.shooter.server.PlayerStatistic;

import java.util.List;

public class MainController {
    @FXML
    public VBox RightVBox;
    @FXML
    public VBox LeftVBox;
    public BorderPane main;
    @FXML
    public Button ready_button;
    @FXML
    private Pane playing_field;
    @FXML
    private Circle big_target;
    @FXML
    private Circle small_target;
    private PlayerInfoControlPanel controlPanel;
    private PlayerIconsField iconsField;
    private Arrow[] arrows;

    ClientGameInfo model = ClientGameInfo.getInstance();

    @FXML
    public void initialize() {
        main.getStylesheets().add("style.css");
        ready_button.getStyleClass().add("unready-btn");
        model.mc = this;
        arrows = new Arrow[4];
        controlPanel = new PlayerInfoControlPanel();
        RightVBox.getChildren().add(controlPanel);
        iconsField = new PlayerIconsField();
        LeftVBox.getChildren().add(iconsField);

        for (int i = 0; i < 4; i++) {
            arrows[i] = new Arrow();
        }

        ConnectWindow connect_window = new ConnectWindow();
        connect_window.show();
    }

    @FXML
    void ready() {
        if (model.cls != null) {
            model.cls.sendSignal(new SignalMsg(MsgAction.SET_READY, true));
            ready_button.getStyleClass().remove("unready-btn");
            ready_button.getStyleClass().add("ready-btn");
        }
    }

    void unready() {
        ready_button.getStyleClass().remove("ready-btn");
        ready_button.getStyleClass().add("unready-btn");
    }

    @FXML
    void shoot() {
        if (model.cls != null) {
            model.cls.sendSignal(new SignalMsg(MsgAction.SHOT, true));
        }
    }

    @FXML
    void pause_game() {
        if (model.cls != null) {
            model.cls.sendSignal(new SignalMsg(MsgAction.PAUSE, true));
        }
    }

    @FXML
    void get_leaderboard() {
        model.cls.sendSignal(new SignalMsg(MsgAction.GET_LEADERBOARD, true));
    }

    public void add_new_player(PlayerInfo info) {
        Platform.runLater( () -> {
            iconsField.addNewIcon(info.getNumOnField(), info.getNumOnField() == model.getPlayerId());
            controlPanel.addNewPlayer(info);
        });
    }
    public void update_data(PlayerInfo[] infos) {
        Platform.runLater(() -> {
            Point[] new_target_coords = model.getTargetCoords();
            big_target.setLayoutX(new_target_coords[0].x);
            big_target.setLayoutY(new_target_coords[0].y);

            small_target.setLayoutX(new_target_coords[1].x);
            small_target.setLayoutY(new_target_coords[1].y);

            ArrowData[] arrows_data = model.getArrows();
            for (int i = 0; i < arrows_data.length; i++) {
                if (arrows_data[i].isActive()) {
                    arrows[i].setLayoutX(arrows_data[i].getCoords().x);
                    arrows[i].setLayoutY(arrows_data[i].getCoords().y);
                    if (!playing_field.getChildren().contains(arrows[i])) {
                        playing_field.getChildren().add(arrows[i]);
                    }
                }
                else {
                    playing_field.getChildren().remove(arrows[i]);
                }
            }
            controlPanel.updateData(infos);
        });
    }

    public void show_winner(PlayerInfo info) {
        Platform.runLater( () -> {
            controlPanel.updateNumWins(info);
            WinnerWindow.show(info);
        });
    }

    public void show_leaderboard(List<PlayerStatistic> leaderboard) {
        Platform.runLater( () -> {
            LeaderboardWindow leaderboard_window = new LeaderboardWindow();
            leaderboard_window.show(leaderboard);
        });
    }
}
