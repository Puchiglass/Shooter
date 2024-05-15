package com.example.shooter.messages;

import lombok.Getter;

@Getter
public class AuthResponse {
    private final boolean result;
    private final String text;

    public AuthResponse(boolean is_connected, String text) {
        this.result = is_connected;
        this.text = text;
    }

    public boolean isConnected() {
        return result;
    }
}
