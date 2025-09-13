package jroyale.view;

public interface IView {
    
    public void initializeRendering();
    
    public void render(long millisecs);

    public void renderPlayerKingTower(float centreLogicX, float centreLogicY);

    // just for debug
    public void renderCells(boolean[][] cells);
}
