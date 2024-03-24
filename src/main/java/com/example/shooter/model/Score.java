package com.example.shooter.model;

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
}
