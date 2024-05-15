package com.example.shooter.server;

import com.example.shooter.AppConfig;
import com.example.shooter.messages.MsgData.Point;

public class ServerArrow {
    static double field_width = AppConfig.PLAYING_FIELD_WIDTH;
    private boolean activity_flag;
    private final int speed;
    private final Point start_coords;
    private Point coords;

    ServerArrow(Point start_coords) {
        activity_flag = false;
        this.start_coords = start_coords;
        coords = new Point(start_coords.x, start_coords.y);
        speed = AppConfig.ARROW_SPEED;
    }

    public void set_active(boolean value) {
        activity_flag = value;
    }

    public boolean is_active() {
        return activity_flag;
    }

    public void remove() {
        coords = new Point(start_coords.x, start_coords.y);
        this.set_active(false);
    }

    public void move() {
        coords.x += speed;
        if (coords.x >= field_width) {
            remove();
        }
    }

    public Point get_coords() {
        return coords;
    }
}
