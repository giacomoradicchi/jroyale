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
    private boolean mousePressed = false;
    private Point2D initialMousePosition;
    private Point2D lastMousePosition;

    public Controller(IModel model, IView view, Scene scene) {
        this.model = model;
        this.view = view;
        this.t0 = System.currentTimeMillis();
        this.scene = scene;
    }

    int count = 0;

    @Override
    public void start() {
        enableMouseInput();

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
                        model.getTowerCentreX(towerType),
                        model.getTowerCentreY(towerType)
                    );
                }

                if (mousePressed) {
                    view.renderPoint(lastMousePosition.getX(), lastMousePosition.getY());
                }
            }
        };
        loop.start();
    }

    private void enableMouseInput() {
        scene.setOnMousePressed(event -> {
            mousePressed = true;
            double x = event.getSceneX();
            double y = event.getSceneY();
            this.initialMousePosition = new Point2D(x, y);
            this.lastMousePosition = new Point2D(x, y);
            System.out.println("Initial mouse position. " + initialMousePosition.toString());
            System.out.println("Last mouse position. " + lastMousePosition.toString());
        });

        scene.setOnMouseDragged(event -> {
            double x = event.getSceneX(); // coordinata rispetto alla scena
            double y = event.getSceneY();
            this.lastMousePosition = new Point2D(x, y);
            System.out.println("Last mouse position. " + lastMousePosition.toString());
        });

        scene.setOnMouseReleased(event -> {
            mousePressed = false;
            System.out.println("Last mouse position. " + lastMousePosition.toString());
        });
    }
}
