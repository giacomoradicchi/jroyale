package jroyale.model.troops;

import jroyale.model.EnemyTargetSelector;
import jroyale.model.TowerTargetSelector;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class MixedAttackerTroop extends Troop {

    public MixedAttackerTroop(String name, double x, double y, int healthPoints, int damage, Speed speedType, MeleeRange melee, Side side) {
        super(name, x, y, healthPoints, damage, speedType, melee, side);
    }

    public MixedAttackerTroop(String name, int n, int m, int healthPoints, int damage, Speed speedType, MeleeRange melee, Side side) {
        super(name, n, m, healthPoints, damage, speedType, melee, side);
    }

    @Override
    protected void updateTarget() {
        
        if (!selectClosestEnemy()) {
            selectClosestTower();
        }
    }

}
