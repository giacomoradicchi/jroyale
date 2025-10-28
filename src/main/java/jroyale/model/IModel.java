package jroyale.model;

public interface IModel {

    public void update();

    // towers logic location
    public double getTowerCentreX(int towerType);
    public double getTowerCentreY(int towerType);

    public int getRowsCount();
    public int getColsCount();

    public double fromGraphicToLogicX(double graphicX);
    public double fromGraphicToLogicY(double graphicY);

    // just for debugging:
    public boolean[][] getReachableTiles();
}
