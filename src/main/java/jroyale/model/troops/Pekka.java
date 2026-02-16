package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Pekka extends MixedAttackerTroop {

    private static final String NAME = "Pekka";
    private static final Speed SPEED = Speed.SLOW;
    private static final Range MELEE = Range.MEDIUM;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 12;
    private static final long LOAD_TIME = (long) (1.8 * 1_000_000_000);
    private static final int HIT_FRAME = 5;

    private static final int HITPOINTS = 1598;
    private static final int DAMAGE = 99;

    public Pekka(double x, double y, Side side) {
        super(NAME, x, y, HITPOINTS, DAMAGE, SPEED, MELEE, side);
    }

    public Pekka(int n, int m, Side side) {
        super(NAME, n, m, HITPOINTS, DAMAGE, SPEED, MELEE, side);
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
    public double getCollisionRadius() {
        return COLLISION_RADIUS;
    }

    @Override
    public int getFPSAnimation() {
        return FPS_ANIMATION;
    }

    @Override
    public EntityType getType() {
        return EntityType.PEKKA;
    }

    @Override
    public int getTotalAnimationSteps() {
        return totalAnimationSteps.get(state);
    }

    // static methods

    public static void setTotalAnimationSteps(Map<State, Integer> totalAnimationSteps) {
        Pekka.totalAnimationSteps = totalAnimationSteps;
    }
    
}
