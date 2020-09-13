package com.javarush.games.spaceinvaders.gameobjects;


import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

import static com.javarush.games.spaceinvaders.Direction.UP;

public class Bullet extends GameObject {
    private int dy;
    public boolean isAlive = true;
    public Bullet(double x, double y, Direction direction) {
        super(x, y);
        setMatrix(ShapeMatrix.BULLET);
        if (direction == UP){dy = -1;} else dy = 1;
    }
    public void move(){
        this.y += dy;
    }
    public void kill(){
        isAlive = false;
    }
}
