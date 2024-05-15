package com.example.shooter.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.shooter.AppConfig;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), AppConfig.WIDTH,  AppConfig.HEIGHT);
        stage.setMinHeight(AppConfig.HEIGHT);
        stage.setMinWidth(AppConfig.WIDTH);
        stage.setTitle(AppConfig.TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}