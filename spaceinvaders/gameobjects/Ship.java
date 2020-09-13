package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject {
    public boolean isAlive = true;
    private List<int[][]> frames;
    private int frameIndex;
    private boolean loopAnimation =false;

    public void setAnimatedView(boolean isLoopAnimation, int[][]... vievFrames) {
        loopAnimation = isLoopAnimation;
        setMatrix(vievFrames[0]);
        frames = Arrays.asList(vievFrames);
        frameIndex = 0;
    }

    public void nextFrame() {
        frameIndex++;
        if (frameIndex >= frames.size() && loopAnimation) frameIndex = 0;
        if (frameIndex >= frames.size() && !loopAnimation) return;
        matrix = frames.get(frameIndex);
    }

    public Ship(double x, double y) {
        super(x, y);
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    public void setStaticView(int[][] viewFrame) {
        frames = new ArrayList<int[][]>();
        frameIndex = 0;
        frames.add(viewFrame);
        setMatrix(viewFrame);
    }

    public Bullet fire() {
        return null;
    }

    public void kill() {
        isAlive = false;
    }
    public boolean isVisible(){
        if (!isAlive && frameIndex >= frames.size()) return false;
        return true;
    }
}
