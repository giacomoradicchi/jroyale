package jroyale.controller;

import javafx.stage.Stage;

public interface IGameEngine {

    public void start(@SuppressWarnings("exports") Stage stage);

    public void startGameLoop();

    public void goToHome();

    public void stop();

    public void resume();

    // static methods
    public static IGameEngine getInstance() {
        return GameEngine.getInstance();
    }
}
