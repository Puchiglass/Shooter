package com.example.shooter.client;

public class BClientModel {
    static final ClientGameInfo gameInfo = new ClientGameInfo();
    public static ClientGameInfo getModel() {
        return gameInfo;
    }
}
