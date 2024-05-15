package com.example.shooter.messages.MsgData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArrowData {

    private Point coords;
    private boolean active;
}
