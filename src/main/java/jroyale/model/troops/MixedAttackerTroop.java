package jroyale.model.troops;

import jroyale.model.TowerTargetSelector;

public class MixedAttackerTroop extends Troop {

    public MixedAttackerTroop(String name, double x, double y, byte speedType, byte side) {
        super(name, x, y, speedType, side);
    }

    public MixedAttackerTroop(String name, int n, int m, byte speedType, byte side) {
        super(name, n, m, speedType, side);
    }

    @Override
    protected void updateTarget() {
        // TODO: mettere la ricerca delle truppe avversarie vicine nel caso in cui il target c'è e non è 
        // una istanza di una torre e se non sono state trovate allora mettere una torre come target.
        
        if (target == null) { 
            target = TowerTargetSelector.getClosestEnemyTower(this);
        }
    }
    
}
