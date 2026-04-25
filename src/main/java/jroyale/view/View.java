package jroyale.view;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

public abstract class View implements IView {

    private static final double NORMALIZED_LOGO_X = 0.5;
    private static final double NORMALIZED_LOGO_Y = 0.10;
    private static final double NORMALIZED_LOGO_WIDTH = 0.67;
    private static final Image LOADING_BACKGROUND = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/loading_background.png"));

    protected static final Image LOGO = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/jroyale_logo.png"));


    @Override
    public void init() {
        loadAsync().setOnSucceeded(e -> {
            buildUI();
        });
    }

    protected void renderLoadingScreen() {
        IMainGUI gui = getGUI();
        double height = gui.getCanvasHeight();
        double width = LOADING_BACKGROUND.getWidth() * height / LOADING_BACKGROUND.getHeight(); 
        gui.renderScreenImage(LOADING_BACKGROUND, gui.getCanvasWidth()/2, gui.getCanvasHeight()/2, width, height, 1);
        
        width = gui.getCanvasWidth() * NORMALIZED_LOGO_WIDTH;
        height = LOGO.getHeight() / LOGO.getWidth() * width;
        gui.renderScreenImage(LOGO, gui.getCanvasWidth() * NORMALIZED_LOGO_X, gui.getCanvasHeight() * NORMALIZED_LOGO_Y, width, height, 1);
        
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

    protected abstract void loadSprites();

    protected abstract void buildUI();
}
