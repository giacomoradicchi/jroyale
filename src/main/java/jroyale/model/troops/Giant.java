package jroyale.model.troops;

import java.util.Map;

import jroyale.model.Entity;

public class Giant extends TowerAttackerTroop {
    
    private static final String NAME = "Giant";
    private static final byte SPEED = Troop.SLOW;
    private static Map<Byte, Integer> numFramesPerDirection;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 12;


    public Giant(double x, double y, byte side) {
        super(NAME, x, y, SPEED, side);
    }

    public Giant(int n, int m, byte side) {
        super(NAME, n, m, SPEED, side);
    }

    public static void setFramesPerDirection(Map<Byte, Integer> numFramesPerDirection) {
        Giant.numFramesPerDirection = numFramesPerDirection;
    }

    @Override
    public int getFramesPerDirection() {
        return numFramesPerDirection.get(state);
    }

    @Override
    public double getCollisionRadius() {
        return COLLISION_RADIUS;
    }

    @Override
    public int getFPSAnimation() {
        return FPS_ANIMATION;
    }
}
