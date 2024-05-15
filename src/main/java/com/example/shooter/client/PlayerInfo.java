package com.example.shooter.client;

import com.example.shooter.server.PlayerStatistic;

public class PlayerInfo {
    int shots;
    int score;
    int num_on_field;
    String name;
    private PlayerStatistic stat;

    public PlayerInfo() {}

    PlayerInfo(int num_on_field, String name, PlayerStatistic stat) {
        shots = 0;
        score = 0;
        this.num_on_field = num_on_field;
        this.name = name;
        this.stat = stat;
    }

    public int get_score() {
        return score;
    }

    public int get_shots() {
        return shots;
    }

    public PlayerStatistic get_stat() {
        return stat;
    }

    public void increase_num_wins() {
        stat.incrementNumWins();
    }

    public void increase_shots() {
        shots++;
    }

    public void increase_score(int points) {
        score += points;
    }

    public void clear_statistic() {
        shots = 0;
        score = 0;
    }

    public String get_name() {
        return name;
    }

    public int get_num_on_field() {
        return num_on_field;
    }
}
