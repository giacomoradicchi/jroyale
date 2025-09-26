package jroyale.controller;

import javafx.animation.AnimationTimer;
import jroyale.model.IModel;
import jroyale.view.IView;

public class Controller implements IController {

    private IModel model;
    private IView view;
    private long t0;

    public Controller(IModel model, IView view) {
        this.model = model;
        this.view = view;
        this.t0 = System.currentTimeMillis();
    }
    
    @Override
    public void start() {
        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.update();
                view.initializeRendering(System.currentTimeMillis() - t0);
                

                view.renderTexture();

                //view.renderCells(model.getReachableTiles());

                //view.render(System.currentTimeMillis() - t0);

                /* view.renderCells(model.getReachableTiles());

                // rendering towers
                view.renderPlayerKingTower(
                    model.getPlayerKingTowerCentreX(), 
                    model.getPlayerKingTowerCentreY()
                ); */
            }
        };
        loop.start();
    }
}
