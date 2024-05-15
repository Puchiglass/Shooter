package com.example.shooter.client;

import com.example.shooter.server.PlayerStatistic;
import com.example.shooter.server.ServerArrow;
import com.example.shooter.server.SocketServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private final SocketServer socket;
    private final PlayerInfo info;
    private final ServerArrow arrow;

    private boolean ready = false;

    public Player(SocketServer socket, ServerArrow arrow, int id, String name, PlayerStatistic stat) {
        this.socket = socket;
        this.arrow = arrow;
        this.info = new PlayerInfo(id, name, stat);
    }

    public void incrementNumWins() {
        info.incrementNumWins();
    }

    public PlayerStatistic getStat() {
        return info.getStatistic();
    }
}
