package jroyale.model;

public interface IModel {

    public void update();

    // towers logic location
    public double getTowerCentreX(int towerType);
    public double getTowerCentreY(int towerType);

    public int getRowCount();
    public int getColsCount();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
