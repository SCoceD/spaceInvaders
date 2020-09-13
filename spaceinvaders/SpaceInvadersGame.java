package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import com.javarush.games.spaceinvaders.gameobjects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    public static final int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private static final int PLAYER_BULLETS_MAX = 1;
    public int frameCount;
    private int score;


    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        score = 0;
        playerBullets = new ArrayList<Bullet>();
        playerShip = new PlayerShip();
        enemyBullets = new ArrayList<Bullet>();
        enemyFleet = new EnemyFleet();
        isGameStopped = false;
        animationsCount = 0;
        setTurnTimer(40);
        createStars();
        drawScene();
    }

    private void drawScene() {
        drawField();
        for (Bullet bullet : playerBullets) bullet.draw(this);
        playerShip.draw(this);
        enemyFleet.draw(this);
        for (Bullet bullet : enemyBullets) bullet.draw(this);
    }

    private void drawField() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellValueEx(i, j, Color.BLACK, "");
            }
        }
        for (Star s : stars) s.draw(this);
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        for (Bullet bullet : enemyBullets) bullet.move();
        playerShip.move();
        for (Bullet bullet : playerBullets) bullet.move();
    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 5, j = 5; i < 29; i += 3, j += 5) {
            stars.add(new Star((double) i, (double) j));
        }
    }

    @Override
    public void onTurn(int step) {
        setScore(score);
        check();
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) enemyBullets.add(bullet);
        moveSpaceObjects();
        drawScene();
        frameCount++;
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x < 0 || y < 0 || x > WIDTH - 1 || y > HEIGHT - 1) return;
        super.setCellValueEx(x, y, cellColor, value);
    }

    private void removeDeadBullets() {
        List<Bullet> bullets = new ArrayList<>(enemyBullets);
        for (Bullet bullet : bullets) {
            if (bullet.y >= HEIGHT - 1 || !bullet.isAlive) enemyBullets.remove(bullet);
        }
        Iterator iterator = playerBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = (Bullet) iterator.next();
            if (bullet.y + bullet.height < 0 || !bullet.isAlive) iterator.remove();
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case LEFT:
                playerShip.setDirection(Direction.LEFT);
                return;
            case RIGHT:
                playerShip.setDirection(Direction.RIGHT);
                return;
            case SPACE:
                if (isGameStopped) {
                    createGame();
                    return;
                }
                Bullet bullet = playerShip.fire();
                if (bullet != null && PLAYER_BULLETS_MAX > playerBullets.size()) playerBullets.add(bullet);
                return;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key) {
            case RIGHT:
                if (playerShip.getDirection() == Direction.RIGHT) playerShip.setDirection(Direction.UP);
                return;
            case LEFT:
                if (playerShip.getDirection() == Direction.LEFT) playerShip.setDirection(Direction.UP);
                return;
        }
    }

    private void check() {
        score += enemyFleet.verifyHit(playerBullets);
        playerShip.verifyHit(enemyBullets);
        removeDeadBullets();
        if (!playerShip.isAlive) stopGameWithDelay();
        enemyFleet.deleteHiddenShips();
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
        if (enemyFleet.getBottomBorder() >= playerShip.y) playerShip.kill();
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) showMessageDialog(Color.BLUE, "WIN", Color.GREEN, 75);
        else showMessageDialog(Color.BLUE, "GAME OVER", Color.RED, 75);
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }
}