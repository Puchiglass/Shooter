package com.example.shooter.messages;

public class AuthResponse {
    boolean result;
    String text;

    public AuthResponse(boolean is_connected, String text) {
        result = is_connected;
        this.text = text;
    }

    public boolean is_connected() {
        return result;
    }

    public String get_text() {
        return text;
    }
}
