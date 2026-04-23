package jroyale.view.home_view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import jroyale.controller.GameEngine;
import jroyale.utils.Config;
import jroyale.view.FontManager;
import jroyale.view.MainGUI;

public class HomeView extends MainGUI implements IHomeView {

    private static HomeView instance = null;

    // static constant
    private static final double NORMALIZED_PLAYBUTTON_X = 0.5;
    private static final double NORMALIZED_PLAYBUTTON_Y = 0.67;
    private static final double NORMALIZED_PLAYBUTTON_TEXT_HEIGHT = 0.05;
    private static final double NORMALIZED_DIFFICULTY_BOX_X = 0.5;
    private static final double NORMALIZED_DIFFICULTY_BOX_Y = NORMALIZED_PLAYBUTTON_Y + 2*NORMALIZED_PLAYBUTTON_TEXT_HEIGHT;
    private static final double NORMALIZED_DIFFICULTY_BOX_TEXT_HEIGHT = 0.04;
    private static final double NORMALIZED_ARENA_SPRITE_X = 0.5;
    private static final double NORMALIZED_ARENA_SPRITE_Y = 0.37;
    private static final double NORMALIZED_ARENA_SPRITE_WIDTH = 0.5;
    private static final double NORMALIZED_LOGO_X = 0.5;
    private static final double NORMALIZED_LOGO_Y = 0.15;
    private static final double NORMALIZED_LOGO_WIDTH = 0.67;
    
    private String difficulty;
    private Image homeBackground;
    private Image arenaSprite;

    private HomeView() {}

    @Override
    public void load() {
        this.homeBackground = new Image(HomeView.class.getResourceAsStream("/jroyale/images/ui/home_background.jpg"));
        this.arenaSprite = new Image(HomeView.class.getResourceAsStream("/jroyale/images/ui/ui_arena_sprite.png"));
    }

    @Override
    public void buildUI() {
        renderScreenImage(homeBackground, getCanvasWidth()/2, getCanvasHeight()/2, getCanvasWidth(), getCanvasHeight(), 1);
        
        double width = getCanvasWidth() * NORMALIZED_ARENA_SPRITE_WIDTH;
        double height = arenaSprite.getHeight() / arenaSprite.getWidth() * width;
        renderScreenImage(arenaSprite, getCanvasWidth() * NORMALIZED_ARENA_SPRITE_X, getCanvasHeight() * NORMALIZED_ARENA_SPRITE_Y, width, height, 1);

        width = getCanvasWidth() * NORMALIZED_LOGO_WIDTH;
        height = LOGO.getHeight() / LOGO.getWidth() * width;
        renderScreenImage(LOGO, getCanvasWidth() * NORMALIZED_LOGO_X, getCanvasHeight() * NORMALIZED_LOGO_Y, width, height, 1);
        
        Button playButton = new Button("Gioca Partita");
        FontManager fontManager = FontManager.getInstance();
        Font font = fontManager.getBoldFont(fontManager.getBoldFontSize(NORMALIZED_PLAYBUTTON_TEXT_HEIGHT * getCanvasHeight()));
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
            playButton.setLayoutX(NORMALIZED_PLAYBUTTON_X * getCanvasWidth() - newVal.getWidth()/2);
            playButton.setLayoutY(NORMALIZED_PLAYBUTTON_Y * getCanvasHeight() - newVal.getHeight()/2);
        });
        
        playButton.setOnAction(e -> GameEngine.getInstance().initGame());
        
        addToRoot(playButton);

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
            "-fx-font-family: '" + fontManager.getBoldFont(NORMALIZED_DIFFICULTY_BOX_TEXT_HEIGHT).getFamily() + "';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: #333333;"
        );

        difficultyBox.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            difficultyBox.setLayoutX(NORMALIZED_DIFFICULTY_BOX_X * getCanvasWidth() - newVal.getWidth()/2);
            difficultyBox.setLayoutY(NORMALIZED_DIFFICULTY_BOX_Y * getCanvasHeight() - newVal.getHeight()/2); 
        });

        difficultyBox.setOnAction(e -> {
            difficulty = difficultyBox.getValue();
            Config.getInstance().setDifficulty(difficulty);
        });
        difficultyBox.setFocusTraversable(false);

        addToRoot(difficultyBox);
    }

    @Override
    public void update(long now) {
        // TODO
    }

    @Override
    public void processOnMousePressed(double x, double y) {
        // TODO
    }

    @Override
    public void processOnMouseDragged(double x, double y) {
        // TODO
    }

    @Override
    public void processOnMouseReleased() {
        // TODO
    }

    // static methods
    public static IHomeView getInstance() {
        if (instance == null) {
            instance = new HomeView();
        }

        return instance;
    }
    
}
