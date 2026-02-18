package jroyale.model.troops;

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
        // TODO: mettere la ricerca delle truppe avversarie vicine nel caso in cui il target c'è e non è 
        // una istanza di una torre e se non sono state trovate allora mettere una torre come target.
        
        if (!selectClosestEnemy()) {
            selectClosestTower();
        }
    }

}
