package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import jroyale.model.IModel;
import jroyale.shared.TowerIndex;
import jroyale.view.IView;

public class Controller implements IController {

    private IModel model;
    private IView view;
    private long t0;
    private Scene scene;
    private int lastLogicMousePositionX = -1;
    private int lastLogicMousePositionY = -1;

    public Controller(IModel model, IView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.t0 = System.currentTimeMillis();
        this.scene = scene;
    }

    int count = 0;

    @Override
    public void start() {
        MouseManager.enableInput(scene);

        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.update();
                view.initializeRendering(System.currentTimeMillis() - t0, scene.getWidth(), scene.getHeight());
                
            
                view.renderArena();

                view.renderCells(model.getReachableTiles());

                // rendering all the towers
                for (int towerType = 0; towerType < TowerIndex.NUM_TOWERS; towerType++) {
                    view.renderTower(
                        towerType, 
                        logic2GraphicX(model.getTowerCentreX(towerType)),
                        logic2GraphicY(model.getTowerCentreY(towerType))
                    );
                }

                if (MouseManager.isMousePressed()) {
                    int logicX = (int) Math.floor(graphic2LogicX(MouseManager.getLastMousePositionX()));
                    int logicY = (int) Math.floor(graphic2LogicY(MouseManager.getLastMousePositionY()));

                    if (0 <= logicX && logicX < model.getColsCount()
                    &&  0 <= logicY && logicY < model.getRowsCount()
                    &&  model.getReachableTiles()[logicY][logicX] == true) {
                        lastLogicMousePositionX = logicX;
                        lastLogicMousePositionY = logicY;
                    } /* else if (lastLogicMousePositionX != -1 && lastLogicMousePositionY != -1){
                        if (0 <= logicX && logicX < model.getColsCount() 
                        &&  model.getReachableTiles()[Math.max(0, Math.min(logicY, model.getRowsCount()-1))][logicX] == true) {
                            lastLogicMousePositionX = logicX;
                            lastLogicMousePositionY = Math.min(logicY, model.getRowsCount()-1);
                        } 
                        if (0 <= logicY && logicY < model.getRowsCount()
                        &&  model.getReachableTiles()[logicY][Math.max(0, Math.min(logicX, model.getColsCount()-1))] == true) {
                            lastLogicMousePositionX = Math.max(0, Math.min(logicX, model.getColsCount()-1));
                            lastLogicMousePositionY = logicY;
                        } 
                    } */

                    

                    if (lastLogicMousePositionX != -1 
                    && lastLogicMousePositionY != -1) {
                        view.renderPoint(logic2GraphicX(lastLogicMousePositionX +0.5), logic2GraphicY(lastLogicMousePositionY+0.5));
                    }

                    
                } 

                if (MouseManager.isMouseReleased()) {
                    // TODO: drop a new Troop

                    lastLogicMousePositionX = -1;
                    lastLogicMousePositionY = -1;
                }
            }
        };
        loop.start();
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

    private double graphic2LogicX(double graphicCoordX) {
        return (graphicCoordX - view.getMapTopLeftCornerX()) / getDx();
    }

    private double graphic2LogicY(double graphicCoordY) {
        return (graphicCoordY - view.getMapTopLeftCornerY()) / getDy();
    }
}
