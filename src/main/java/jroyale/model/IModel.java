package jroyale.model;

public interface IModel {

    public void update();

    public double getTowerCentreX(int towerType); // logic location

    public double getTowerCentreY(int towerType); // logic location

    public int getRowsCount();

    public int getColsCount();

    public void addPlayerTroop(Troop troop);

    public Troop getPlayerTroop(int index);

    public Troop getOpponentTroop(int index);

    public int getNumberOfPlayerTroops(); 

    public int getNumberOfOpponentTroops();

    // just for debugging:
    public boolean[][] getReachableTiles();
}
