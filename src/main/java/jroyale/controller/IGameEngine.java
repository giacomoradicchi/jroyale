package jroyale.controller;

import javafx.stage.Stage;

public interface IGameEngine {

    public void start(Stage stage);

    public void startGameLoop();

    public void goToHome();

    public void stop();

    public void resume();
}
