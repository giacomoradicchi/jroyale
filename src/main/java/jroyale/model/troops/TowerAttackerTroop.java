package jroyale.model.troops;

import jroyale.utils.Enums.Side;

public abstract class TowerAttackerTroop extends Troop {

    public TowerAttackerTroop(double x, double y, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(x, y, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    public TowerAttackerTroop(int row, int col, String name, Speed speedType, MeleeRange melee, double mass,
        double collisionRadius, double loadTime, int hitPoints, int damage, Side side) {
        super(row, col, name, speedType, melee, mass, collisionRadius, loadTime, hitPoints, damage, side);
    }

    @Override
    protected void updateTarget() {
        selectClosestTower();
    }
    
    
}
