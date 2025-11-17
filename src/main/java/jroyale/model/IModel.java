package jroyale.model;

import java.util.List;

import jroyale.model.troops.Troop;

public interface IModel {

    public void update(long now);

    public int getRowsCount();

    public int getColsCount();

    public void addTroop(Troop troop);

    public List<Entity> getEntitiesOrderedByPosY(); // necessary to enable depth in rendering

    public List<Entity> getEntitiesOnTile(int i, int j);

    //public List<Tower> getPlayerTowersLeft();

    //public List<Tower> getOpponentTowersLeft();

    public boolean isTileReachable(int i, int j);

    public boolean isPlayerTroopDroppableOnTile(int i, int j);

    public boolean[][] getPlayerDroppableTiles();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
