package com.example.shooter.client.visual;

import com.example.shooter.AppConfig;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.shooter.client.BClientModel;
import com.example.shooter.client.ClientGameInfo;
import com.example.shooter.client.SocketClient;
import com.example.shooter.messages.AuthResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectWindow {
    private static final Logger log = LogManager.getLogger(ConnectWindow.class);
    private final ClientGameInfo model = BClientModel.getModel();
    private TextField name_input;
    private Label error_label;

    private Stage window;

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
            cs = new Socket(ip, AppConfig.PORT);
            model.cls = new SocketClient(cs);
            model.cls.sendAuthData(name_input.getText());

            AuthResponse resp = model.cls.getAuthResponse();
            if (resp.isConnected()) {
                model.cls.listenMsg();
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
            log.warn("Error connect", e);
        }
    }

}
