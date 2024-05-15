package com.example.shooter.server;

import com.example.shooter.*;
import com.example.shooter.client.Player;
import com.example.shooter.client.PlayerInfo;
import com.example.shooter.messages.MsgData.ArrowData;
import com.example.shooter.messages.MsgData.Point;

import java.util.HashMap;
import java.util.Objects;

public class Model {
    Server server = null;

    public static final ServerArrow[] arrows = {
            new ServerArrow(new Point(0, 65)),
            new ServerArrow(new Point(0, 200)),
            new ServerArrow(new Point(0, 335)),
            new ServerArrow(new Point(0, 470))
    };
    ServerTarget[] targets;
    static final int max_players = AppConfig.MAX_PLAYERS;

    boolean game_status = false;

    boolean pause_status = false;

    int cnt_players = 0;

    HashMap<Integer, Integer> player_id_by_port = new HashMap<>();
    Player[] players = new Player[max_players];

    void add_player(int id, Player player) {
        players[id] = player;
    }

    void delete_id(int port) {
        player_id_by_port.remove(port);
    }

    boolean check_ready() {
        for (Player pl: players) {
            if (pl != null && !pl.is_ready()) {
                return false;
            }
        }
        return true;
    }

    void move_arrows() {
        for (Player player: players) {
            if (player != null && player.get_arrow().is_active()) {
                player.get_arrow().move();
            }
        }
    }

    void move_targets() {
        for (ServerTarget target : targets) {
            target.move();
        }
    }

    public void set_target_start_coords() {
        for (ServerTarget target : targets) {
            target.set_start_coords();
        }
    }

    public void clear_statistic() {
        for (Player player: players) {
            if (player != null) {
                player.get_info().clear_statistic();
            }
        }
    }

    public void send_unready_players() {
        for (Player player: players) {
            if (player != null) {
                player.set_ready(false);
                player.get_socket().send_unready();
            }
        }
    }

    public void send_winner(PlayerInfo info) {
        for (Player player: players) {
            if (player != null) {
                player.get_socket().send_winner(info);
            }
        }
    }

    void send_data_to_players() {
        for (Player player: players) {
            if (player != null) {
                player.get_socket().send_data();
            }
        }
    }

    void send_new_player(int id) {
        for (Player player: players) {
            if (player != null) {
                player.get_socket().send_new_player(id);
            }
        }
    }

    public PlayerInfo[] get_players_info() {
        PlayerInfo[] res = new PlayerInfo[cnt_players];
        int cur_ind = 0;
        for (Player player: players) {
            if (player != null) {
                PlayerInfo info = player.get_info();
                res[cur_ind] = info;
                cur_ind++;
            }
        }
        return res;
    }

    public Point[] get_target_coords() {
        Point[] res = new Point[2];
        for (int i = 0; i < 2; i++) {
            res[i] = targets[i].get_coords();
        }
        return res;
    }

    public ArrowData[] get_data_arrows() {
        ArrowData[] res = new ArrowData[cnt_players];
        int cur_ind = 0;
        for (Player player: players) {
            if (player != null) {
                ServerArrow arrow = player.get_arrow();
                res[cur_ind] = new ArrowData(arrow.get_coords(), arrow.is_active());
                cur_ind++;
            }
        }
        return res;
    }

    public PlayerInfo[] get_data_info() {
        PlayerInfo[] res = new PlayerInfo[cnt_players];
        int cur_ind = 0;
        for (Player player: players) {
            if (player != null) {
                res[cur_ind] = player.get_info();
                cur_ind++;
            }
        }
        return res;
    }

    public boolean is_unique_name(String name) {
        for (Player player : players) {
            if (player != null && Objects.equals(player.get_info().get_name(), name)) {
                return false;
            }
        }
        return true;
    }

    public boolean is_pause() {
        return pause_status;
    }

    public void pause_game() {
        pause_status = true;
        send_unready_players();
    }

    public void unpause_game() {
        pause_status = false;
        server.unpause_game();
    }

    public void run_game() {
        game_status = true;
        server.start_game();
    }

    public ServerTarget[] get_targets() {
        return targets;
    }

    public void set_server(Server server) {
        this.server = server;
    }

    public void set_target(ServerTarget[] target) {
        this.targets = target;
    }

    public Player get_player(int id) {
        return players[id];
    }

    public Player[] get_players() {
        return players;
    }

    public void set_player_id(int port, int id) {
        player_id_by_port.put(port, id);
    }

    public int get_player_id(int port) {
        return player_id_by_port.get(port);
    }

    public int get_cnt_players() {
        return cnt_players;
    }

    public void increase_cnt_players() {
        cnt_players++;
    }

    public void decrease_cnt_players() {
        cnt_players--;
        if (cnt_players == 0) {
            game_status = false;
            pause_status = false;
        }
    }

    public static int get_max_players() {
        return max_players;
    }

    public void set_is_run_game(boolean val) {
        game_status = val;
    }

    public boolean is_run_game() {
        return game_status;
    }

    public int get_free_num_on_field() {
        for (int i = 0; i < max_players; i++) {
            if (players[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
