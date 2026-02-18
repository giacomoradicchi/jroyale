package jroyale.model;

import java.util.List;

import jroyale.model.troops.Troop;

public interface IEnemyTargetSelector {

    public Troop getClosestEnemyInVisionRange(Troop troop);

    public List<Troop> getTroopsInMeleeRange(Troop troop);
}
