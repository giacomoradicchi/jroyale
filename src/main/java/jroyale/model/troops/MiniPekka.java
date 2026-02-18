package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class MiniPekka extends MixedAttackerTroop {

    private static final String NAME = "Mini-Pekka";
    private static final Speed SPEED = Speed.FAST;
    private static final MeleeRange MELEE = MeleeRange.MEDIUM;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.45;
    private static final int FPS_ANIMATION = 18; //18
    private static final long LOAD_TIME = (long) (1.6 * 1_000_000_000L);

    private static final int HITPOINTS = 677;
    private static final int DAMAGE = 355;
    private static final int HIT_FRAME = 7;

    public MiniPekka(double x, double y, Side side) {
        super(NAME, x, y, HITPOINTS, DAMAGE, SPEED, MELEE, side);
    }

    public MiniPekka(int n, int m, Side side) {
        super(NAME, n, m, HITPOINTS, DAMAGE, SPEED, MELEE, side);
    }

    public static void setTotalAnimationSteps(Map<State,Integer> totalAnimationSteps) {
        MiniPekka.totalAnimationSteps = totalAnimationSteps;
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
        return EntityType.MINIPEKKA;
    }
    
}
