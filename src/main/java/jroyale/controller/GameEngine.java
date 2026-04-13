package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import jroyale.model.Entity;

public class GameEngine implements IGameEngine {
    
    private static GameEngine instance = null;
    
    private IControllerForModel controllerForModel;
    private IControllerForView controllerForView;
    private AnimationTimer gameLoop;

    private GameEngine() {}

    // private methods
    private void startGameLoop() {
        initGameLoop();
        

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // update
                controllerForModel.updateModel(now);
                controllerForView.updateView(now);
                
                // rendering
                controllerForView.renderArena();

                if (controllerForView.shouldRenderDragPlacementPreview()) {
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

                controllerForView.renderPlayerDeck(
                    controllerForModel.getFirstHandPlayerCard().getType(), controllerForModel.getFirstHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getSecondHandPlayerCard().getType(), controllerForModel.getSecondHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getThirdHandPlayerCard().getType(), controllerForModel.getThirdHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getFourthHandPlayerCard().getType(), controllerForModel.getFourthHandPlayerCard().getCardStats().getElixirCost(),
                    controllerForModel.getPlayerElixirLeft(),
                    controllerForModel.getPlayerElixirChargeTimeProgress(),
                    controllerForModel.getMaxElixir()
                );

            }
        };

        gameLoop.start();
    }

    private void initGameLoop() {
        controllerForView.initView();
        controllerForModel.initModel();
    }

    // instance methods
    @Override
    public void start(@SuppressWarnings("exports") Stage stage) {
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
