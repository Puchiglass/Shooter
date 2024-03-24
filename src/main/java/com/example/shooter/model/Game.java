package com.example.shooter.model;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Game {

    private final AnchorPane playingField;
    private final BigTarget bigTarget;
    private final SmallTarget smallTarget;
    private final Score score;
    private final Arrow arrow;
    private final Button startButton;
    private final Button pauseButton;
    private GameStatus gameStatus = GameStatus.STOP;
    private Thread thread;


    public void start() {
        bigTarget.goToStart();
        smallTarget.goToStart();
        score.setDefault();
        startButton.setText("Начать заново");
        pauseButton.setText("Пауза");
        gameStatus = GameStatus.RUN;
        moveTarget();
        restartAnimate();
    }

    public void pause() {
        if (gameStatus.equals(GameStatus.RUN)) {
            gameStatus = GameStatus.PAUSE;
            pauseButton.setText("Продолжить");
        } else if (gameStatus.equals(GameStatus.PAUSE)) {
            gameStatus = GameStatus.RUN;
            pauseButton.setText("Пауза");
            restartAnimate();
        }
    }

    public void stop() {
        gameStatus = GameStatus.STOP;
        startButton.setText("Начало игры");
        pauseButton.setText("Пауза");
        thread = null;
    }

    private void moveTarget() {
        if (thread != null) return;

        thread = new Thread(() -> {
            try {
                while (gameStatus.equals(GameStatus.RUN) || gameStatus.equals(GameStatus.PAUSE)) {
                    synchronized (this) {
                        try {
                            if (gameStatus.equals(GameStatus.PAUSE))
                                this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    bigTarget.move();
                    smallTarget.move();
                    if (arrow != null)
                        arrow.move();

                    Thread.sleep(25);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void restartAnimate() {
        synchronized (this) {
            notifyAll();
        }
    }
}
