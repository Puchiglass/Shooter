package com.example.shooter.server;

import com.google.gson.Gson;
import com.example.shooter.client.Player;
import com.example.shooter.client.PlayerInfo;
import com.example.shooter.messages.*;
import com.example.shooter.messages.MsgData.ArrowData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SocketServer {
    private static final Logger log = LogManager.getLogger(SocketServer.class);
    private static final Gson gson = new Gson();
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
            log.warn("Error SocketClient");
        }
    }

    public void listenMsg() {
        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    void run() {
        while (true) {
            SignalMsg msg = readMsg();
            if (msg == null) {
                gameInfo.addPlayer(gameInfo.getPlayerId(ss.getPort()), null);
                gameInfo.deleteId(ss.getPort());
                try {
                    ss.close();
                }
                catch (IOException e) {
                    log.warn("Error disconnect client" + e);
                }
                gameInfo.decrementCntPlayers();
                break;
            }
            if (msg.getAction() == MsgAction.SET_READY) {
                gameInfo.getPlayer(gameInfo.getPlayerId(ss.getPort())).setReady(msg.isSignal());
                if (!gameInfo.isGameStatus() && gameInfo.checkReady()) {
                    gameInfo.run_game();
                }
                else if (gameInfo.isGameStatus() && gameInfo.isPauseStatus() && gameInfo.checkReady()) {
                    gameInfo.unpauseGame();
                }
            }
            else if (msg.getAction() == MsgAction.SHOT) {
                Player player = gameInfo.getPlayer(gameInfo.getPlayerId(ss.getPort()));
                if (!player.getArrow().isActive()) {
                    player.getInfo().incrementShots();
                    player.getArrow().setActive(msg.isSignal());
                }
            }
            else if (msg.getAction() == MsgAction.PAUSE) {
                if (!gameInfo.isPauseStatus()) {
                    gameInfo.pauseGame();
                }
            }
            else if (msg.getAction() == MsgAction.GET_LEADERBOARD) {
                sendLeaderboard();
            }

        }
    }

    public AuthMsg readAuthMsg() {
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

    public void sendUnready() {
        String str_msg = gson.toJson(new Msg(MsgAction.SET_UNREADY, null, null, null, null));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send unready to player");
        }
    }

    public void sendAuthResp(AuthResponse resp) {
        String str_msg = gson.toJson(resp);
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send auth res to player");
        }
    }

    public void sendWinner(PlayerInfo info) {
        PlayerInfo[] info_data = new PlayerInfo[] {info};
        String str_msg = gson.toJson(new Msg(MsgAction.WINNER, null, null, info_data, null));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send winner");
        }
    }

    private void sendLeaderboard() {
        List<PlayerStatistic> leaderboard = PlayerRepository.getLeaderboard();
        String str_msg = gson.toJson(new Msg(MsgAction.SEND_LEADERBOARD, null, null,
                null, leaderboard));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send leaderboard");
        }
    }

    public void sendData() {
        ArrowData[] arrows = gameInfo.getDataArrows();
        String str_msg = gson.toJson(new Msg(MsgAction.SEND_DATA, gameInfo.getTargetCoords(), arrows,
                gameInfo.getDataInfo(), null));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send data to player");
        }
    }

    public void sendNewPlayer(int id) {
        PlayerInfo[] info = gameInfo.getPlayersInfo();
        Msg msg = new Msg(MsgAction.NEW_PLAYER, null, null, info, null);
        msg.setNewPlayerId(id);
        String str_msg = gson.toJson(msg);
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send new player");
        }
    }

    public SignalMsg readMsg() {
        SignalMsg msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, SignalMsg.class);
        }
        catch (IOException e) {
            log.warn("Client disconnect - " + ss.getPort());
        }
        return msg;
    }

    public void close() {
        try {
            log.info("Client disconnect - " + ss.getPort());
            ss.close();
        }
        catch (IOException e) {
            log.warn("Error close connect");
        }
    }
}
