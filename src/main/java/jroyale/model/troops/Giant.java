package jroyale.model.troops;

import java.util.Map;

import jroyale.shared.Enums.EntityType;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;

public class Giant extends TowerAttackerTroop {
    
    private static final String NAME = "Giant";
    private static final byte SPEED = Troop.SLOW;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 12;
    private static final long LOAD_TIME = (long) (1 * 1_000_000_000);
    private static final int HIT_FRAME = 6;

    private static final int HITPOINTS = 1598;
    private static final int DAMAGE = 99;


    public Giant(double x, double y, Side side) {
        super(NAME, x, y, HITPOINTS, DAMAGE, SPEED, side);
    }

    public Giant(int n, int m, Side side) {
        super(NAME, n, m, HITPOINTS, DAMAGE, SPEED, side);
    }

    public static void setTotalAnimationSteps(Map<State, Integer> totalAnimationSteps) {
        Giant.totalAnimationSteps = totalAnimationSteps;
    }

    @Override
    public int getTotalAnimationSteps() {
        return totalAnimationSteps.get(state);
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
        return HIT_FRAME;
    }

    @Override
    public EntityType getType() {
        return EntityType.GIANT;
    }
}
