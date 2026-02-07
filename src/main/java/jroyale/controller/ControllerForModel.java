package jroyale.controller;

public class ControllerForModel implements IControllerForModel{

    private static IControllerForModel instance = null;

    @Override
    public void startGameLoop() {
        
    }

    public static IControllerForModel getInstance() {
        if (instance == null) {
            instance = new ControllerForModel();
        }
        return instance;
    }
    
}
