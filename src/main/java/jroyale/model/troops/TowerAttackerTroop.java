package jroyale.model.troops;

import jroyale.model.TowerTargetSelector;

public class TowerAttackerTroop extends Troop {

    public TowerAttackerTroop(String name, double x, double y, byte speedType, byte side) {
        super(name, x, y, speedType, side);
    }

    public TowerAttackerTroop(String name, int n, int m, byte speedType, byte side) {
        super(name, n, m, speedType, side);
    }

    @Override
    protected void updateTarget() {
        if (target == null) { // TODO: aggiungere metodo attack(entity) che attacca una torre e che la rimuove dai target quando la vita è a zero.
            target = TowerTargetSelector.getClosestEnemyTower(this);
        }
    }
    
    
}
