package jroyale.model;

import jroyale.model.troops.Troop;

public interface IEnemyTargetSelector {

    public Troop getClosestEnemyOnRange(Troop troop);

}
