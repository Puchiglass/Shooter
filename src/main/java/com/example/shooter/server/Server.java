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
    GameInfo gameInfo = BModel.getModel();
    Thread game_thread = null;
    int port = 5588;
    InetAddress ip = null;

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
                        AppConfig.BIG_TARGET_DISTANCE,
                        AppConfig.BIG_TARGET_SPEED,
                        AppConfig.BIG_TARGET_POINTS_FOR_HIT),
                new ServerTarget(new Point(675, 270),
                        AppConfig.SMALL_TARGET_RADIUS,
                        AppConfig.SMALL_TARGET_DISTANCE,
                        AppConfig.SMALL_TARGET_SPEED,
                        AppConfig.SMALL_TARGET_POINTS_FOR_HIT),
        });

        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 0, ip);

            while (true) {
                cs = ss.accept();
                System.out.println("Client connect - " + cs.getPort());

                SocketServer server_socket = new SocketServer(cs);
                AuthMsg msg = server_socket.read_auth_msg();
                String result_text = "";
                boolean result = true;
                if (gameInfo.getCountPlayers() >= GameInfo.MAX_PLAYERS) {
                    result_text = "Сервер заполнен!";
                    result = false;
                }
                else if (!gameInfo.isUniqueName(msg.getName())) {
                    result_text = "Игрок с таким именем уже есть. Введите другое имя.";
                    result = false;
                }
                server_socket.send_auth_resp(new AuthResponse(result, result_text));

                if (result) {
                    gameInfo.incrementCntPlayers();
                    int num_on_field = gameInfo.getFreeNumOnField();
                    PlayerStatistic player_stat = PlayerRepository.getPlayerStat(msg.getName());

                    Player player = new Player(server_socket, GameInfo.arrows[num_on_field], num_on_field,
                            msg.getName(), player_stat);
                    gameInfo.setPlayerId(cs.getPort(), num_on_field);
                    gameInfo.addPlayer(num_on_field, player);
                    gameInfo.sendNewPlayer(num_on_field);
                    server_socket.listen_msg();
                }
                else {
                    server_socket.close();
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Server startup error!");
        }
    }

    public void start_game() {
        gameInfo.clearStatistic();
        game_thread = new Thread(this::game);
        game_thread.setDaemon(true);
        game_thread.start();
    }

    public void unpause_game() {
        synchronized (this) {
            notifyAll();
        }
    }

    void stop_game() {
        gameInfo.setGameStatus(false);
        gameInfo.setTargetToStart();
        gameInfo.sendUnreadyPlayers();
        game_thread.interrupt();
        game_thread = null;
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
                        if (player != null && player.get_arrow().isActive() && target.check_hit(player.get_arrow())) {
                            player.get_arrow().remove();
                            player.get_info().increase_score(target.get_points_for_hit());
                            if (player.get_info().get_score() >= AppConfig.POINTS_FOR_WIN) {
                                player.increase_num_wins();
                                PlayerRepository.increaseNumWins(player.get_stat());
                                gameInfo.sendWinner(player.get_info());
                                stop_game();
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
