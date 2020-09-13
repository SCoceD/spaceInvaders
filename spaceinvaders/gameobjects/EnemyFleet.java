package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet() {
        createShips();
    }

    public void draw(Game game) {
        for (EnemyShip ship : ships) {
            ship.draw(game);
        }
    }

    private void createShips() {
        ships = new ArrayList<EnemyShip>();
        for (int j = 0; j < ROWS_COUNT; j++) {
            for (int k = 0; k < COLUMNS_COUNT; k++) {
                ships.add(new EnemyShip(k * STEP, j * STEP + 12));
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT/2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length/2-1, 5));
    }

    private double getLeftBorder() {
        double min_left = 110;
        for (EnemyShip ship : ships) {
            if (ship.x < min_left) {
                min_left = ship.x;
            }
        }
        return min_left;
    }

    private double getRightBorder() {
        double max_right = 0;
        for (EnemyShip ship : ships) {
            if (ship.x + ship.width > max_right) {
                max_right = ship.x + ship.width;
            }
        }
        return max_right;
    }

    private double getSpeed() {
        if (2.0 < 3.0 / ships.size()) return 2.0;
        else return 3.0 / ships.size();
    }

    public void move() {
        if (ships.size() == 0) return;
        if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            for (EnemyShip ship : ships) {
                ship.move(Direction.DOWN, getSpeed());
            }
        } else {
            for (EnemyShip ship : ships) {
                ship.move(direction, getSpeed());
            }
        }
        if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            for (EnemyShip ship : ships) {
                ship.move(Direction.DOWN, getSpeed());
            }
        } else {
            for (EnemyShip ship : ships) {
                ship.move(direction, getSpeed());
            }
        }
    }

    public Bullet fire(Game game) {
        if (ships.size() == 0) return null;
        if (game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) return null;
        return ships.get(game.getRandomNumber(ships.size())).fire();
    }

    public void deleteHiddenShips() {
        Iterator iterator = ships.iterator();
        while (iterator.hasNext()) {
            EnemyShip ship = (EnemyShip) iterator.next();
            if (!ship.isVisible()) iterator.remove();
        }
    }
    public int verifyHit(List<Bullet> bullets){
        if (bullets.isEmpty())return 0;
        int score = 0;
        for (Bullet bullet : bullets) {
            for (EnemyShip ship : ships) {
                if (bullet.isCollision(ship) && ship.isAlive && bullet.isAlive){
                    bullet.kill();
                    ship.kill();
                    score+=ship.score;
                }
            }
        }
        return score;
    }

    public int getShipsCount() {
        return ships.size();
    }

    public double getBottomBorder(){
        double maxY = 0;
        for (EnemyShip ship : ships) {
            if(ship.y > maxY) maxY = ship.y + ship.height;
        }
        return maxY;
    }
}