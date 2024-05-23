package com.example.shooter.client;

import com.example.shooter.AppConfig;
import javafx.scene.shape.Line;

public class Arrow extends Line {
    public Arrow() {
        super(0.0, 0.0, AppConfig.ARROW_LENGTH, 0.0);
    }
}