package com.example.shooter.client;

import javafx.scene.shape.Polygon;

public class Arrow extends Polygon {
    double length;

    Arrow() {
        super(0.0, 0.0,
                -10.0, -5.0,
                -10.0, -2.0,
                -40.0, -2.0,
                -40.0, 2.0,
                -10.0, 2.0,
                -10.0, 5.0);
        length = 40.0;
    }
}
