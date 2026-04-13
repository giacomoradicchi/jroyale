package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Pekka extends MixedAttackerTroop {

    private static final String NAME = "Pekka";
    private static final Speed SPEED = Speed.SLOW;
    private static final MeleeRange MELEE = MeleeRange.MEDIUM;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 12;
    private static final long LOAD_TIME = (long) (1.8 * 1_000_000_000);

    private static final int HIT_FRAME = 5;
    private static final int HITPOINTS = 1598;
    private static final int DAMAGE = 355;

    public Pekka(double x, double y, String name, Speed speedType, MeleeRange melee,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public Pekka(int row, int col, String name, Speed speedType, MeleeRange melee,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, collisionRadius, loadTime, hitPoints, damage, side);
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
