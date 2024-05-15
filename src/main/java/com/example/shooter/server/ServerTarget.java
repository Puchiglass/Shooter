package com.example.shooter.server;

import com.example.shooter.AppConfig;
import com.example.shooter.messages.MsgData.Point;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServerTarget {
    private final static double fieldHeight = AppConfig.PLAYING_FIELD_HEIGHT;

    private final Point coords;
    private final double radius;
    private final int speed;
    private final int points;
    private final double xDistance;

    private boolean isUpper = true;


    public void move() {
        if (isUpper) {
            if (coords.y - radius - speed > 0) {
                coords.y -= speed;
            }
            else {
                isUpper = false;
                coords.y += speed;
            }
        }
        else {
            if (coords.y + radius + speed <= fieldHeight) {
                coords.y += speed;
            }
            else {
                isUpper = true;
                coords.y -= speed;
            }
        }
    }

    public boolean checkHit(ServerArrow arrow) {
        double x = Math.pow(coords.x - arrow.getCoords().x, 2);
        double y = Math.pow(coords.y - arrow.getCoords().y, 2);
        double r = Math.pow(radius, 2);
        return x + y <= r;
    }

    public void setStartCoords() {
        isUpper = true;
        coords.x = xDistance;
        coords.y = fieldHeight / 2;
    }
}
