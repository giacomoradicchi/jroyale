package jroyale.model;

import java.util.Set;

import jroyale.model.troops.Troop;

public interface IEnemyTargetSelector {

    public Troop getClosestEnemyInVisionRange(Troop troop);

    public Set<Troop> getTroopsInMeleeRange(Troop troop);

}
