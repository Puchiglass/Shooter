package com.example.shooter.client;

import com.example.shooter.server.PlayerStatistic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerInfo {
    private final int numOnField;
    private final String name;
    private final PlayerStatistic statistic;

    private int shots = 0;
    private int score = 0;

    public void incrementNumWins() {
        statistic.incrementNumWins();
    }

    public void incrementShots() {
        shots++;
    }

    public void incrementScore(int points) {
        score += points;
    }

    public void clearStatistic() {
        shots = 0;
        score = 0;
    }
}
