package com.example.shooter.game.model;

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

    public void move() {
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

    public void goToStart() {
        circle.setCenterY(0);
    }

    public double getX() {
        return circle.getCenterX();
    }

    public double getY() {
        return circle.getCenterY();
    }

    private double getCenterY() {
        return circle.getCenterY();
    }

    public double getRadius() {
        return circle.getRadius();
    }
}
