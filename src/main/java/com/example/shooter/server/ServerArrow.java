package com.example.shooter.server;

import com.example.shooter.AppConfig;
import com.example.shooter.messages.MsgData.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerArrow {
    static double FIELD_WIDTH = AppConfig.PLAYING_FIELD_WIDTH;
    private boolean active;
    private final int speed;
    private final Point startCoords;
    private Point coords;

    public void remove() {
        coords = new Point(startCoords.x, startCoords.y);
        this.setActive(false);
    }

    public void move() {
        coords.x += speed;
        if (coords.x >= FIELD_WIDTH) {
            remove();
        }
    }

    ServerArrow(Point startCoords) {
        active = false;
        this.startCoords = startCoords;
        coords = new Point(startCoords.x + AppConfig.ARROW_LENGTH, startCoords.y);
        speed = AppConfig.ARROW_SPEED;
    }
}
