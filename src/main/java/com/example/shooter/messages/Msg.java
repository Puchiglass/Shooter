package com.example.shooter.messages;

import com.example.shooter.client.PlayerInfo;
import com.example.shooter.messages.MsgData.ArrowData;
import com.example.shooter.messages.MsgData.Point;
import com.example.shooter.server.PlayerStatistic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class Msg {
    private final MsgAction action;
    private final Point[] targets;
    private final ArrowData[] arrows;
    private final PlayerInfo[] info;
    private final List<PlayerStatistic> leaderboard;

    private int newPlayerId;
}
