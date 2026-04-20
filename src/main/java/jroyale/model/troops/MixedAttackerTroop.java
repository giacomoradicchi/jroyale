package jroyale.model.troops;

import jroyale.model.EnemyTargetSelector;
import jroyale.model.TowerTargetSelector;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class MixedAttackerTroop extends Troop {

   public MixedAttackerTroop(double x, double y, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public MixedAttackerTroop(int row, int col, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    @Override
    protected void updateTarget() {
        
        // searching for a new target: first search a troop in range. if not found, go to the closest tower
        if (!selectClosestEnemy()) {
            selectClosestTower();
        }
    }

}
