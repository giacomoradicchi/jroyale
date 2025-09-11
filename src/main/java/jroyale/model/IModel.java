package jroyale.model;

public interface IModel {
    
    public int[] getPlayerTowerPosition();

    public void update();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
