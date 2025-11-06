package jroyale.model;

import java.util.List;

public interface IModel {

    public void update(long now);

    public int getRowsCount();

    public int getColsCount();

    public void addPlayerTroop(PlayerTroop troop);

    public List<Entity> getEntitiesOrderedByPosY(); // necessary to enable depth in rendering

    public List<Entity> getEntitiesOnTile(int i, int j);

    // just for debugging:
    public boolean[][] getReachableTiles();
}
