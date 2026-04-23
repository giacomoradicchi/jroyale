package jroyale.model.troops;

import jroyale.model.towers.Tower;
import jroyale.utils.Enums.Side;

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
        
        

        if (target != null && target.getHitPoints() == 0 && target instanceof Tower) {
            isAttackingTower = false;
        }

        // searching for a new target if it's not attacking a tower: first search a troop in range. if not found, go to the closest tower
        if (!isAttackingTower && !selectClosestEnemy()) {
            selectClosestTower();
        } 
    }

}
