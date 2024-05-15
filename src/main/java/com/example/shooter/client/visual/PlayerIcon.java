package com.example.shooter.client.visual;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PlayerIcon extends Polygon {
    public PlayerIcon(boolean is_main) {
        super(
            50, 25,
                0, 50,
                0, 0
        );
        Color color;
        if (is_main) {
            color = Color.GREEN;
        }
        else {
            color = Color.RED;
        }
        this.setFill(color);
    }
}
