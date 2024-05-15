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

    private final ServerGameInfo serverGameInfo = ServerGameInfo.getInstance();
    private final Socket ss;

    private DataInputStream dis;
    private DataOutputStream dos;

    public SocketServer(Socket ss) {
        this.ss = ss;

        try {
            OutputStream os = ss.getOutputStream();
            dos = new DataOutputStream(os);
            InputStream is = ss.getInputStream();
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
                serverGameInfo.addPlayer(serverGameInfo.getPlayerId(ss.getPort()), null);
                serverGameInfo.deleteId(ss.getPort());
                try {
                    ss.close();
                }
                catch (IOException e) {
                    log.warn("Error disconnect client" + e);
                }
                serverGameInfo.decrementCntPlayers();
                break;
            }
            if (msg.getAction() == MsgAction.SET_READY) {
                serverGameInfo.getPlayer(serverGameInfo.getPlayerId(ss.getPort())).setReady(msg.isSignal());
                if (!serverGameInfo.isGameStatus() && serverGameInfo.checkReady()) {
                    serverGameInfo.runGame();
                }
                else if (serverGameInfo.isGameStatus() && serverGameInfo.isPauseStatus() && serverGameInfo.checkReady()) {
                    serverGameInfo.unpauseGame();
                }
            }
            else if (msg.getAction() == MsgAction.SHOT) {
                Player player = serverGameInfo.getPlayer(serverGameInfo.getPlayerId(ss.getPort()));
                if (!player.getArrow().isActive()) {
                    player.getInfo().incrementShots();
                    player.getArrow().setActive(msg.isSignal());
                }
            }
            else if (msg.getAction() == MsgAction.PAUSE) {
                if (!serverGameInfo.isPauseStatus()) {
                    serverGameInfo.pauseGame();
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
            String msgStr = dis.readUTF();
            msg = gson.fromJson(msgStr, AuthMsg.class);
        }
        catch (IOException e) {
            System.out.println("Error read");
        }
        return msg;
    }

    public void sendUnready() {
        String strMsg = gson.toJson(new Msg(MsgAction.SET_UNREADY, null, null, null, null));
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            log.warn("Error send unready to player");
        }
    }

    public void sendAuthResp(AuthResponse resp) {
        String strMsg = gson.toJson(resp);
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            log.warn("Error send auth res to player");
        }
    }

    public void sendWinner(PlayerInfo info) {
        PlayerInfo[] infoData = new PlayerInfo[] {info};
        String strMsg = gson.toJson(new Msg(MsgAction.WINNER, null, null, infoData, null));
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            log.warn("Error send winner");
        }
    }

    private void sendLeaderboard() {
        List<PlayerStatistic> leaderboard = PlayerRepository.getLeaderboard();
        String strMsg = gson.toJson(new Msg(MsgAction.SEND_LEADERBOARD, null, null,
                null, leaderboard));
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            log.warn("Error send leaderboard");
        }
    }

    public void sendData() {
        ArrowData[] arrows = serverGameInfo.getDataArrows();
        String strMsg = gson.toJson(new Msg(MsgAction.SEND_DATA, serverGameInfo.getTargetCoords(), arrows,
                serverGameInfo.getDataInfo(), null));
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            log.warn("Error send data to player");
        }
    }

    public void sendNewPlayer(int id) {
        PlayerInfo[] info = serverGameInfo.getPlayersInfo();
        Msg msg = new Msg(MsgAction.NEW_PLAYER, null, null, info, null);
        msg.setNewPlayerId(id);
        String strMsg = gson.toJson(msg);
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            log.warn("Error send new player");
        }
    }

    public SignalMsg readMsg() {
        SignalMsg msg = null;
        try {
            String msgStr = dis.readUTF();
            msg = gson.fromJson(msgStr, SignalMsg.class);
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
