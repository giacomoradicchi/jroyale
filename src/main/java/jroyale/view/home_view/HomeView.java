package jroyale.view.home_view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
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

    private String difficulty;

    private HomeView() {}

    @Override
    public void init() {
        this.fillScreenRoundedRect(getCanvasWidth()/2.0, getCanvasHeight()/2.0, getCanvasWidth(), getCanvasHeight(), 0, 0, 1, Color.BLUE);

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
        
        playButton.setOnAction(e -> GameEngine.getInstance().startGame());
        
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
            "-fx-background-color: #FFD700;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #B8860B;" +
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
            // TODO: send data to config
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
