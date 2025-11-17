package jroyale.model.troops;

import jroyale.model.Entity;

public class Giant extends TowerAttackerTroop {
    
    private static final String NAME = "Giant";
    private static final byte SPEED = Troop.SLOW;
    private static final double COLLISION_RADIUS = 0.75;

    private static int numFramesPerDirection = Entity.DEFAULT_FRAMES_PER_DIRECTION;

    public Giant(double x, double y, byte side) {
        super(NAME, x, y, SPEED, side);
    }

    public Giant(int n, int m, byte side) {
        super(NAME, n, m, SPEED, side);
    }

    public static void setFramesPerDirection(int numFramesPerDirection) {
        Giant.numFramesPerDirection = numFramesPerDirection;
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
