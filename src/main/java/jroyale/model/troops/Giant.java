package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Giant extends TowerAttackerTroop {
    
    private static final String NAME = "Giant";
    private static final Speed SPEED = Speed.SLOW;
    private static final MeleeRange MELEE = MeleeRange.LONG;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 10;
    private static final long LOAD_TIME = (long) (1.5 * 1_000_000_000);
    private static final int HIT_FRAME = 7;
    private static final int HITPOINTS = 1600;
    private static final int DAMAGE = 300;


    public Giant(double x, double y, Side side) {
        super(NAME, x, y, HITPOINTS, DAMAGE, SPEED, MELEE, side);
    }

    public Giant(int row, int col, Side side) {
        super(NAME, row, col, HITPOINTS, DAMAGE, SPEED, MELEE, side);
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
