package jroyale.view;

public interface IView {
    
    public void initializeRendering(long millisecs);
    
    public void render(long millisecs);

    public void renderTexture();

    public void renderPlayerKingTower(float centreLogicX, float centreLogicY);

    public double getCanvasWidth();

    public double getCanvasHeight();

    public void resizeCanvas(double newHeight);

    // just for debug
    public void renderCells(boolean[][] cells);
}
