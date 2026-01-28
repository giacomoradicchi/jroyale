package jroyale.model.troops;

import jroyale.model.TowerTargetSelector;
import jroyale.shared.Enums.Side;

public abstract class TowerAttackerTroop extends Troop {

    public TowerAttackerTroop(String name, double x, double y, int healthPoints, int damage, byte speedType, Side side) {
        super(name, x, y, healthPoints, damage, speedType, side);
    }

    public TowerAttackerTroop(String name, int n, int m, int healthPoints, int damage, byte speedType, Side side) {
        super(name, n, m, healthPoints, damage, speedType, side);
    }

    @Override
    protected void updateTarget() {
        if (target.getHitPoints() == 0) { // TODO: aggiungere metodo attack(entity) che attacca una torre e che la rimuove dai target quando la vita è a zero.
            target = TowerTargetSelector.getClosestEnemyTower(this);
            enemyHit = false; // reset enemyHit
        }
    }
    
    
}
