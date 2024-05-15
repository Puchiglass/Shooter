package com.example.shooter.server;

import com.google.gson.Gson;
import com.example.shooter.client.Player;
import com.example.shooter.client.PlayerInfo;
import com.example.shooter.messages.*;
import com.example.shooter.messages.MsgData.ArrowData;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SocketServer {
    Gson gson = new Gson();
    GameInfo gameInfo = BModel.getModel();
    Socket ss;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    public SocketServer(Socket ss) {
        this.ss = ss;

        try {
            os = ss.getOutputStream();
            dos = new DataOutputStream(os);
            is = ss.getInputStream();
            dis = new DataInputStream(is);
        }
        catch (IOException e) {
            System.out.println("Error SocketClient");
        }
    }

    public void listen_msg() {
        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    void run() {
        while (true) {
            SignalMsg msg = read_msg();
            if (msg == null) {
                gameInfo.addPlayer(gameInfo.getPlayerId(ss.getPort()), null);
                gameInfo.deleteId(ss.getPort());
                try {
                    ss.close();
                }
                catch (IOException e) {
                    System.out.println("Error disconnect client" + e);
                }
                gameInfo.decrementCntPlayers();
                break;
            }
            if (msg.get_action() == MsgAction.SET_READY) {
                gameInfo.getPlayer(gameInfo.getPlayerId(ss.getPort())).set_ready(msg.get_signal());
                if (!gameInfo.isGameStatus() && gameInfo.checkReady()) {
                    gameInfo.run_game();
                }
                else if (gameInfo.isGameStatus() && gameInfo.isPauseStatus() && gameInfo.checkReady()) {
                    gameInfo.unpauseGame();
                }
            }
            else if (msg.get_action() == MsgAction.SHOT) {
                Player player = gameInfo.getPlayer(gameInfo.getPlayerId(ss.getPort()));
                if (!player.get_arrow().isActive()) {
                    player.get_info().increase_shots();
                    player.get_arrow().setActive(msg.get_signal());
                }
            }
            else if (msg.get_action() == MsgAction.PAUSE) {
                if (!gameInfo.isPauseStatus()) {
                    gameInfo.pauseGame();
                }
            }
            else if (msg.get_action() == MsgAction.GET_LEADERBOARD) {
                send_leaderboard();
            }

        }
    }

    public AuthMsg read_auth_msg() {
        AuthMsg msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, AuthMsg.class);
        }
        catch (IOException e) {
            System.out.println("Error read");
        }
        return msg;
    }

    public void send_unready() {
        String str_msg = gson.toJson(new Msg(MsgAction.SET_UNREADY, null, null, null, null));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send unready to player");
        }
    }

    public void send_auth_resp(AuthResponse resp) {
        String str_msg = gson.toJson(resp);
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send auth res to player");
        }
    }

    public void send_winner(PlayerInfo info) {
        PlayerInfo[] info_data = new PlayerInfo[] {info};
        String str_msg = gson.toJson(new Msg(MsgAction.WINNER, null, null, info_data, null));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send winner");
        }
    }

    private void send_leaderboard() {
        List<PlayerStatistic> leaderboard = PlayerRepository.getLeaderboard();
        String str_msg = gson.toJson(new Msg(MsgAction.SEND_LEADERBOARD, null, null,
                null, leaderboard));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send leaderboard");
        }
    }

    public void send_data() {
        ArrowData[] arrows = gameInfo.getDataArrows();
        String str_msg = gson.toJson(new Msg(MsgAction.SEND_DATA, gameInfo.getTargetCoords(), arrows,
                gameInfo.getDataInfo(), null));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send data to player");
        }
    }

    public void send_new_player(int id) {
        PlayerInfo[] info = gameInfo.getPlayersInfo();
        Msg msg = new Msg(MsgAction.NEW_PLAYER, null, null, info, null);
        msg.set_new_player_id(id);
        String str_msg = gson.toJson(msg);
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send new player");
        }
    }

    public SignalMsg read_msg() {
        SignalMsg msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, SignalMsg.class);
        }
        catch (IOException e) {
            System.out.println("Client disconnect - " + ss.getPort());
        }
        return msg;
    }

    public void close() {
        try {
            System.out.println("Client disconnect - " + ss.getPort());
            ss.close();
        }
        catch (IOException e) {
            System.out.println("Error close connect");
        }
    }
}
