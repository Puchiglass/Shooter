package com.example.shooter.net.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.Socket;

@Getter
@AllArgsConstructor
public class User {

    private final int id;
    public Socket cs;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", cs=" + cs +
                '}';
    }
}
