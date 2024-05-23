package com.example.shooter.client.visual;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.shooter.client.PlayerInfo;

public class WinnerWindow {
    public static void show(PlayerInfo info) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 300, 200);
        window.setScene(scene);
        window.setTitle("Игра закончилась");

        Label winnerLabel = new Label("Победитель");
        vBox.getChildren().add(winnerLabel);

        Label winnerName = new Label(info.getName());
        vBox.getChildren().add(winnerName);

        Button connectButton = new Button("Продолжить");
        connectButton.setOnAction(event-> window.close());
        vBox.getChildren().add(connectButton);
        window.showAndWait();
    }

}

