package com.example.shooter.server;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "player_statistic")
@NoArgsConstructor
@Getter
public class PlayerStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(unique = true)
    private String name;

    @Column(name = "num_wins")
    private int numWins = 0;


    public void incrementNumWins() {
        numWins++;
    }

    public PlayerStatistic(String name) {
        this.name = name;
        numWins = 0;
    }
}
