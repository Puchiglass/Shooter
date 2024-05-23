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
    private final ClientGameInfo model = ClientGameInfo.getInstance();
    private TextField nameInput;
    private Label errorLabel;

    private Stage window;

    public void show() {
        window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 300, 200);
        window.setScene(scene);
        window.setTitle("Подключение");

        Label nameLabel = new Label("Имя");
        vBox.getChildren().add(nameLabel);

        nameInput = new TextField();
        vBox.getChildren().add(nameInput);
        nameInput.setMaxSize(100, 20);

        errorLabel = new Label();
        vBox.getChildren().add(errorLabel);

        Button connectButton = new Button("Подключиться");
        connectButton.setOnAction(event->connect());
        vBox.getChildren().add(connectButton);
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
            model.cls.sendAuthData(nameInput.getText());

            AuthResponse resp = model.cls.getAuthResponse();
            if (resp.isConnected()) {
                model.cls.listenMsg();
                window.close();
            }
            else {
                errorLabel.setText(resp.getText());
                model.cls.close();
                model.cls = null;
            }

        }
        catch (IOException e) {
            errorLabel.setText("Ошибка подключения к серверу");
            log.warn("Error connect", e);
        }
    }

}
