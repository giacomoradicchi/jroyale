package jroyale.controller;

import javafx.geometry.Point2D;
import javafx.scene.Scene;

class MouseManager {
    
    private static boolean mousePressed = false, mouseReleased = false;
    private static Point2D initialMousePosition;
    private static Point2D lastMousePosition;

    static void enableInput(Scene scene) {
        scene.setOnMousePressed(event -> {
            mousePressed = true;
            double x = event.getSceneX();
            double y = event.getSceneY();
            initialMousePosition = new Point2D(x, y);
            lastMousePosition = new Point2D(x, y);
        });

        scene.setOnMouseDragged(event -> {
            double x = event.getSceneX(); // coordinata rispetto alla scena
            double y = event.getSceneY();
            lastMousePosition = new Point2D(x, y);
        });

        scene.setOnMouseReleased(event -> {
            mousePressed = false;
            mouseReleased = true;
        });
    }

    static boolean isMousePressed() {
        return mousePressed;
    } 

    static boolean isMouseReleased() {
        // if mouse is released, this methods must return true only the first time
        // it's called, then it must return to false.

        boolean previousState = mouseReleased;

        if (previousState == true) {
            mouseReleased = false;
        }   

        
        return previousState;
    }

    static double getInitialMousePositionX() {
        return initialMousePosition.getX();
    }

    static double getInitialMousePositionY() {
        return initialMousePosition.getY();
    }

    static double getLastMousePositionX() {
        return lastMousePosition.getX();
    }

    static double getLastMousePositionY() {
        return lastMousePosition.getY();
    }
}
