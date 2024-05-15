package com.example.shooter.messages;

public class AuthMsg {
    String name;
    public AuthMsg(String name) {
        this.name = name;
    }

    public String get_name() {
        return name;
    }
}
