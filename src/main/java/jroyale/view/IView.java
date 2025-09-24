package jroyale.view;

public interface IView {
    
    public void initializeRendering(long millisecs);
    
    public void render(long millisecs);

    public void renderTexture();

    public void renderPlayerKingTower(float centreLogicX, float centreLogicY);

    // just for debug
    public void renderCells(boolean[][] cells);
}
