package jroyale.view.home_view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import jroyale.utils.Config;
import jroyale.view.FontManager;
import jroyale.view.IMainGUI;
import jroyale.view.View;

public class HomeView extends View implements IHomeView {

    private static HomeView instance = null;

    // normalized positions and sizes

    private static final double NORMALIZED_ARENA_SPRITE_X = 0.5;
    private static final double NORMALIZED_ARENA_SPRITE_Y = 0.37;
    private static final double NORMALIZED_ARENA_SPRITE_WIDTH = 0.5;

    private static final double NORMALIZED_PLAY_BUTTON_TEXT_HEIGHT = 0.05;
    private static final double NORMALIZED_PLAY_BUTTON_X = 0.5;
    private static final double NORMALIZED_PLAY_BUTTON_Y = 0.67;

    private static final double NORMALIZED_DIFFICULTY_BOX_TEXT_HEIGHT = 0.025;
    private static final double NORMALIZED_DIFFICULTY_BOX_X = 0.5;
    private static final double NORMALIZED_DIFFICULTY_BOX_Y =
        NORMALIZED_PLAY_BUTTON_Y
        + 2 * NORMALIZED_PLAY_BUTTON_TEXT_HEIGHT;

    private static final double NORMALIZED_LOGO_X = 0.5;
    private static final double NORMALIZED_LOGO_Y = 0.15;
    private static final double NORMALIZED_LOGO_WIDTH = 0.67;

    // render layer depth

    private static final int RENDER_LAYER = 1;

    // style constants

    private static final int BORDER_RADIUS = 15;
    private static final int BORDER_WIDTH = 2;

    private static final String DEFAULT_CONFIG_DIFFICULTY = "STANDARD";
    private static final String DEFAULT_UI_DIFFICULTY = "Standard";

    private static final String PLAY_BUTTON_TEXT = "Gioca Partita";

    private static final String DIFFICULTY_BASIC = "Basic";
    private static final String DIFFICULTY_STANDARD = "Standard";
    private static final String DIFFICULTY_EXPERT = "Expert";
    private static final String DIFFICULTY_MASTER = "Master";

    private static final String PLAY_BUTTON_STYLE =
        "-fx-background-color: #FFD700;" +
        "-fx-background-radius: " + BORDER_RADIUS + ";" +
        "-fx-border-radius: " + BORDER_RADIUS + ";" +
        "-fx-border-color: #B8860B;" +
        "-fx-border-width: " + BORDER_WIDTH + ";" +
        "-fx-text-fill: #333333;";

    private static final String DIFFICULTY_BOX_BASE_STYLE =
        "-fx-background-color: #0095ff;" +
        "-fx-background-radius: " + BORDER_RADIUS + ";" +
        "-fx-border-radius: " + BORDER_RADIUS + ";" +
        "-fx-border-color: #1595e4;" +
        "-fx-border-width: " + BORDER_WIDTH + ";" +
        "-fx-text-fill: #333333;";

    private String difficulty;

    private Image homeBackground;
    private Image arenaSprite;

    private final IMainGUI gui;

    private HomeView() {
        this.gui = IMainGUI.getInstance();
    }

    @Override
    protected void loadSprites() {
        this.homeBackground = new Image(
            HomeView.class.getResourceAsStream(
                "/jroyale/images/ui/home_background.jpg"
            )
        );

        this.arenaSprite = new Image(
            HomeView.class.getResourceAsStream(
                "/jroyale/images/ui/ui_arena_sprite.png"
            )
        );
    }

    private void buildUI() {
        renderBackground();
        renderArena();
        renderLogo();

        createPlayButton();
        createDifficultyBox();
    }

    private void renderBackground() {
        gui.renderScreenImage(
            homeBackground,
            gui.getCanvasWidth() / 2,
            gui.getCanvasHeight() / 2,
            gui.getCanvasWidth(),
            gui.getCanvasHeight(),
            false,
            RENDER_LAYER
        );
    }

    private void renderArena() {
        double width =
            gui.getCanvasWidth() * NORMALIZED_ARENA_SPRITE_WIDTH;

        double height =
            arenaSprite.getHeight()
            / arenaSprite.getWidth()
            * width;

        gui.renderScreenImage(
            arenaSprite,
            gui.getCanvasWidth() * NORMALIZED_ARENA_SPRITE_X,
            gui.getCanvasHeight() * NORMALIZED_ARENA_SPRITE_Y,
            width,
            height,
            false,
            RENDER_LAYER
        );
    }

    private void renderLogo() {
        double width =
            gui.getCanvasWidth() * NORMALIZED_LOGO_WIDTH;

        double height =
            LOGO.getHeight()
            / LOGO.getWidth()
            * width;

        gui.renderScreenImage(
            LOGO,
            gui.getCanvasWidth() * NORMALIZED_LOGO_X,
            gui.getCanvasHeight() * NORMALIZED_LOGO_Y,
            width,
            height,
            false,
            RENDER_LAYER
        );
    }

    private void createPlayButton() {
        Button playButton = new Button(PLAY_BUTTON_TEXT);

        FontManager fontManager = FontManager.getInstance();

        Font font = fontManager.getBoldFont(
            fontManager.getBoldFontSize(
                NORMALIZED_PLAY_BUTTON_TEXT_HEIGHT
                * gui.getCanvasHeight()
            )
        );

        playButton.setFont(font);
        playButton.setStyle(PLAY_BUTTON_STYLE);

        playButton.layoutBoundsProperty().addListener(
            (obs, oldVal, newVal) -> {
                playButton.setLayoutX(
                    NORMALIZED_PLAY_BUTTON_X * gui.getCanvasWidth()
                    - newVal.getWidth() / 2
                );

                playButton.setLayoutY(
                    NORMALIZED_PLAY_BUTTON_Y * gui.getCanvasHeight()
                    - newVal.getHeight() / 2
                );
            }
        );

        playButton.setOnAction(e -> {
            gui.resetNodes();
            controllerForView.initGameView();
        });

        gui.addToRoot(playButton);
    }

    private void createDifficultyBox() {
        ComboBox<String> difficultyBox = new ComboBox<>();

        difficultyBox.getItems().addAll(
            DIFFICULTY_BASIC,
            DIFFICULTY_STANDARD,
            DIFFICULTY_EXPERT,
            DIFFICULTY_MASTER
        );

        if (difficulty == null) {
            setConfigDifficulty();
        }

        difficultyBox.setValue(difficulty);

        FontManager fontManager = FontManager.getInstance();

        String style =
            DIFFICULTY_BOX_BASE_STYLE +
            "-fx-font-family: '" +
            fontManager.getBoldFont(0).getFamily() +
            "';" +
            "-fx-font-size: " +
            (int) fontManager.getBoldFontSize(
                NORMALIZED_DIFFICULTY_BOX_TEXT_HEIGHT
                * gui.getCanvasHeight()
            ) +
            "px;";

        difficultyBox.setStyle(style);

        difficultyBox.layoutBoundsProperty().addListener(
            (obs, oldVal, newVal) -> {
                difficultyBox.setLayoutX(
                    NORMALIZED_DIFFICULTY_BOX_X * gui.getCanvasWidth()
                    - newVal.getWidth() / 2
                );

                difficultyBox.setLayoutY(
                    NORMALIZED_DIFFICULTY_BOX_Y * gui.getCanvasHeight()
                    - newVal.getHeight() / 2
                );
            }
        );

        difficultyBox.setOnAction(e -> {
            difficulty = difficultyBox.getValue();
            Config.getInstance().setDifficulty(difficulty);
        });

        difficultyBox.setFocusTraversable(false);

        gui.addToRoot(difficultyBox);
    }

    private void setConfigDifficulty() {
        String configDifficulty =
            Config.getInstance().getDifficulty();

        if (configDifficulty == null) {
            Config.getInstance().setDifficulty(
                DEFAULT_CONFIG_DIFFICULTY
            );

            difficulty = DEFAULT_UI_DIFFICULTY;
            return;
        }

        switch (configDifficulty.strip().toUpperCase()) {
            case "BASIC":
                difficulty = DIFFICULTY_BASIC;
                break;

            case "STANDARD":
                difficulty = DIFFICULTY_STANDARD;
                break;

            case "EXPERT":
                difficulty = DIFFICULTY_EXPERT;
                break;

            case "MASTER":
                difficulty = DIFFICULTY_MASTER;
                break;

            default:
                difficulty = DEFAULT_UI_DIFFICULTY;
                break;
        }
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

    @Override
    public IMainGUI getGUI() {
        return gui;
    }

    // static methods

    public static IHomeView getInstance() {
        if (instance == null) {
            instance = new HomeView();
        }

        return instance;
    }
}