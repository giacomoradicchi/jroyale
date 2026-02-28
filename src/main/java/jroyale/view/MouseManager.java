package jroyale.view;

import jroyale.utils.Point;
import javafx.scene.Scene;

public class MouseManager implements IMouseManager{
    
    private static MouseManager instance = null;

    private boolean mousePressed = false, mouseReleased = false;
    private Point initialMousePosition = new Point(-1, -1); // mouse position when mouse is first pressed
    private Point lastMousePosition = new Point(-1, -1);; // last mouse position when mouse is dragged 

    private MouseManager() {} 

    @Override
    public void init(Scene scene) {
        scene.setOnMousePressed(event -> {
            mousePressed = true;
            double x = event.getSceneX();
            double y = event.getSceneY();
            initialMousePosition.setPoint(x, y);
            lastMousePosition.setPoint(x, y);
        });

        scene.setOnMouseDragged(event -> {
            double x = event.getSceneX(); // coordinata rispetto alla scena
            double y = event.getSceneY();
            lastMousePosition.setPoint(x, y);
        });

        scene.setOnMouseReleased(event -> {
            mousePressed = false;
            mouseReleased = true;
        });
    }

    @Override
    public boolean isMousePressed() {
        return mousePressed;
    } 

    @Override
    public boolean isMouseReleased() {
        // if mouse is released, this methods must return true only the first time
        // it's called, then it must return to false.

        boolean previousState = mouseReleased;

        if (previousState == true) {
            mouseReleased = false;
        }   

        
        return previousState;
    }

    @Override
    public double getInitialMousePositionX() {
        return initialMousePosition.getX();
    }

    @Override
    public double getInitialMousePositionY() {
        return initialMousePosition.getY();
    }

    @Override
    public double getLastMousePositionX() {
        return lastMousePosition.getX();
    }

    @Override
    public double getLastMousePositionY() {
        return lastMousePosition.getY();
    }

    // static methods

    public static IMouseManager getInstance() {
        if (instance == null) {
            instance = new MouseManager();
        }

        return instance;
    }
}
