package jroyale.view.home_view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import jroyale.controller.ControllerForView;
import jroyale.utils.Config;
import jroyale.view.FontManager;
import jroyale.view.IMainGUI;
import jroyale.view.MainGUI;
import jroyale.view.View;

public class HomeView extends View implements IHomeView {

    private static HomeView instance = null;

    // static constant
    private static final double NORMALIZED_ARENA_SPRITE_X = 0.5;
    private static final double NORMALIZED_ARENA_SPRITE_Y = 0.37;
    private static final double NORMALIZED_ARENA_SPRITE_WIDTH = 0.5;
    private static final double NORMALIZED_PLAYBUTTON_TEXT_HEIGHT = 0.05;
    private static final double NORMALIZED_PLAYBUTTON_X = 0.5;
    private static final double NORMALIZED_PLAYBUTTON_Y = 0.67;
    private static final double NORMALIZED_DIFFICULTY_BOX_TEXT_HEIGHT = 0.025;
    private static final double NORMALIZED_DIFFICULTY_BOX_X = 0.5;
    private static final double NORMALIZED_DIFFICULTY_BOX_Y = NORMALIZED_PLAYBUTTON_Y + 2*NORMALIZED_PLAYBUTTON_TEXT_HEIGHT;
    private static final double NORMALIZED_LOGO_X = 0.5;
    private static final double NORMALIZED_LOGO_Y = 0.15;
    private static final double NORMALIZED_LOGO_WIDTH = 0.67;
    
    private String difficulty;
    private Image homeBackground;
    private Image arenaSprite;

    private final IMainGUI gui;

    private HomeView() {
        this.gui = MainGUI.getInstance();
    }

    @Override
    protected void loadSprites() {
        this.homeBackground = new Image(HomeView.class.getResourceAsStream("/jroyale/images/ui/home_background.jpg"));
        this.arenaSprite = new Image(HomeView.class.getResourceAsStream("/jroyale/images/ui/ui_arena_sprite.png"));
    }

    private void buildUI() {
        gui.renderScreenImage(homeBackground, gui.getCanvasWidth()/2, gui.getCanvasHeight()/2, gui.getCanvasWidth(), gui.getCanvasHeight(), 1);
        
        double width = gui.getCanvasWidth() * NORMALIZED_ARENA_SPRITE_WIDTH;
        double height = arenaSprite.getHeight() / arenaSprite.getWidth() * width;
        gui.renderScreenImage(arenaSprite, gui.getCanvasWidth() * NORMALIZED_ARENA_SPRITE_X, gui.getCanvasHeight() * NORMALIZED_ARENA_SPRITE_Y, width, height, 1);

        width = gui.getCanvasWidth() * NORMALIZED_LOGO_WIDTH;
        height = LOGO.getHeight() / LOGO.getWidth() * width;
        gui.renderScreenImage(LOGO, gui.getCanvasWidth() * NORMALIZED_LOGO_X, gui.getCanvasHeight() * NORMALIZED_LOGO_Y, width, height, 1);
        
        Button playButton = new Button("Gioca Partita");
        FontManager fontManager = FontManager.getInstance();
        Font font = fontManager.getBoldFont(fontManager.getBoldFontSize(NORMALIZED_PLAYBUTTON_TEXT_HEIGHT * gui.getCanvasHeight()));
        playButton.setFont(font);
        playButton.setStyle(
            "-fx-background-color: #FFD700;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #B8860B;" +
            "-fx-border-width: 2;" +
            "-fx-text-fill: #333333;"
        );
        playButton.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            playButton.setLayoutX(NORMALIZED_PLAYBUTTON_X * gui.getCanvasWidth() - newVal.getWidth()/2);
            playButton.setLayoutY(NORMALIZED_PLAYBUTTON_Y * gui.getCanvasHeight() - newVal.getHeight()/2);
        });
        
        playButton.setOnAction(e -> {
            gui.resetNodes();
            ControllerForView.getInstance().initGameView();
        });
        
        gui.addToRoot(playButton);

        ComboBox<String> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("Basic", "Standard", "Expert", "Master");

        if (difficulty == null) {
            switch (Config.getInstance().getDifficulty().strip().toUpperCase()) {
                case "BASIC":
                    difficulty = "Basic";
                    break;
                case "STANDARD":
                    difficulty = "Standard";
                    break;
                case "EXPERT":
                    difficulty = "Expert";
                    break;
                case "MASTER":
                    difficulty = "Master";
                    break;
            
                default:
                    difficulty = "Standard";
                    break;
            }
        }

        difficultyBox.setValue(difficulty); // default value

        difficultyBox.setStyle(
            "-fx-background-color: #0095ff;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #1595e4;" +
            "-fx-border-width: 2;" +
            "-fx-font-family: '" + fontManager.getBoldFont(0).getFamily() + "';" +
            "-fx-font-size: " + (int) fontManager.getBoldFontSize(NORMALIZED_DIFFICULTY_BOX_TEXT_HEIGHT * gui.getCanvasHeight()) + "px;" + 
            "-fx-text-fill: #333333;"
        );

        difficultyBox.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            difficultyBox.setLayoutX(NORMALIZED_DIFFICULTY_BOX_X * gui.getCanvasWidth() - newVal.getWidth()/2);
            difficultyBox.setLayoutY(NORMALIZED_DIFFICULTY_BOX_Y * gui.getCanvasHeight() - newVal.getHeight()/2); 
        });

        difficultyBox.setOnAction(e -> {
            difficulty = difficultyBox.getValue();
            Config.getInstance().setDifficulty(difficulty);
        });
        difficultyBox.setFocusTraversable(false);

        gui.addToRoot(difficultyBox);
    }

    @Override
    protected void onLoadFinished() {
        buildUI();
    }

    @Override
    public void update(long now) {
        // empty
    }

    @Override
    public void processOnMousePressed(double x, double y) {
        // empty
    }

    @Override
    public void processOnMouseDragged(double x, double y) {
        // empty
    }

    @Override
    public void processOnMouseReleased() {
        // empty
    }

    // static methods
    public static IHomeView getInstance() {
        if (instance == null) {
            instance = new HomeView();
        }

        return instance;
    }

    @Override
    public IMainGUI getGUI() {
        return gui;
    }
    
}
