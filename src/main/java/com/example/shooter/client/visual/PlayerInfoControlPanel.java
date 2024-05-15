package com.example.shooter.client.visual;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import com.example.shooter.AppConfig;
import com.example.shooter.client.PlayerInfo;

public class PlayerInfoControlPanel extends GridPane {
    private final PlayerInfoVBox[] players;

    public PlayerInfoControlPanel() {
        super();
        this.addColumn(0);
        for (int i = 0; i < AppConfig.MAX_PLAYERS; i++) {
            this.getRowConstraints().add(new RowConstraints(AppConfig.PLAYING_FIELD_HEIGHT / 4));
        }
        this.setAlignment(Pos.CENTER);
        players = new PlayerInfoVBox[AppConfig.MAX_PLAYERS];
    }

    public void addNewPlayer(PlayerInfo info) {
        if (players[info.getNumOnField()] == null) {
            players[info.getNumOnField()] = new PlayerInfoVBox(info);
            this.add(players[info.getNumOnField()], 0, info.getNumOnField());
        }
    }

    public void updateData(PlayerInfo[] infos) {
        for (PlayerInfo info: infos) {
            int id = info.getNumOnField();
            players[id].updateScoreVal(info.getScore());
            players[id].updateShotsVal(info.getShots());
        }
    }

    public void updateNumWins(PlayerInfo info) {
        players[info.getNumOnField()].updateWinsVal(info.getStatistic().getNumWins());
    }
}
