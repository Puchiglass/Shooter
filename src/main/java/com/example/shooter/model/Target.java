package com.example.shooter.model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Target {
    private final Circle circle;
    private final Line line;
    private final double upperBound;
    private final double bottomBound;
    protected boolean isGoingUp;
    protected double speed = 1.75;

    public Target(Circle circle, Line line) {
        this.circle = circle;
        this.line = line;
        this.upperBound = line.getEndY();
        this.bottomBound = line.getStartY();
        this.isGoingUp = true;
    }

    void move() {
        double center = getCenterY();
        if (isGoingUp) {
            if (center > bottomBound + circle.getRadius())
                circle.setCenterY(center - speed);
            else
                isGoingUp = false;
        } else {
            if (center < upperBound - circle.getRadius())
                circle.setCenterY(center + speed);
            else
                isGoingUp = true;
        }
    }

    void goToStart() {
        circle.setCenterY(0);
    }

    private double getCenterY() {
        return circle.getCenterY();
    }
}
