package jroyale.model;

import jroyale.model.towers.Tower;

public interface ITowerTargetSelector {
    public void addTower(Tower tower);
    
    public void removeTower(Tower tower);

    public Tower getClosestEnemyTower(Entity e);

    public void reset();

    // static methods
    public static ITowerTargetSelector getInstance() {
        return TowerTargetSelector.getInstance();
    }
} 