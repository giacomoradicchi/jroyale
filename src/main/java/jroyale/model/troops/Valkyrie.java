package jroyale.model.troops;

import java.util.Map;

import jroyale.model.EnemyTargetSelector;
import jroyale.model.Entity;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public class Valkyrie extends MixedAttackerTroop{

    private static final String NAME = "Valkyrie";
    private static final Speed SPEED = Speed.MEDIUM;
    private static final MeleeRange MELEE = MeleeRange.LONG;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.5;
    private static final int FPS_ANIMATION = 15;
    private static final long LOAD_TIME = (long) (1.5 * 1_000_000_000L);

    private static final int HITPOINTS = 677; 
    private static final int DAMAGE = 677;
    private static final int HIT_FRAME = 4;

    public Valkyrie(double x, double y, Side side) {
        super(NAME, x, y, HITPOINTS, DAMAGE, SPEED, MELEE, side);
    }

    public Valkyrie(int n, int m, Side side) {
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
    public double getCollisionRadius() {
        return COLLISION_RADIUS;
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
