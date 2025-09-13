package jroyale.model;

public interface IModel {

    public void update();

    public float getPlayerKingTowerCentreX();
    public float getPlayerKingTowerCentreY();

    public int getRowCount();
    public int getColsCount();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
