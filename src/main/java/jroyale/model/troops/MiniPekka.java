package jroyale.model.troops;

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class MiniPekka extends MixedAttackerTroop {

    //private final String name;
    //private final Speed speed;
    //private final MeleeRange melee;
    //private final double collisionRadius;
    //private final long loadTime;
    //private final int hitPoints;
    //private final int damage;

    private static Map<State, Integer> totalAnimationSteps;
    private static final int FPS_ANIMATION = 18; //18
    private static final int HIT_FRAME = 7;

    public MiniPekka(double x, double y, String name, Speed speedType, MeleeRange melee,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, collisionRadius, loadTime, hitPoints, damage, side);
        
    }

    public MiniPekka(int row, int col, String name, Speed speedType, MeleeRange melee,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public static void setTotalAnimationSteps(Map<State,Integer> totalAnimationSteps) {
        MiniPekka.totalAnimationSteps = totalAnimationSteps;
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
        return EntityType.MINIPEKKA;
    }
    
}
