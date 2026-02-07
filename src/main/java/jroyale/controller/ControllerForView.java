package jroyale.controller;

import javafx.stage.Stage;
import jroyale.view.View2;

public class ControllerForView implements IControllerForView {

    private static ControllerForView instance;

    private ControllerForView() {
        // empty
    }

    
    @Override
    public void openWindow(Stage stage) {
        View2.getInstance().openWindow(stage);
        ControllerForModel.getInstance().startGameLoop();
    }

    // static methods
    public static IControllerForView getInstance() {
        if (instance == null) {
            instance = new ControllerForView();
        }

        return instance;
    }
    
}
