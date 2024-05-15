package com.example.shooter.client;

import com.google.gson.Gson;
import com.example.shooter.messages.*;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    Gson gson = new Gson();
    ClientModel model = BClientModel.get_model();
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
            Msg msg = read_response();
            if (msg.get_action() == MsgAction.SEND_DATA) {
                model.set_target_coords(msg.get_target());
                model.set_arrows(msg.get_arrows());
                model.update_data(msg.get_info());
            }
            else if (msg.get_action() == MsgAction.NEW_PLAYER) {
                model.add_new_player(msg.get_info(), msg.get_new_player_id());
            }
            else if (msg.get_action() == MsgAction.WINNER) {
                model.show_winner(msg.get_info()[0]);
            }
            else if (msg.get_action() == MsgAction.SET_UNREADY) {
                model.set_unready();
            }
            else if (msg.get_action() == MsgAction.SEND_LEADERBOARD) {
                model.show_leaderboard(msg.get_leaderboard());
            }
        }
    }

    public Msg read_response() {
        Msg msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, Msg.class);
        }
        catch (IOException e) {
            System.out.println("Error read");
        }
        return msg;
    }

    public AuthResponse get_auth_response() {
        AuthResponse msg = null;
        try {
            String msg_str = dis.readUTF();
            msg = gson.fromJson(msg_str, AuthResponse.class);
        }
        catch (IOException e) {
            System.out.println("Error read");
        }
        return msg;
    }

    public void close() {
        try {
            cs.close();
        }
        catch (IOException e) {
            System.out.println("Error close connect");
        }
    }

    public void send_auth_data(String name) {
        String str_msg = gson.toJson(new AuthMsg(name));
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send auth data");
        }
    }

    public void send_signal(SignalMsg msg) {
        String str_msg = gson.toJson(msg);
        try {
            dos.writeUTF(str_msg);
        } catch (IOException e) {
            System.out.println("Error send data to player");
        }
    }




}
