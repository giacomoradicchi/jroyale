package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import jroyale.model.Entity;
import jroyale.model.IModel;
import jroyale.model.PlayerTroop;
import jroyale.model.Troop;
import jroyale.model.towers.Tower;
import jroyale.shared.Side;
import jroyale.view.IView;

public class Controller implements IController {

    private IModel model;
    private IView view;
    private Scene scene;
    private int lastMouseColumnIndex = -1;
    private int lastMouseRowIndex = -1;

    public Controller(IModel model, IView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.scene = scene;
    }

    int count = 0;

    @Override
    public void start() {
        MouseManager.enableInput(scene);

        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.update(now);
                view.initializeRendering(now, scene.getWidth(), scene.getHeight());
                
                view.renderArena();

                //view.renderCells(model.getReachableTiles());

                // handling mouse events

                if (MouseManager.isMousePressed()) {
                    updateLogicMousePos();
                    renderDragPlacementPreview();
                } 

                if (MouseManager.isMouseReleased() && isLastLogicMousePosValid()) {
                    model.addPlayerTroop(
                        new PlayerTroop(
                            null, 
                            lastMouseRowIndex,
                            lastMouseColumnIndex
                        )
                    );
                    
                    resetLastLogicMousePos();
                    view.resetDragPlacementPreviewAnimation();
                }
                
                // depth rendering based on Y pos

                for (Entity entity : model.getEntitiesOrderedByPosY()) {
                    renderEntity(entity);
                }
            }
        };
        loop.start();
    }

    // render methods:
    private void renderEntity(Entity e) {
        if (e instanceof Troop) {
            view.renderTroop(logic2GraphicX(e.getX()), logic2GraphicY(e.getY()), Side.PLAYER);
        } else if (e instanceof Tower) {
            Tower tower = (Tower) e;
            view.renderTower(
                tower.getTowerType(), 
                logic2GraphicX(tower.getX()),
                logic2GraphicY(tower.getY())
            );
        }
    }


    // Mouse pressed methods:

    private void resetLastLogicMousePos() {
        lastMouseColumnIndex = -1;
        lastMouseRowIndex = -1;
    }

    private void renderDragPlacementPreview() {
        if (isLastLogicMousePosValid()) {
            view.renderDragPlacementPreview(index2GraphicCentreX(lastMouseColumnIndex), index2GraphicCentreY(lastMouseRowIndex));
        }
    }

    private boolean isLastLogicMousePosValid() {
        return lastMouseColumnIndex != -1 && lastMouseRowIndex != -1;
    }

    private void updateLogicMousePos() {
        // casting logic coords into int so the card placing will fit exactly inside a tile

        int logicX = (int) Math.floor(graphic2LogicX(MouseManager.getLastMousePositionX()));
        int logicY = (int) Math.floor(graphic2LogicY(MouseManager.getLastMousePositionY()));

        if (0 <= logicX && logicX < model.getColsCount()
        &&  0 <= logicY && logicY < model.getRowsCount()
        &&  model.getReachableTiles()[logicY][logicX] == true) {
            lastMouseColumnIndex = logicX;
            lastMouseRowIndex = logicY;
        } 
        
        /* else if (lastLogicMousePositionX != -1 && lastLogicMousePositionY != -1){
            if (0 <= logicX && logicX < model.getColsCount() 
            &&  model.getReachableTiles()[lastLogicMousePositionY][logicX] == true) {
                lastLogicMousePositionX = logicX;
                lastLogicMousePositionY = Math.max(0, Math.min(logicY, model.getRowsCount()-1));
            } 
            if (0 <= logicY && logicY < model.getRowsCount()
            &&  model.getReachableTiles()[logicY][Math.max(0, Math.min(logicX, model.getColsCount()-1))] == true) {
                lastLogicMousePositionX = Math.max(0, Math.min(logicX, model.getColsCount()-1));
                lastLogicMousePositionY = logicY;
            } 
        }  */

        
    }

    // Coords Transformation:

    private double getDx() {
        return view.getMapWidth() / model.getColsCount();
    }

    private double getDy() {
        return view.getMapHeight() / model.getRowsCount();
    }

    private double logic2GraphicX(double logicCoordX) {
        return view.getMapTopLeftCornerX() + logicCoordX * getDx();
    }

    private double logic2GraphicY(double logicCoordY) {
        return view.getMapTopLeftCornerY() + logicCoordY * getDy();
    }

    private double index2GraphicCentreX(int m) {
        // this methods returns the graphic centre of the cell in (?, m) position.
        // -----
        // | @ | (?, m)
        // -----
        //
        // x coord must be shifted by 0.5, which is half a cell
        return logic2GraphicX(m + 0.5);
    }

    private double index2GraphicCentreY(int n) {
        // this methods returns the graphic centre of the cell in (n, ?) position.
        // -----
        // | @ | (n, ?)
        // -----
        //
        // y coord must be shifted by 0.5, which is half a cell
        return logic2GraphicY(n + 0.5);
    }

    private double graphic2LogicX(double graphicCoordX) {
        return (graphicCoordX - view.getMapTopLeftCornerX()) / getDx();
    }

    private double graphic2LogicY(double graphicCoordY) {
        return (graphicCoordY - view.getMapTopLeftCornerY()) / getDy();
    }
}
