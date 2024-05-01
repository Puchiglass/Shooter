package com.example.shooter.net.client;

import com.example.shooter.net.model.GameInfo;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

    private static final Logger log = LogManager.getLogger(Client.class);
    private static final Gson gson = new Gson();

    int port = 3124;
    InetAddress ip = null;

    public Socket clientStart() {
        Socket cs;
        try {
            ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            log.info("Client start on port {}", port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cs;
    }

    public static void main(String[] args) {
        Socket cs = new Client().clientStart();
        log.info("Client connected to server ({})", cs.getPort());

        try {
            InputStream is = cs.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            String json = dis.readUTF();
            GameInfo gameInfo = gson.fromJson(json, GameInfo.class);
            log.info("Game info from server: {}", gameInfo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
