package jroyale.model.troops;

import jroyale.model.EnemyTargetSelector;
import jroyale.model.TowerTargetSelector;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class MixedAttackerTroop extends Troop {

   public MixedAttackerTroop(double x, double y, String name, Speed speedType, MeleeRange melee,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public MixedAttackerTroop(int row, int col, String name, Speed speedType, MeleeRange melee,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, collisionRadius, loadTime, hitPoints, damage, side);
    }

    @Override
    protected void updateTarget() {
        
        if (!selectClosestEnemy()) {
            selectClosestTower();
        }
    }

}
