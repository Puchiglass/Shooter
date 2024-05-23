package com.example.shooter.client.visual;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PlayerIcon extends Polygon {
    public PlayerIcon(boolean isMain) {
        super(
                30, 15,
                0, 30,
                0, 0
        );

        Color color;
        if (isMain) {
            color = Color.BLUE;
        } else {
            color = Color.BLACK;
        }
        this.setFill(color);
    }
}
