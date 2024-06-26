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
public class ServerGameInfo {
    public static final int MAX_PLAYERS = AppConfig.MAX_PLAYERS;
    public static final ServerArrow[] arrows = {
            new ServerArrow(new Point(0, 65)),
            new ServerArrow(new Point(0, 200)),
            new ServerArrow(new Point(0, 335)),
            new ServerArrow(new Point(0, 470))
    };

    private Server server = null;
    private ServerTarget[] targets;
    private boolean gameStatus = false;
    private boolean pauseStatus = false;
    private int countPlayers = 0;
    private HashMap<Integer, Integer> playerIdByPort = new HashMap<>();
    private Player[] players = new Player[MAX_PLAYERS];

    private static ServerGameInfo instance;
    private ServerGameInfo() {}
    public static ServerGameInfo getInstance() {
        if (instance == null) {
            instance = new ServerGameInfo();
        }
        return instance;
    }

    void addPlayer(int id, Player player) {
        players[id] = player;
    }

    void deleteId(int port) {
        playerIdByPort.remove(port);
    }

    boolean checkReady() {
        for (Player p: players) {
            if (p != null && !p.isReady()) {
                return false;
            }
        }
        return true;
    }

    void moveArrows() {
        for (Player p: players) {
            if (p != null && p.getArrow().isActive()) {
                p.getArrow().move();
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
            t.setStartCoords();
        }
    }

    public void clearStatistic() {
        for (Player p: players) {
            if (p != null) {
                p.getInfo().clearStatistic();
            }
        }
    }

    public void sendUnreadyPlayers() {
        for (Player p: players) {
            if (p != null) {
                p.setReady(false);
                p.getSocket().sendUnready();
            }
        }
    }

    public void sendWinner(PlayerInfo info) {
        for (Player p: players) {
            if (p != null) {
                p.getSocket().sendWinner(info);
            }
        }
    }

    void sendDataToPlayers() {
        for (Player p: players) {
            if (p != null) {
                p.getSocket().sendData();
            }
        }
    }

    void sendNewPlayer(int id) {
        for (Player p: players) {
            if (p != null) {
                p.getSocket().sendNewPlayer(id);
            }
        }
    }

    public PlayerInfo[] getPlayersInfo() {
        PlayerInfo[] res = new PlayerInfo[countPlayers];
        int cur_ind = 0;
        for (Player p: players) {
            if (p != null) {
                PlayerInfo info = p.getInfo();
                res[cur_ind] = info;
                cur_ind++;
            }
        }
        return res;
    }

    public Point[] getTargetCoords() {
        Point[] res = new Point[2];
        for (int i = 0; i < 2; i++) {
            res[i] = targets[i].getCoords();
        }
        return res;
    }

    public ArrowData[] getDataArrows() {
        ArrowData[] res = new ArrowData[countPlayers];
        int cur_ind = 0;
        for (Player p: players) {
            if (p != null) {
                ServerArrow arrow = p.getArrow();
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
                res[cur_ind] = p.getInfo();
                cur_ind++;
            }
        }
        return res;
    }

    public boolean isUniqueName(String name) {
        for (Player p : players) {
            if (p != null && Objects.equals(p.getInfo().getName(), name)) {
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
        server.unpauseGame();
    }

    public void runGame() {
        gameStatus = true;
        server.startGame();
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
