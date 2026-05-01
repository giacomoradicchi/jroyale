package jroyale.view;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import jroyale.controller.IControllerForView;

public abstract class View implements IView {

    private static final double NORMALIZED_LOGO_X = 0.5;
    private static final double NORMALIZED_LOGO_Y = 0.10;
    private static final double NORMALIZED_LOGO_WIDTH = 0.67;
    private static final Image LOADING_BACKGROUND = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/loading_background.png"));
    private static final double ALPHA = 1.0;

    protected static final Image LOGO = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/jroyale_logo.png"));

    protected IControllerForView controllerForView;

    @Override
    public void setController(IControllerForView controller) {
        controllerForView = controller;
    }

    @Override
    public void init() {
        // first it loads sprites on another thread with loadAsync, then onLoadFinished is called 
        loadAsync().setOnSucceeded(e -> {
            onLoadFinished();
        });
    }

    protected void renderLoadingScreen() {
        IMainGUI gui = getGUI();
        double height = gui.getCanvasHeight();
        double width = LOADING_BACKGROUND.getWidth() * height / LOADING_BACKGROUND.getHeight(); 
        gui.renderScreenImage(LOADING_BACKGROUND, gui.getCanvasWidth()/2, gui.getCanvasHeight()/2, width, height, false, ALPHA);
        
        width = gui.getCanvasWidth() * NORMALIZED_LOGO_WIDTH;
        height = LOGO.getHeight() / LOGO.getWidth() * width;
        gui.renderScreenImage(LOGO, gui.getCanvasWidth() * NORMALIZED_LOGO_X, gui.getCanvasHeight() * NORMALIZED_LOGO_Y, width, height, false, ALPHA);
    }

    private Task<Void> loadAsync() {
        renderLoadingScreen();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                loadSprites();
                return null;
            }
        };
        new Thread(task, "sprite-loading-thread").start();
        return task;
    }

    // since it runs on another thread, it is prohibited to execute JavaFX methods in here. 
    protected abstract void loadSprites();

    // now JavaFX methods can be executed (runs on main JavaFX thread). 
    protected abstract void onLoadFinished();
}
