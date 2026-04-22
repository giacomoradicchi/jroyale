package jroyale.controller;

import javafx.stage.Stage;

public interface IGameEngine {

    public void start(Stage stage);

    public void startGame();

    public void stop();

    public void resume();
}
