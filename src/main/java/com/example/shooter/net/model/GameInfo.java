package com.example.shooter.net.model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameInfo {
    @Expose
    private int score;
    @Expose
    private int shots;

    @Override
    public String toString() {
        return "{" +
                "score=" + score +
                ", shots=" + shots +
                '}';
    }
}
