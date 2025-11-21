package jroyale.view;

import jroyale.shared.State;
import jroyale.view.View.TroopType;

public interface IView {
    
    public void initializeRendering(long millisecs, double newWidth, double newHeight);

    public void loadSprites();
    
    public void render(long millisecs);

    public void renderArena();

    public void renderTower(int towerType, double centreX, double centreY);

    public void renderTroop(double centreX, double centreY, double angleDirection, TroopType type, int currentFrame, byte state, int side);

    public void renderDragPlacementPreview(double centreX, double centreY);

    public void resetDragPlacementPreviewAnimation();

    public double getCanvasWidth();

    public double getCanvasHeight();

    public void setCanvasWidth(double newWidth);

    public void setCanvasHeight(double newHeight);

    public double getMapTopLeftCornerX();

    public double getMapTopLeftCornerY();

    public double getMapWidth();

    public double getMapHeight();

    // just for debug
    public void renderCells(boolean[][] cells);

    public void renderPoint(double centreX, double centreY);

    public void renderOval(double centreX, double centreY, double width, double height, double opacity);

    public void renderVector(double startX, double startY, double angle);
}
