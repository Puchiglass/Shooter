package com.example.shooter.client.visual;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.example.shooter.AppConfig;
import com.example.shooter.client.PlayerInfo;

public class PlayerInfoVBox extends VBox {
    Label nameLabel;
    Label scoreLabel;
    Label shotsLabel;
    Label scoreVal;
    Label shotsVal;
    Label winsLabel;
    Label winsVal;

    public PlayerInfoVBox(PlayerInfo info) {
        super();
        double height = AppConfig.PLAYING_FIELD_HEIGHT / 4;
        this.setHeight(height);

        nameLabel = new Label(info.getName());
        this.getChildren().add(nameLabel);

        scoreLabel = new Label("Счет:");
        this.getChildren().add(scoreLabel);

        scoreVal = new Label(Integer.toString(info.getScore()));
        this.getChildren().add(scoreVal);

        shotsLabel = new Label("Выстрелы:");
        this.getChildren().add(shotsLabel);

        shotsVal = new Label(Integer.toString(info.getShots()));
        this.getChildren().add(shotsVal);

        winsLabel = new Label("Победы:");
        this.getChildren().add(winsLabel);

        winsVal = new Label(Integer.toString(info.getStatistic().getNumWins()));
        this.getChildren().add(winsVal);

        this.setLayoutY(info.getNumOnField() * height);
        this.setLayoutX(30);
        this.setAlignment(Pos.CENTER);
    }

    public void updateScoreVal(int val)
    {
        scoreVal.setText(Integer.toString(val));
    }

    public void updateShotsVal(int val) {
        shotsVal.setText(Integer.toString(val));
    }

    public void updateWinsVal(int val) {
        winsVal.setText(Integer.toString(val));
    }

}
