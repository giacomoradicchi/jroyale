package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Pekka extends MixedAttackerTroop {

    private static Map<State, Integer> totalAnimationSteps;
    private static final int FPS_ANIMATION = 12;
    private static final int HIT_FRAME = 5;

    public Pekka(double x, double y, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public Pekka(int row, int col, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    @Override
    protected int getHitFrame() {
        return HIT_FRAME;
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
