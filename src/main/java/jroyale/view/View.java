package jroyale.view;

import java.io.File;

import javafx.application.Application;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jroyale.controller.IControllerForView;

public abstract class View implements IView {

    private static final double NORMALIZED_LOGO_X = 0.5;
    private static final double NORMALIZED_LOGO_Y = 0.10;
    private static final double NORMALIZED_LOGO_WIDTH = 0.67;
    private static final Image LOADING_BACKGROUND = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/loading_background.png"));
    private static final Image STARTUP_LOGO = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/unipg_logo.png"));
    private static final double NORMALIZED_STARTUP_WIDTH = 0.5;
    private static final boolean IS_STARTUP_LOGO_MONOCHROME = false;
    private static final Color STARTUP_BACKGROUND_COLOR = Color.BLACK;
    private static final Color STARTUP_FILL_COLOR = Color.WHITE;
    private static final double ALPHA = 1.0;
    private static final double STARTUP_TIME = 2;

    protected static final Image LOGO = new Image(MainGUI.class.getResourceAsStream("/jroyale/images/ui/jroyale_logo.png"));

    protected IControllerForView controllerForView;

    private static boolean isFirstTime = true;
    private AudioClip startSound = new AudioClip(getClass().getResource("/jroyale/sfx/supercell_jingle.mp3").toExternalForm());
    private AudioClip loadingSound = new AudioClip(getClass().getResource("/jroyale/sfx/scroll_loading_01.mp3").toExternalForm());
    private MediaPlayer loadingPlayer = new MediaPlayer(new Media(loadingSound.getSource()));

    @Override
    public void setController(IControllerForView controller) {
        controllerForView = controller;
    }

    @Override
    public void init() {
        if (isFirstTime) {
            // start view
            renderStartupScreen();
            isFirstTime = false;
            return;
        }
        
        // first it loads sprites on another thread with loadAsync, then onLoadFinished is called 
        loadAsync().setOnSucceeded(e -> {
            onLoadFinished();
        });
    }

    protected void renderStartupScreen() {
        startSound.play();

        IMainGUI gui = getGUI();
        double height = gui.getCanvasHeight();
        double width = gui.getCanvasWidth(); 
        double centerX = width/2;
        double centerY = height/2;
        
        // 1. render background
        gui.fillScreenRoundedRect(
            centerX, 
            centerY, 
            width, 
            height, 
            0, 
            0, 
            ALPHA, 
            STARTUP_BACKGROUND_COLOR
        );

        // 2. render circle and logo image
        double startupLogoWidth = width * NORMALIZED_STARTUP_WIDTH;
        double startupLogoHeight = STARTUP_LOGO.getHeight() * startupLogoWidth / STARTUP_LOGO.getWidth();
        gui.fillWorldRoundedRect(centerX, centerY, startupLogoWidth, startupLogoHeight, startupLogoWidth, startupLogoHeight, ALPHA, STARTUP_FILL_COLOR);
        gui.renderScreenImage(STARTUP_LOGO, centerX, centerY, startupLogoWidth, startupLogoHeight, IS_STARTUP_LOGO_MONOCHROME, ALPHA);
        
        // 3. start again init method (this time isFirstTime = false)
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(STARTUP_TIME), event -> {
            init();
        }));

        timer.play();                       // starts timer
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
        
        loadingSound.play();

        renderLoadingScreen();

        final long MIN_DURATION_MS = (long) (loadingPlayer.getTotalDuration().toSeconds() * 1000); // from seconds to ms

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                long startTime = System.currentTimeMillis();
                
                loadSprites();

                long elapsedTime = System.currentTimeMillis() - startTime;

                if (elapsedTime < MIN_DURATION_MS) {
                    Thread.sleep(MIN_DURATION_MS - elapsedTime);
                }
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
