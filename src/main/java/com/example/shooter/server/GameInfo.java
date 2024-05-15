package com.example.shooter.server;

import com.example.shooter.*;
import com.example.shooter.client.Player;
import com.example.shooter.client.PlayerInfo;
import com.example.shooter.messages.MsgData.ArrowData;
import com.example.shooter.messages.MsgData.Point;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Objects;

@Getter
@Setter
public class GameInfo {
    Server server = null;

    public static final ServerArrow[] arrows = {
            new ServerArrow(new Point(0, 65)),
            new ServerArrow(new Point(0, 200)),
            new ServerArrow(new Point(0, 335)),
            new ServerArrow(new Point(0, 470))
    };
    private ServerTarget[] targets;
    static final int MAX_PLAYERS = AppConfig.MAX_PLAYERS;
    private boolean gameStatus = false;
    private boolean pauseStatus = false;
    private int countPlayers = 0;

    HashMap<Integer, Integer> playerIdByPort = new HashMap<>();
    Player[] players = new Player[MAX_PLAYERS];

    void addPlayer(int id, Player player) {
        players[id] = player;
    }

    void deleteId(int port) {
        playerIdByPort.remove(port);
    }

    boolean checkReady() {
        for (Player p: players) {
            if (p != null && !p.is_ready()) {
                return false;
            }
        }
        return true;
    }

    void moveArrows() {
        for (Player p: players) {
            if (p != null && p.get_arrow().isActive()) {
                p.get_arrow().move();
            }
        }
    }

    void moveTargets() {
        for (ServerTarget t : targets) {
            t.move();
        }
    }

    public void setTargetToStart() {
        for (ServerTarget t : targets) {
            t.set_start_coords();
        }
    }

    public void clearStatistic() {
        for (Player p: players) {
            if (p != null) {
                p.get_info().clearStatistic();
            }
        }
    }

    public void sendUnreadyPlayers() {
        for (Player p: players) {
            if (p != null) {
                p.set_ready(false);
                p.get_socket().send_unready();
            }
        }
    }

    public void sendWinner(PlayerInfo info) {
        for (Player p: players) {
            if (p != null) {
                p.get_socket().send_winner(info);
            }
        }
    }

    void sendDataToPlayers() {
        for (Player p: players) {
            if (p != null) {
                p.get_socket().send_data();
            }
        }
    }

    void sendNewPlayer(int id) {
        for (Player p: players) {
            if (p != null) {
                p.get_socket().send_new_player(id);
            }
        }
    }

    public PlayerInfo[] getPlayersInfo() {
        PlayerInfo[] res = new PlayerInfo[countPlayers];
        int cur_ind = 0;
        for (Player p: players) {
            if (p != null) {
                PlayerInfo info = p.get_info();
                res[cur_ind] = info;
                cur_ind++;
            }
        }
        return res;
    }

    public Point[] getTargetCoords() {
        Point[] res = new Point[2];
        for (int i = 0; i < 2; i++) {
            res[i] = targets[i].get_coords();
        }
        return res;
    }

    public ArrowData[] getDataArrows() {
        ArrowData[] res = new ArrowData[countPlayers];
        int cur_ind = 0;
        for (Player p: players) {
            if (p != null) {
                ServerArrow arrow = p.get_arrow();
                res[cur_ind] = new ArrowData(arrow.getCoords(), arrow.isActive());
                cur_ind++;
            }
        }
        return res;
    }

    public PlayerInfo[] getDataInfo() {
        PlayerInfo[] res = new PlayerInfo[countPlayers];
        int cur_ind = 0;
        for (Player p: players) {
            if (p != null) {
                res[cur_ind] = p.get_info();
                cur_ind++;
            }
        }
        return res;
    }

    public boolean isUniqueName(String name) {
        for (Player p : players) {
            if (p != null && Objects.equals(p.get_info().getName(), name)) {
                return false;
            }
        }
        return true;
    }

    public void pauseGame() {
        pauseStatus = true;
        sendUnreadyPlayers();
    }

    public void unpauseGame() {
        pauseStatus = false;
        server.unpause_game();
    }

    public void run_game() {
        gameStatus = true;
        server.start_game();
    }

    public Player getPlayer(int id) {
        return players[id];
    }

    public void setPlayerId(int port, int id) {
        playerIdByPort.put(port, id);
    }

    public int getPlayerId(int port) {
        return playerIdByPort.get(port);
    }

    public void incrementCntPlayers() {
        countPlayers++;
    }

    public void decrementCntPlayers() {
        countPlayers--;
        if (countPlayers == 0) {
            gameStatus = false;
            pauseStatus = false;
        }
    }

    public int getFreeNumOnField() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
