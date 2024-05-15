package com.example.shooter.client.visual;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.example.shooter.AppConfig;
import com.example.shooter.client.PlayerInfo;

public class PlayerInfoVBox extends VBox {
    Label name_label;
    Label score_label;
    Label shots_label;
    Label score_val;
    Label shots_val;
    Label wins_label;
    Label wins_val;

    public PlayerInfoVBox(PlayerInfo info) {
        super();
        double height = AppConfig.PLAYING_FIELD_HEIGHT / 4;
        this.setHeight(height);

        name_label = new Label(info.get_name());
        this.getChildren().add(name_label);

        score_label = new Label("Счет:");
        this.getChildren().add(score_label);

        score_val = new Label(Integer.toString(info.get_score()));
        this.getChildren().add(score_val);

        shots_label = new Label("Выстрелы:");
        this.getChildren().add(shots_label);

        shots_val = new Label(Integer.toString(info.get_shots()));
        this.getChildren().add(shots_val);

        wins_label = new Label("Победы:");
        this.getChildren().add(wins_label);

        wins_val = new Label(Integer.toString(info.get_stat().getNumWins()));
        this.getChildren().add(wins_val);

        this.setLayoutY(info.get_num_on_field() * height);
        this.setLayoutX(30);
        this.setAlignment(Pos.CENTER);
    }

    public void update_score_val(int val)
    {
        score_val.setText(Integer.toString(val));
    }

    public void update_shots_val(int val) {
        shots_val.setText(Integer.toString(val));
    }

    public void update_wins_val(int val) {
        wins_val.setText(Integer.toString(val));
    }

}
