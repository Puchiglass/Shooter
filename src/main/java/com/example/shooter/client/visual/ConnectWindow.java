package com.example.shooter.client.visual;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.shooter.client.BClientModel;
import com.example.shooter.client.ClientModel;
import com.example.shooter.client.SocketClient;
import com.example.shooter.messages.AuthResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectWindow {
    ClientModel model = BClientModel.get_model();
    TextField name_input;
    Label error_label;

    Stage window;

    int port = 5588;

    public void show() {
        window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 300, 200);
        window.setScene(scene);
        window.setTitle("Подключение");

        Label name_label = new Label("Имя");
        vBox.getChildren().add(name_label);

        name_input = new TextField();
        vBox.getChildren().add(name_input);
        name_input.setMaxSize(100, 20);

        error_label = new Label();
        vBox.getChildren().add(error_label);

        Button connect_button = new Button("Подключиться");
        connect_button.setOnAction(event->connect());
        vBox.getChildren().add(connect_button);
        window.showAndWait();
    }

    private void connect() {
        if (model.cls != null) {
            return;
        }
        try {
            Socket cs;
            InetAddress ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            model.cls = new SocketClient(cs);
            model.cls.send_auth_data(name_input.getText());

            AuthResponse resp = model.cls.get_auth_response();
            if (resp.isConnected()) {
                model.cls.listen_msg();
                window.close();
            }
            else {
                error_label.setText(resp.getText());
                model.cls.close();
                model.cls = null;
            }

        }
        catch (IOException e) {
            error_label.setText("Ошибка подключения к серверу");
            System.out.println("Error connect");
        }
    }

}
