package com.example.shooter.client;

import com.google.gson.Gson;
import com.example.shooter.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private static final Logger log = LogManager.getLogger(SocketClient.class);
    private static final Gson gson = new Gson();
    ClientGameInfo model = BClientModel.getModel();
    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    public SocketClient(Socket cs) {
        this.cs = cs;

        try {
            os = cs.getOutputStream();
            dos = new DataOutputStream(os);
            is = cs.getInputStream();
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
            Msg msg = readResponse();
            if (msg.getAction() == MsgAction.SEND_DATA) {
                model.setTargetCoords(msg.getTargets());
                model.setArrows(msg.getArrows());
                model.updateData(msg.getInfo());
            }
            else if (msg.getAction() == MsgAction.NEW_PLAYER) {
                model.addNewPlayer(msg.getInfo(), msg.getNewPlayerId());
            }
            else if (msg.getAction() == MsgAction.WINNER) {
                model.showWinner(msg.getInfo()[0]);
            }
            else if (msg.getAction() == MsgAction.SET_UNREADY) {
                model.setUnready();
            }
            else if (msg.getAction() == MsgAction.SEND_LEADERBOARD) {
                model.showLeaderboard(msg.getLeaderboard());
            }
        }
    }

    public Msg readResponse() {
        Msg msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, Msg.class);
        }
        catch (IOException e) {
            log.warn("Error read");
        }
        return msg;
    }

    public AuthResponse getAuthResponse() {
        AuthResponse msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, AuthResponse.class);
        }
        catch (IOException e) {
            log.warn("Error read");
        }
        return msg;
    }

    public void close() {
        try {
            cs.close();
        }
        catch (IOException e) {
            log.warn("Error close connect");
        }
    }

    public void sendAuthData(String name) {
        String str_msg = gson.toJson(new AuthMsg(name));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send auth data");
        }
    }

    public void sendSignal(SignalMsg msg) {
        String str_msg = gson.toJson(msg);
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            log.warn("Error send signal");
        }
    }
}
