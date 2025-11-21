package jroyale.model.troops;

import java.util.Map;

public class Giant extends TowerAttackerTroop {
    
    private static final String NAME = "Giant";
    private static final byte SPEED = Troop.SLOW;
    private static Map<Byte, Integer> numFramesPerDirection;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 12;
    private static final long LOAD_TIME = (long) (1 * 1_000_000_000);
    private static final int HITPOINTS = 1598;
    private static final int DAMAGE = 99;


    public Giant(double x, double y, byte side) {
        super(NAME, x, y, HITPOINTS, DAMAGE, SPEED, side);
    }

    public Giant(int n, int m, byte side) {
        super(NAME, n, m, HITPOINTS, DAMAGE, SPEED, side);
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

    @Override
    protected long getLoadTime() {
        return LOAD_TIME;
    }

    @Override
    protected int getHitFrame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHitFrame'");
    }
}
