package com.example.shooter.game.model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class SmallTarget extends Target {
    public SmallTarget(Circle circle, Line line) {
        super(circle, line);
        super.speed = 5;
    }

    public void goToStart() {
        super.goToStart();
        super.isGoingUp = true;
    }

}
