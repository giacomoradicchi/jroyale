package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import jroyale.model.Entity;
import jroyale.utils.Config;

public class GameEngine implements IGameEngine {
    
    private static GameEngine instance = null;
    
    private IControllerForModel controllerForModel;
    private IControllerForView controllerForView;
    private AnimationTimer gameLoop;

    private GameEngine() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                

                // update
                if (!controllerForModel.isGameOver())
                    controllerForModel.updateModel(now);

                controllerForView.updateGameView(now);
                
                // rendering
                controllerForView.renderArena();

                if (controllerForView.shouldRenderDragPlacementPreview()) {
                    controllerForView.renderDragPlacementPreview(
                        controllerForView.logicToGraphicX(controllerForView.getSelectedCol()) + controllerForView.getDx()/2,
                        controllerForView.logicToGraphicY(controllerForView.getSelectedRow()) + controllerForView.getDy()/2
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
                
                controllerForView.renderPlayerDeck(
                    controllerForModel.getFirstHandPlayerCard().getType(), controllerForModel.getFirstHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getSecondHandPlayerCard().getType(), controllerForModel.getSecondHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getThirdHandPlayerCard().getType(), controllerForModel.getThirdHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getFourthHandPlayerCard().getType(), controllerForModel.getFourthHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getPlayerElixirLeft(),
                    controllerForModel.getPlayerElixirChargeTimeProgress(),
                    controllerForModel.getMaxElixir()
                );

                controllerForView.renderTimeLeft(controllerForModel.getTimeLeftSec());
                
                if (controllerForModel.isGameOver()) {
                    controllerForView.renderGameOver();
                } 
            }
        };

    }

    // private methods
    @Override
    public void startGameLoop() {
        controllerForModel.initModel(Config.getInstance().getMaxTimeSec());

        gameLoop.start();
    }

    // instance methods
    @Override
    public void start(Stage stage) {
        // saving instances to avoid redundace
        controllerForView = ControllerForView.getInstance();
        controllerForModel = ControllerForModel.getInstance();
        
        controllerForView.openWindow(stage);
        controllerForView.initView();
    } 

    @Override
    public void goToHome() {
        controllerForModel.resetModel();
        controllerForView.goToHome();
        stop();
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
