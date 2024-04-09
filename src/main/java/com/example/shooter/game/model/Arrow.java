package com.example.shooter.game.model;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Arrow {
    private Line line;
    private boolean isAlive = true;

    public void move(AnchorPane playingField) {
        if (line == null) return;

        double head = line.getEndX();
        if (head >= 480) {
            isAlive = false;
        } else {
            line.setStartX(line.getStartX() + 15);
            line.setEndX(line.getEndX() + 15);
        }
    }

    public Line newArrow(AnchorPane playingField) {
        line = new Line();
        line.setStartX(50);
        line.setStartY(169);
        line.setEndX(100);
        line.setEndY(169);
        line.setId("arrow");
        Platform.runLater(() -> playingField.getChildren().add(line));
        return line;
    }

    public void removeArrow(AnchorPane playingField) {
        Platform.runLater(() -> playingField.getChildren().remove(line));
    }

    public double getX() {
        return line.getEndX();
    }
    public double getY() {
        return line.getEndY();
    }
}
