package com.example.shooter.client;

import com.example.shooter.server.PlayerStatistic;
import com.example.shooter.server.ServerArrow;
import com.example.shooter.server.SocketServer;

public class Player {
    SocketServer socket;
    boolean ready_status;
    PlayerInfo info;
    ServerArrow arrow;

    public Player(SocketServer socket, ServerArrow arrow, int id, String name, PlayerStatistic stat) {
        this.socket = socket;
        ready_status = false;
        this.arrow = arrow;
        info = new PlayerInfo(id, name, stat);
    }

    public void increase_num_wins() {
        info.incrementNumWins();
    }

    public PlayerStatistic get_stat() {
        return info.getStatistic();
    }

    public boolean is_ready() {
        return ready_status;
    }

    public void set_ready(boolean value) {
        ready_status = value;
    }

    public ServerArrow get_arrow() {
        return arrow;
    }

    public SocketServer get_socket() {
        return socket;
    }

    public PlayerInfo get_info() {
        return info;
    }
}
