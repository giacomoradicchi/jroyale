package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import jroyale.model.CollisionManager;
import jroyale.model.Entity;
import jroyale.model.IModel;
import jroyale.model.towers.Tower;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Troop;
import jroyale.shared.Side;
import jroyale.view.IView;
import jroyale.view.View;
import jroyale.view.View.TroopType;
import jroyale.view.troops.GiantView;
import jroyale.view.troops.MiniPekkaView;
import jroyale.view.troops.TroopView;

public class Controller implements IController {

    private IModel model;
    private IView view;
    private Scene scene;
    private int lastMouseColumnIndex = -1;
    private int lastMouseRowIndex = -1;

    // binders 
    private ModelViewBinder<Troop, TroopView> troopBinder = new ModelViewBinder<>();

    public Controller(IModel model, IView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.scene = scene;
    }

    @Override
    public void start() {
        MouseManager.enableInput(scene);
        CollisionManager.setModel(model);
        view.loadSprites();
        initModelTroopsFrames();

        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.update(now);

                view.initializeRendering(now, scene.getWidth(), scene.getHeight());
                
                view.renderArena();

                //view.renderCells(model.getPlayerDroppableTiles());

                handleMouseEvents();

                
                
                // depth rendering based on Y pos

                for (Entity entity : model.getEntitiesOrderedByPosY()) {
                    renderEntity(entity);
                }
            }
        };
        loop.start();
    }

    //
    // private methods
    //

    private void initModelTroopsFrames() {
        MiniPekka.setFramesPerDirection(MiniPekkaView.NUM_FRAMES_PER_DIRECTION);
        Giant.setFramesPerDirection(GiantView.NUM_FRAMES_PER_DIRECTION);
    }

    private void setBindings() {
        troopBinder.bind(null, null);
    }

    private void handleMouseEvents() {
        if (MouseManager.isMousePressed()) {
            updateLogicMousePos();
            renderDragPlacementPreview();
        } 

        if (MouseManager.isMouseReleased() && isLastLogicMousePosValid()) {
            model.addTroop(
                new Giant(lastMouseRowIndex, lastMouseColumnIndex, Side.PLAYER)
            );
            
            resetLastLogicMousePos();
            view.resetDragPlacementPreviewAnimation();
        }
    }

    // render methods:
    private void renderEntity(Entity e) {
        if (e instanceof Troop) {

            renderTroop(e);

        } else if (e instanceof Tower) {
            
            renderTower(e);
        }
    }

    private void renderTroop(Entity e) {
        
        view.renderTroop(
            logic2GraphicX(e.getX()),           // graphic X 
            logic2GraphicY(e.getY()),           // graphic Y
            e.getDirection().angle(),           // angle direction
            View.TroopType.GIANT,          // troop type
            e.getCurrentFrame(),                // current frame
            Side.PLAYER                         // side
        );

        /* view.renderOval(
            logic2GraphicX(e.getX()), 
            logic2GraphicY(e.getY()), 
            getDx() * (e.getCollisionRadius() * 2),
            getDy() * (e.getCollisionRadius() * 2),
            0.5
        ); */    
    }

    private void renderTower(Entity e) {
        Tower tower = (Tower) e;

        /* view.renderOval(
            logic2GraphicX(tower.getX()), 
            logic2GraphicY(tower.getY()), 
            getDx() * (tower.getCollisionRadius() * 2),
            getDy() * (tower.getCollisionRadius() * 2),
            0.5
        );    */
        
        view.renderTower(
            tower.getTowerType(), 
            logic2GraphicX(tower.getX()),
            logic2GraphicY(tower.getY())
        );  
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
        &&  model.isPlayerTroopDroppableOnTile(logicY, logicX)) {
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
