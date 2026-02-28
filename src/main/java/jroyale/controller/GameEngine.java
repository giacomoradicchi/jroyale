package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jroyale.model.Entity;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Pekka;
import jroyale.model.troops.Skeleton;
import jroyale.model.troops.Valkyrie;
import jroyale.utils.Enums.Side;

public class GameEngine implements IGameEngine {
    
    private static GameEngine instance = null;
    
    private IControllerForModel controllerForModel;
    private IControllerForView controllerForView;
    private AnimationTimer gameLoop;

    private GameEngine() {}

    // private methods
    private void startGameLoop() {
        initGameLoop();

        // TODO: remove it, just for debug
        controllerForModel.addTroop(
            new Valkyrie(22, 10, Side.PLAYER)
        );
        controllerForModel.addTroop(
            new Skeleton(10, 13, Side.OPPONENT)
        ); 
        
        controllerForModel.addTroop(
            new Skeleton(8, 13, Side.OPPONENT)
        ); controllerForModel.addTroop(
            new Skeleton(8, 13, Side.OPPONENT)
        ); controllerForModel.addTroop(
            new Skeleton(8, 13, Side.OPPONENT)
        ); controllerForModel.addTroop(
            new Skeleton(8, 13, Side.OPPONENT)
        ); controllerForModel.addTroop(
            new Skeleton(8, 13, Side.OPPONENT)
        ); controllerForModel.addTroop(
            new Skeleton(8, 13, Side.OPPONENT)
        ); 
        controllerForModel.addTroop(
            new Skeleton(10, 13, Side.OPPONENT)
        ); 
        controllerForModel.addTroop(
            new Valkyrie(23, 5, Side.PLAYER)
        );
        
        controllerForModel.addTroop(
            new Giant(10, 5, Side.OPPONENT)
        );

        /* controllerForModel.addTroop(
            new MiniPekka(10, 15, Side.OPPONENT)
        ); */

        

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // update
                controllerForModel.updateModel(now);
                controllerForView.updateView(now);
                
                // rendering
                controllerForView.renderArena();

                if (controllerForView.shouldRenderDragPlacementPreview()) {
                    /* controllerForView.fillPoint(
                        controllerForView.logicToGraphicX(controllerForView.getSelectedCol() + 0.5),
                        controllerForView.logicToGraphicY(controllerForView.getSelectedRow() + 0.5), 
                        10, 
                        Color.BLUE
                    ); */
                    controllerForView.renderDragPlacementPreview(
                        controllerForView.logicToGraphicX(controllerForView.getSelectedCol() + 0.5),
                        controllerForView.logicToGraphicY(controllerForView.getSelectedRow() + 0.5)
                    );
                }

                for (Entity e : controllerForModel.getEntitiesOrderedByPosY()) {
                    controllerForView.renderEntity(
                        controllerForView.logicToGraphicX(e.getX()),
                        controllerForView.logicToGraphicY(e.getY()),
                        e.getHitPoints(),
                        e.getMaxHitPoints(),
                        controllerForView.getDx() * e.getCollisionRadius(),
                        e.getDirection().angle(),
                        e.getCurrentAnimationIndex(),
                        e.getState(),
                        e.getSide(),
                        e.getType()
                    );
                }

            }
        };

        gameLoop.start();
    }

    private void initGameLoop() {
        controllerForView.initView();
        EntityBinder.getInstance().init();
        controllerForModel.initModel();
    }

    // instance methods
    @Override
    public void start(Stage stage) {
        // saving instances so the access to them will be faster
        controllerForView = ControllerForView.getInstance();
        controllerForModel = ControllerForModel.getInstance();
        
        controllerForView.openWindow(stage);
        startGameLoop();
    }   

    @Override
    public void stop() {
        gameLoop.stop();
    }

    @Override
    public void resume() {
        gameLoop.start();
    }

    // static methods
    public static IGameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }

        return instance;
    }
}
