package com.example.shooter.model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class BigTarget extends Target {
    public BigTarget(Circle circle, Line line) {
        super(circle, line);
        super.speed = 2.5;
    }

    void goToStart() {
        super.goToStart();
        super.isGoingUp = false;
    }
}
