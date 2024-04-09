package com.example.shooter.game.app;

import com.example.shooter.game.model.*;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class Game {

    private final AnchorPane playingField;
    private final BigTarget bigTarget;
    private final SmallTarget smallTarget;
    private final Score score;
    private final Button startButton;
    private final Button pauseButton;
    private Arrow arrow;
    private GameStatus gameStatus = GameStatus.STOP;
    private Thread thTargets;
    private Thread thArrows;


    public void start() {
        bigTarget.goToStart();
        smallTarget.goToStart();
        score.setDefault();
        startButton.setText("Начать заново");
        pauseButton.setText("Пауза");
        gameStatus = GameStatus.RUN;
        animate();
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
        if (thTargets != null)
            thTargets.interrupt();
        thTargets = null;
        if (arrow != null)
            arrow.removeArrow(playingField);
        arrow = null;
    }

    private void animate() {
        if (thTargets != null) return;

        thTargets = new Thread(() -> {
            try {
                while (gameStatus.equals(GameStatus.RUN) || gameStatus.equals(GameStatus.PAUSE)) {
                    synchronized (this) {
                        try {
                            if (gameStatus.equals(GameStatus.PAUSE))
                                this.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    Platform.runLater(() -> {
                        bigTarget.move();
                        smallTarget.move();
                        if (arrow != null) {
                            arrow.move(playingField);
                            if (!arrow.isAlive()) {
                                arrow.removeArrow(playingField);
                                arrow = null;
                            }
                            score.plusScore(isHit());
                        }
                    });

                    Thread.sleep(25);
                }
            } catch (InterruptedException e) {}
        });
        thTargets.setDaemon(true);
        thTargets.start();
    }

    public void shoot() {
        Platform.runLater(score::plusShoot);
    }

    private int isHit() {
        // TODO дистанция считается с подобранными числами, не понятно как это делать динамически
        System.out.println("\n");
        if (arrow == null) return 0;

        double xArrow = arrow.getX();
        double yArrow = arrow.getY();

        double distanceToBigTarget = Math.sqrt(
                Math.pow(bigTarget.getY() - 0, 2) +
                        Math.pow(bigTarget.getX() - (xArrow - 390), 2)
        );
        if (distanceToBigTarget <= bigTarget.getRadius()) {
            arrow.setAlive(false);
            return 1;
        }

        double distanceToSmallTarget = Math.sqrt(
                Math.pow(smallTarget.getY() - 0, 2) +
                        Math.pow(smallTarget.getX() - (xArrow - 475), 2)
        );
        if (distanceToSmallTarget <= smallTarget.getRadius()) {
            arrow.setAlive(false);
            return 2;
        }
        return 0;
    }

    private void restartAnimate() {
        synchronized (this) {
            notifyAll();
        }
    }

    public boolean isShooting() {
        return arrow != null;
    }
}
