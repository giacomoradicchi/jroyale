package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jroyale.model.IModel;
import jroyale.shared.TowerIndex;
import jroyale.view.IView;

public class Controller implements IController {

    private IModel model;
    private IView view;
    private long t0;
    private Scene scene;

    public Controller(IModel model, IView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.t0 = System.currentTimeMillis();
        this.scene = scene;
    }

    int count = 0;

    @Override
    public void start() {
        setupResizeListener();

        

        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.update();
                view.initializeRendering(System.currentTimeMillis() - t0, scene.getWidth(), scene.getHeight());
                
            
                view.renderArena();

                //view.renderCells(model.getReachableTiles());

                //view.render(System.currentTimeMillis() - t0);

                // rendering all the towers
                for (int towerType = 0; towerType < TowerIndex.NUM_TOWERS; towerType++) {
                    view.renderTower(
                        towerType, 
                        model.getTowerCentreX(towerType),
                        model.getTowerCentreY(towerType)
                    );
                }
            }
        };
        loop.start();
    }

    private void setupResizeListener() {
        /* scene.widthProperty().addListener((o, oldW, newW) -> {
            view.setCanvasWidth(newW.doubleValue());
        });

        scene.heightProperty().addListener((o, oldH, newH) -> {
            view.setCanvasHeight(newH.doubleValue());
        }); */
    }
}
