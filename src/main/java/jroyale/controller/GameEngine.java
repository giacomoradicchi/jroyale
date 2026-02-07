package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import jroyale.model.Entity;
import jroyale.model.troops.Giant;
import jroyale.model.troops.MiniPekka;
import jroyale.model.troops.Skeleton;
import jroyale.model.troops.Troop;
import jroyale.shared.Enums.Side;

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
            new Skeleton(22, 10, Side.PLAYER)
        );

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // update
                controllerForModel.updateModel(now);
                controllerForView.updateView(now);
                
                // rendering
                controllerForView.renderArena();

                for (Entity e : controllerForModel.getEntitiesOrderedByPosY()) {
                    if (e instanceof Troop) {
                        controllerForView.renderTroop(
                            controllerForView.logicToGraphicX(e.getX()),                                      // graphic X 
                            controllerForView.logicToGraphicY(e.getY()),                                      // graphic Y
                            e.getDirection().angle(),                                      // angle direction
                            e.getCurrentFrame(),                                           // current frame
                            ((Troop) e).getState(),
                            e.getSide(),                                                    // side
                            EntityBinder.getInstance().getEntityTypeView((e.getClass()))          // troop type
                        );
                    }
                }

            }
        };

        gameLoop.start();
    }

    private void initGameLoop() {
        controllerForView.initView();
        controllerForModel.initModel();
        EntityBinder.getInstance().init();
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
