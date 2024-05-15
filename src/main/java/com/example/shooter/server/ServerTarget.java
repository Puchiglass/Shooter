package com.example.shooter.server;

import com.example.shooter.AppConfig;
import com.example.shooter.messages.MsgData.Point;

public class ServerTarget {
    static double field_height = AppConfig.PLAYING_FIELD_HEIGHT;
    private final Point coords;
    private final double radius;
    private boolean is_upper;
    private final int speed;
    private final int points;
    private final double x_distance;

    ServerTarget(Point coords, double radius, double distance_from_edge, int target_speed, int points_for_hit) {
        this.coords = coords;
        is_upper = true;
        this.radius = radius;
        speed = target_speed;
        points = points_for_hit;
        x_distance = distance_from_edge;
    }

    public void move() {
        if (is_upper) {
            if (coords.y - radius - speed > 0) {
                coords.y -= speed;
            }
            else {
                is_upper = false;
                coords.y += speed;
            }
        }
        else {
            if (coords.y + radius + speed <= field_height) {
                coords.y += speed;
            }
            else {
                is_upper = true;
                coords.y -= speed;
            }
        }
    }

    public boolean check_hit(ServerArrow arrow) {
        double x = Math.pow(coords.x - arrow.getCoords().x, 2);
        double y = Math.pow(coords.y - arrow.getCoords().y, 2);
        double r = Math.pow(radius, 2);
        return x + y <= r;
    }

    public int get_points_for_hit() {
        return points;
    }

    public void set_start_coords() {
        is_upper = true;
        coords.x = x_distance;
        coords.y = field_height / 2;
    }

    public Point get_coords() {
        return coords;
    }
}
