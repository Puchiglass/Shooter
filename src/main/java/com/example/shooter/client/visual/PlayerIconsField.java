package com.example.shooter.client.visual;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import com.example.shooter.AppConfig;

public class PlayerIconsField extends GridPane {
    PlayerIcon[] icons;
    public PlayerIconsField() {
        super();
        this.addColumn(0);
        for (int i = 0; i < AppConfig.MAX_PLAYERS; i++) {
            this.getRowConstraints().add(new RowConstraints(AppConfig.PLAYING_FIELD_HEIGHT / 4));
        }
        this.setAlignment(Pos.CENTER);
        icons = new PlayerIcon[AppConfig.MAX_PLAYERS];
    }

    public void add_new_icon(int id, boolean is_main) {
        if (icons[id] == null){
            icons[id] = new PlayerIcon(is_main);
            this.add(icons[id], 0, id);
        }
    }
}
