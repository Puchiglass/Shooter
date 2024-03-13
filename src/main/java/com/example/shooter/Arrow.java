package com.example.shooter;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Arrow {
    private final Line arrowShaft;
    private final Polygon arrowHead;

    Arrow(double endX, double endY, int size) {
        arrowShaft = new Line(endX - size, endY, endX, endY);
        arrowHead = new Polygon();

        arrowHead.getPoints().addAll(
                arrowShaft.getEndX() - 5, arrowShaft.getEndY() - 5,
                arrowShaft.getEndX(), arrowShaft.getEndY(),
                arrowShaft.getEndX() - 5, arrowShaft.getEndY() + 5
        );
    }

    public Line getArrowShaft() {
        return arrowShaft;
    }
    public Polygon getArrowHead() {
        return arrowHead;
    }

    public void movement(int speed) {
        arrowShaft.setStartX(arrowShaft.getStartX() + speed);
        arrowShaft.setEndX(arrowShaft.getEndX() + speed);

        arrowHead.getPoints().set(0, arrowHead.getPoints().get(0) + speed);
        arrowHead.getPoints().set(2, arrowHead.getPoints().get(2) + speed);
        arrowHead.getPoints().set(4, arrowHead.getPoints().get(4) + speed);
    }
}
