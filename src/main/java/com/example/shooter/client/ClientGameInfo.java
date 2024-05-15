package com.example.shooter.client;

import com.example.shooter.messages.MsgData.ArrowData;
import com.example.shooter.messages.MsgData.Point;
import com.example.shooter.server.PlayerStatistic;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientGameInfo {

    public SocketClient cls = null;
    public MainController mc;

    private Point[] targetCoords;
    private ArrowData[] arrows;
    private int playerId = -1;

    public void updateData(PlayerInfo[] infos) {
        mc.update_data(infos);
    }

    public void setUnready() {
        mc.unready();
    }

    public void addNewPlayer(PlayerInfo[] infos, int new_player_id) {
        if (playerId == - 1) {
            playerId = new_player_id;
        }
        for (PlayerInfo info : infos) {
            mc.add_new_player(info);
        }
    }

    public void showWinner(PlayerInfo info) {
        mc.show_winner(info);
    }

    public void showLeaderboard(List<PlayerStatistic> leaderboard) {
        mc.show_leaderboard(leaderboard);
    }
}
