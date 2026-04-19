package jroyale.model.troops;

import java.util.Map;

import jroyale.model.EnemyTargetSelector;
import jroyale.model.Entity;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Valkyrie extends MixedAttackerTroop{

    private static Map<State, Integer> totalAnimationSteps;
    private static final int FPS_ANIMATION = 15;
    private static final int HIT_FRAME = 4;

    public Valkyrie(double x, double y, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public Valkyrie(int row, int col, String name, Speed speedType, MeleeRange melee, double mass,
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
        return EntityType.VALKYRIE;
    }

    @Override
    public int getTotalAnimationSteps() {
        return totalAnimationSteps.get(state);
    }

    @Override 
    protected void attackTarget() {
        // first attack target
        super.attackTarget();

        // then attack also sourrounding enemies
        for (Entity enemy : EnemyTargetSelector.getInstance().getTroopsInMeleeRange(this)) {
            enemy.setDamage(getDamage());
        }
    }
    

    // static methods

    public static void setTotalAnimationSteps(Map<State, Integer> totalAnimationSteps) {
        Valkyrie.totalAnimationSteps = totalAnimationSteps;
    }


}
