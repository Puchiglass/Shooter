package com.example.shooter.server;

import com.example.shooter.*;
import com.example.shooter.client.Player;
import com.example.shooter.client.SocketClient;
import com.example.shooter.messages.AuthMsg;
import com.example.shooter.messages.AuthResponse;
import com.example.shooter.messages.MsgData.Point;
import org.hibernate.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Logger log = LogManager.getLogger(SocketClient.class);

    private final ServerGameInfo serverGameInfo = ServerGameInfo.getInstance();
    private Thread gameThread = null;

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    void run() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.close();
        serverGameInfo.setServer(this);
        ServerSocket ss;
        Socket cs;
        serverGameInfo.setTargets(new ServerTarget[]{
                new ServerTarget(new Point(475, 270),
                        AppConfig.BIG_TARGET_RADIUS,
                        AppConfig.BIG_TARGET_SPEED,
                        AppConfig.BIG_TARGET_POINTS_FOR_HIT,
                        AppConfig.BIG_TARGET_DISTANCE),
                new ServerTarget(new Point(675, 270),
                        AppConfig.SMALL_TARGET_RADIUS,
                        AppConfig.SMALL_TARGET_SPEED,
                        AppConfig.SMALL_TARGET_POINTS_FOR_HIT,
                        AppConfig.SMALL_TARGET_DISTANCE),
        });

        try {
            InetAddress ip = InetAddress.getLocalHost();
            ss = new ServerSocket(AppConfig.PORT, 0, ip);

            while (true) {
                cs = ss.accept();
                log.info("Client connect - " + cs.getPort());

                SocketServer socketServer = new SocketServer(cs);
                AuthMsg msg = socketServer.readAuthMsg();
                String resultText = "";
                boolean result = true;
                if (serverGameInfo.getCountPlayers() >= ServerGameInfo.MAX_PLAYERS) {
                    resultText = "Сервер заполнен!";
                    result = false;
                }
                else if (!serverGameInfo.isUniqueName(msg.getName())) {
                    resultText = "Игрок с таким именем уже есть. Введите другое имя.";
                    result = false;
                }
                socketServer.sendAuthResp(new AuthResponse(result, resultText));

                if (result) {
                    serverGameInfo.incrementCntPlayers();
                    int numOnField = serverGameInfo.getFreeNumOnField();
                    PlayerStatistic playerStat = PlayerRepository.getPlayerStat(msg.getName());

                    Player player = new Player(socketServer, ServerGameInfo.arrows[numOnField], numOnField,
                            msg.getName(), playerStat);
                    serverGameInfo.setPlayerId(cs.getPort(), numOnField);
                    serverGameInfo.addPlayer(numOnField, player);
                    serverGameInfo.sendNewPlayer(numOnField);
                    socketServer.listenMsg();
                }
                else {
                    socketServer.close();
                }
            }
        }
        catch (IOException ex) {
            log.warn("Server startup error!");
        }
    }

    public void startGame() {
        serverGameInfo.clearStatistic();
        gameThread = new Thread(this::game);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void unpauseGame() {
        synchronized (this) {
            notifyAll();
        }
    }

    void stopGame() {
        serverGameInfo.setGameStatus(false);
        serverGameInfo.setTargetToStart();
        serverGameInfo.sendUnreadyPlayers();
        gameThread.interrupt();
        gameThread = null;
        serverGameInfo.sendDataToPlayers();
    }
    void game() {
        try {
            while (serverGameInfo.isGameStatus()) {
                if (serverGameInfo.isPauseStatus()) {
                    synchronized (this) {
                        this.wait();
                    }
                }
                serverGameInfo.moveArrows();
                serverGameInfo.moveTargets();
                for (ServerTarget target : serverGameInfo.getTargets()) {
                    for (Player player: serverGameInfo.getPlayers()) {
                        if (player != null && player.getArrow().isActive() && target.checkHit(player.getArrow())) {
                            player.getArrow().remove();
                            player.getInfo().incrementScore(target.getPoints());
                            if (player.getInfo().getScore() >= AppConfig.POINTS_FOR_WIN) {
                                player.incrementNumWins();
                                PlayerRepository.increaseNumWins(player.getStat());
                                serverGameInfo.sendWinner(player.getInfo());
                                stopGame();
                            }
                        }
                    }
                }
                serverGameInfo.sendDataToPlayers();

                Thread.sleep(50);
            }
        }
        catch (InterruptedException e) {
            ;
        }
    }
}
