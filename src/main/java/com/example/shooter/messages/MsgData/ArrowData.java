package com.example.shooter.messages.MsgData;

public class ArrowData {
    Point coords;
    boolean active_flag;

    public ArrowData(Point coords, boolean is_active) {
        this.coords = coords;
        this.active_flag = is_active;
    }

    public Point get_coords() {
        return coords;
    }

    public boolean is_active() {
        return active_flag;
    }
}
