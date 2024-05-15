package com.example.shooter.server;

import javax.persistence.*;

@Entity
@Table(name = "player_statistic")
public class PlayerStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Column(unique = true)
    private String name;
    @Column
    private int num_wins;

    public PlayerStatistic() {}

    public PlayerStatistic(String name) {
        this.name = name;
        num_wins = 0;
    }

    public void increase_num_wins() {
        num_wins++;
    }
    public int getNum_wins() {
        return num_wins;
    }

    public String getName() {
        return name;
    }
    

    public int get_id() {
        return id;
    }
}
