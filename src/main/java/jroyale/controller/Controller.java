package jroyale.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jroyale.model.IModel;
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
    
    @Override
    public void start() {
        setupResizeListener();

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

    private void setupResizeListener() {
        scene.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
                double newHeight = newVal.doubleValue();
                double aspectRatio = view.getCanvasWidth() / view.getCanvasHeight(); // original aspect Ratio
                double newWidth = newHeight * aspectRatio;

                view.resizeCanvas(newHeight);

                Stage stage = (Stage) scene.getWindow();
                stage.setWidth(newWidth);
            }
        });
    }
}
