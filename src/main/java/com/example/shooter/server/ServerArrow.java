package com.example.shooter.server;

import com.example.shooter.AppConfig;
import com.example.shooter.messages.MsgData.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerArrow {
    private final static double FIELD_WIDTH = AppConfig.PLAYING_FIELD_WIDTH;
    private final static int SPEED = AppConfig.ARROW_SPEED;

    private final Point startCoords;

    private Point coords;
    private boolean active = false;

    public void remove() {
        coords = new Point(startCoords.x, startCoords.y);
        this.setActive(false);
    }

    public void move() {
        coords.x += SPEED;
        if (coords.x >= FIELD_WIDTH) {
            remove();
        }
    }

    ServerArrow(Point startCoords) {
        this.startCoords = startCoords;
        coords = new Point(startCoords.x + AppConfig.ARROW_LENGTH, startCoords.y);
    }
}
