package com.example.shooter.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignalMsg {
    private MsgAction action;
    private boolean signal;
}
