package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Giant extends TowerAttackerTroop {
    
    private static Map<State, Integer> totalAnimationSteps;
    private static final int FPS_ANIMATION = 10;
    private static final int HIT_FRAME = 7;


    public Giant(double x, double y, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public Giant(int row, int col, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public static void setTotalAnimationSteps(Map<State, Integer> totalAnimationSteps) {
        Giant.totalAnimationSteps = totalAnimationSteps;
    }

    @Override
    public int getTotalAnimationSteps() {
        return totalAnimationSteps.get(state);
    }

    @Override
    public int getFPSAnimation() {
        return FPS_ANIMATION;
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
