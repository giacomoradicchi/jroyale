package jroyale.model.troops;

import java.util.Map;

import jroyale.model.Entity;

public class MiniPekka extends MixedAttackerTroop {

    private static final String NAME = "Mini-Pekka";
    private static final byte SPEED = Troop.FAST;
    private static Map<Byte, Integer> numFramesPerDirection;
    private static final double COLLISION_RADIUS = 0.45;
    private static final int FPS_ANIMATION = 18;

    public MiniPekka(double x, double y, byte side) {
        super(NAME, x, y, SPEED, side);
    }

    public MiniPekka(int n, int m, byte side) {
        super(NAME, n, m, SPEED, side);
    }

    public static void setFramesPerDirection(Map<Byte, Integer> numFramesPerDirection) {
        MiniPekka.numFramesPerDirection = numFramesPerDirection;
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
