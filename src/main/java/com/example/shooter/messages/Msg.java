package com.example.shooter.messages;

import com.example.shooter.client.PlayerInfo;
import com.example.shooter.messages.MsgData.ArrowData;
import com.example.shooter.messages.MsgData.Point;
import com.example.shooter.server.PlayerStatistic;

import java.util.List;

public class Msg {
    MsgAction action;
    Point[] targets;

    ArrowData[] arrows;
    PlayerInfo[] info;
    List<PlayerStatistic> leaderboard;

    int new_player_id;

    public Msg(MsgAction action, Point[] targets, ArrowData[] arrows, PlayerInfo[] info,
               List<PlayerStatistic> leaderboard) {
        this.action = action;
        this.targets = targets;
        this.arrows = arrows;
        this.info = info;
        this.leaderboard = leaderboard;
    }

    public MsgAction get_action() {
        return action;
    }

    public Point[] get_target() {
        return targets;
    }

    public ArrowData[] get_arrows() {
        return arrows;
    }

    public PlayerInfo[] get_info() {
        return info;
    }

    public List<PlayerStatistic> get_leaderboard() {
        return leaderboard;
    }

    public void set_new_player_id(int id) {
        new_player_id = id;
    }

    public int get_new_player_id() {
        return new_player_id;
    }
}
