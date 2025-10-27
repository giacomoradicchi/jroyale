package jroyale.view;

public interface IView {
    
    public void initializeRendering(long millisecs, double newWidth, double newHeight);
    
    public void render(long millisecs);

    public void renderArena();

    public void renderTower(int towerType, double centreLogicX, double centreLogicY);

    public double getCanvasWidth();

    public double getCanvasHeight();

    public void setCanvasWidth(double newWidth);

    public void setCanvasHeight(double newHeight);

    // just for debug
    public void renderCells(boolean[][] cells);

    public void renderPoint(double graphicX, double graphicY);
}
