package jroyale.controller;

import javafx.animation.AnimationTimer;
import jroyale.model.IModel;
import jroyale.view.IView;

public class Controller implements IController {

    private IModel model;
    private IView view;

    public Controller(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }
    
    @Override
    public void start() {
        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.update();
                view.render();
            }
        };
        loop.start();
    }
}
