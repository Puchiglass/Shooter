package com.example.shooter.net.server;

import com.example.shooter.net.model.GameInfo;
import com.example.shooter.net.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Server {

    private final static Server server = new Server();
    private static final Logger log = LogManager.getLogger(Server.class);
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private InetAddress ip = null;
    private final int port = 3124;
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket ss = server.startServer();
        try {
            int i = 0;
            while (i < 2) {
                User user = server.getNewUser(ss);
                users.add(user);
                log.info("New user connected ({})", user);
                i++;
            }
            GameInfo gameInfo = new GameInfo(3, 5);
            for (User user : users) {
                DataOutputStream dos = new DataOutputStream(user.getCs().getOutputStream());
                dos.writeUTF(gson.toJson(gameInfo));
                log.info("Game info send to user ({})", user);
            }

//                Socket cs = ss.accept();
//                DataInputStream dis = new DataInputStream(cs.getInputStream());
//                DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
//                log.info("Client connected ({})", cs.getPort());

//                String json = (String) dis.readUTF();
//                Test test = gson.fromJson(json, Test.class);
//                log.info("Connect: {}", test.getS());
//
//                Test testResponse = new Test("Hello, client!");
//                dos.writeUTF(gson.toJson(testResponse));
//                log.info("Message send to client");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User getNewUser(ServerSocket ss) {
        Socket cs = null;
        try {
            cs = ss.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new User(users.size() + 1, cs);
    }

    private ServerSocket startServer() {
        ServerSocket ss;

        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 4, ip);
            log.info("Server start on port {}", port);

            return ss;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
