package jroyale.model;

import java.util.List;

public interface IModel {

    public void update(long now);

    public double getTowerCentreX(int towerType); // logic location

    public double getTowerCentreY(int towerType); // logic location

    public int getRowsCount();

    public int getColsCount();

    public void addPlayerTroop(PlayerTroop troop);

    public List<Troop> getTroopsOrderedByPosY(); // necessary to enable depth in rendering

    // just for debugging:
    public boolean[][] getReachableTiles();
}
