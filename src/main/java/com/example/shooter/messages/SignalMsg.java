package com.example.shooter.messages;

public class SignalMsg {
    MsgAction action;
    boolean signal;

    public SignalMsg(MsgAction action, boolean signal) {
        this.action = action;
        this.signal = signal;
    }

    public MsgAction get_action() {
        return action;
    }

    public boolean get_signal() {
        return signal;
    }
}
