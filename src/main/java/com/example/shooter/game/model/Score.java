package com.example.shooter.game.model;

import javafx.scene.control.Label;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Score {
    public Label scoreLabel;
    public Label countShootLabel;

    public void setDefault() {
        scoreLabel.setText("0");
        countShootLabel.setText("0");
    }


    public void plusScore(int hit) {
        int currentHit = Integer.parseInt(scoreLabel.getText());
        scoreLabel.setText(String.valueOf(currentHit + hit));
    }

    public void plusShoot() {
        int currentShoot = Integer.parseInt(countShootLabel.getText());
        countShootLabel.setText(String.valueOf(currentShoot + 1));
    }
}
