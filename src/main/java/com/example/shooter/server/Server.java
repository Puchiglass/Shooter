package com.example.shooter.server;

import com.example.shooter.*;
import com.example.shooter.client.Player;
import com.example.shooter.messages.AuthMsg;
import com.example.shooter.messages.AuthResponse;
import com.example.shooter.messages.MsgData.Point;
import org.hibernate.Session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final GameInfo gameInfo = BModel.getModel();
    private Thread gameThread = null;

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    void run() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.close();
        gameInfo.setServer(this);
        ServerSocket ss;
        Socket cs;
        gameInfo.setTargets(new ServerTarget[]{
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
                System.out.println("Client connect - " + cs.getPort());

                SocketServer socketServer = new SocketServer(cs);
                AuthMsg msg = socketServer.readAuthMsg();
                String resultText = "";
                boolean result = true;
                if (gameInfo.getCountPlayers() >= GameInfo.MAX_PLAYERS) {
                    resultText = "Сервер заполнен!";
                    result = false;
                }
                else if (!gameInfo.isUniqueName(msg.getName())) {
                    resultText = "Игрок с таким именем уже есть. Введите другое имя.";
                    result = false;
                }
                socketServer.sendAuthResp(new AuthResponse(result, resultText));

                if (result) {
                    gameInfo.incrementCntPlayers();
                    int numOnField = gameInfo.getFreeNumOnField();
                    PlayerStatistic playerStat = PlayerRepository.getPlayerStat(msg.getName());

                    Player player = new Player(socketServer, GameInfo.arrows[numOnField], numOnField,
                            msg.getName(), playerStat);
                    gameInfo.setPlayerId(cs.getPort(), numOnField);
                    gameInfo.addPlayer(numOnField, player);
                    gameInfo.sendNewPlayer(numOnField);
                    socketServer.listenMsg();
                }
                else {
                    socketServer.close();
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Server startup error!");
        }
    }

    public void startGame() {
        gameInfo.clearStatistic();
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
        gameInfo.setGameStatus(false);
        gameInfo.setTargetToStart();
        gameInfo.sendUnreadyPlayers();
        gameThread.interrupt();
        gameThread = null;
        gameInfo.sendDataToPlayers();
    }
    void game() {
        try {
            while (gameInfo.isGameStatus()) {
                if (gameInfo.isPauseStatus()) {
                    synchronized (this) {
                        this.wait();
                    }
                }
                gameInfo.moveArrows();
                gameInfo.moveTargets();
                for (ServerTarget target : gameInfo.getTargets()) {
                    for (Player player: gameInfo.getPlayers()) {
                        if (player != null && player.getArrow().isActive() && target.checkHit(player.getArrow())) {
                            player.getArrow().remove();
                            player.getInfo().incrementScore(target.getPoints());
                            if (player.getInfo().getScore() >= AppConfig.POINTS_FOR_WIN) {
                                player.incrementNumWins();
                                PlayerRepository.increaseNumWins(player.getStat());
                                gameInfo.sendWinner(player.getInfo());
                                stopGame();
                            }
                        }
                    }
                }
                gameInfo.sendDataToPlayers();

                Thread.sleep(50);
            }
        }
        catch (InterruptedException e) {
            ;
        }
    }
}
