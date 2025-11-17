package jroyale.model.troops;

import jroyale.model.Entity;

public class MiniPekka extends MixedAttackerTroop {

    private static final String NAME = "Mini-Pekka";
    private static final byte SPEED = Troop.FAST;
    private static int numFramesPerDirection = Entity.DEFAULT_FRAMES_PER_DIRECTION;
    private static final double COLLISION_RADIUS = 0.45;

    public MiniPekka(double x, double y, byte side) {
        super(NAME, x, y, SPEED, side);
    }

    public MiniPekka(int n, int m, byte side) {
        super(NAME, n, m, SPEED, side);
    }

    public static void setFramesPerDirection(int numFramesPerDirection) {
        MiniPekka.numFramesPerDirection = numFramesPerDirection;
    }

    @Override
    public int getFramesPerDirection() {
        return numFramesPerDirection;
    }

    @Override
    public double getCollisionRadius() {
        return COLLISION_RADIUS;
    }
    
}
