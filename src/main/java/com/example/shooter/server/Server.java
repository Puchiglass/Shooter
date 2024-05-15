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
    Model model = BModel.get_model();
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
        model.set_server(this);
        ServerSocket ss;
        Socket cs;
        model.set_target(new ServerTarget[]{
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
                if (model.get_cnt_players() >= Model.get_max_players()) {
                    result_text = "Сервер заполнен!";
                    result = false;
                }
                else if (!model.is_unique_name(msg.get_name())) {
                    result_text = "Игрок с таким именем уже есть. Введите другое имя.";
                    result = false;
                }
                server_socket.send_auth_resp(new AuthResponse(result, result_text));

                if (result) {
                    model.increase_cnt_players();
                    int num_on_field = model.get_free_num_on_field();
                    PlayerStatistic player_stat = DB.get_player_stat(msg.get_name());

                    Player player = new Player(server_socket, Model.arrows[num_on_field], num_on_field,
                            msg.get_name(), player_stat);
                    model.set_player_id(cs.getPort(), num_on_field);
                    model.add_player(num_on_field, player);
                    model.send_new_player(num_on_field);
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
        model.clear_statistic();
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
        model.set_is_run_game(false);
        model.set_target_start_coords();
        model.send_unready_players();
        game_thread.interrupt();
        game_thread = null;
        model.send_data_to_players();
    }
    void game() {
        try {
            while (model.game_status) {
                if (model.is_pause()) {
                    synchronized (this) {
                        this.wait();
                    }
                }
                model.move_arrows();
                model.move_targets();
                for (ServerTarget target : model.get_targets()) {
                    for (Player player: model.get_players()) {
                        if (player != null && player.get_arrow().is_active() && target.check_hit(player.get_arrow())) {
                            player.get_arrow().remove();
                            player.get_info().increase_score(target.get_points_for_hit());
                            if (player.get_info().get_score() >= AppConfig.POINTS_FOR_WIN) {
                                player.increase_num_wins();
                                DB.increase_num_wins(player.get_stat());
                                model.send_winner(player.get_info());
                                stop_game();
                            }
                        }
                    }
                }
                model.send_data_to_players();

                Thread.sleep(50);
            }
        }
        catch (InterruptedException e) {
            ;
        }
    }
}
