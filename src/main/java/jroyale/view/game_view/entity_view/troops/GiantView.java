package jroyale.view.game_view.entity_view.troops;

import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IMainGUI;
import jroyale.view.game_view.IGameView;
import jroyale.view.game_view.animations.AnimationKey;
import jroyale.view.game_view.animations.Direction;
import jroyale.view.game_view.entity_view.EntityView;

public class GiantView extends TroopView {

    private static Map<State, Integer> numFramesPerDirection;

    private static GiantView instance;

    private static final Image RAW_SPELL_ICON = new Image(
        GiantView.class.getResourceAsStream(
            TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/giant.png"
        )
    );

    private static final String TROOP_PATH = "giant/";
    private static final String HEADER_NAME_FILE = "chr_giant_sprite_";
    private static final double ALPHA_THRESHOLD = 0.25;

    private static final int NUM_INDEX_DIGITS = 3;
    private static final double HEIGHT_IN_TILES = 3.2;
    private static final double NORMALIZED_SHIFT_Y = -0.03;

    // render layer depth
    private static final int RENDER_LAYER = 1;

    // horizontal crop offset from right
    private static final int RIGHT_CROP_PIXELS = 30;

    private static final int CROP_START_X = 0;
    private static final int CROP_START_Y = 0;

    private static final int NOT_FLIPPED = 0;
    private static final int FLIPPED = 1;

    // sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 0;
    private static final int OPPONENT_IDLE_BASE_INDEX = 9;
    private static final int PLAYER_MOVE_BASE_INDEX = 144;
    private static final int OPPONENT_MOVE_BASE_INDEX = 0;
    private static final int PLAYER_ATTACK_BASE_INDEX = 0;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 90;

    private final IGameView gameView = IGameView.getInstance();

    private GiantView() {
        super();
    }

    @Override
    public void renderEntity(
            double centreX,
            double centreY,
            double angleDirection,
            int currentFrame,
            State state,
            Side side
    ) {
        Side baseSide = side;
        if (state != State.MOVE && side == Side.OPPONENT) {
            baseSide = Side.PLAYER;
        }

        AnimationKey key = new AnimationKey(
            baseSide,
            state,
            direction.fromAngle(angleDirection)
        );

        Image image = animationBuffer.get(key).getFrame(currentFrame);

        IMainGUI gui = gameView.getGUI();

        double canvasHeight = gui.getCanvasHeight();
        double scale = getImageScale();
        double width = image.getWidth() * scale;
        double height = image.getHeight() * scale;
        double shiftY = NORMALIZED_SHIFT_Y * canvasHeight;

        int flipped = NOT_FLIPPED;
        if (Direction.hasToFlip(angleDirection)) {
            flipped = FLIPPED;
        }

        gui.renderWorldImage(
            image,
            centreX,
            centreY + shiftY,
            Math.pow(-1, flipped) * width,
            height,
            false,
            RENDER_LAYER
        );

        if (state != State.MOVE && side == Side.OPPONENT) {
            key = new AnimationKey(
                side,
                state,
                direction.fromAngle(angleDirection)
            );

            image = animationBuffer.get(key).getFrame(currentFrame);

            gui.renderWorldImage(
                image,
                centreX,
                centreY + shiftY,
                Math.pow(-1, flipped) * width,
                height,
                false,
                RENDER_LAYER
            );
        }
    }

    @Override
    public double getSpritesHeight() {
        return SPRITES_HEIGHT;
    }

    @Override
    protected double getHeightInTiles() {
        return HEIGHT_IN_TILES;
    }

    @Override
    public Image getRawSpellIcon() {
        return RAW_SPELL_ICON;
    }

    @Override
    protected int getPlayerIdleBaseIndex() {
        return PLAYER_IDLE_BASE_INDEX;
    }

    @Override
    protected int getOpponentIdleBaseIndex() {
        return OPPONENT_IDLE_BASE_INDEX;
    }

    @Override
    protected int getPlayerMoveBaseIndex() {
        return PLAYER_MOVE_BASE_INDEX;
    }

    @Override
    protected int getOpponentMoveBaseIndex() {
        return OPPONENT_MOVE_BASE_INDEX;
    }

    @Override
    protected int getPlayerAttackBaseIndex() {
        return PLAYER_ATTACK_BASE_INDEX;
    }

    @Override
    protected int getOpponentAttackBaseIndex() {
        return OPPONENT_ATTACK_BASE_INDEX;
    }

    @Override
    public int getNumFramesPerDirection(State state) {
        return numFramesPerDirection.get(state);
    }

    @Override
    protected String getTroopPath() {
        return TROOP_PATH;
    }

    @Override
    protected String getHeaderNamePath() {
        return HEADER_NAME_FILE;
    }

    @Override
    protected int getNumIndexDigits() {
        return NUM_INDEX_DIGITS;
    }

    @Override
    protected Image transformImage(Image image) {
        Image temp = image;

        // crop transparent pixels from right
        temp = ImageUtils.crop(
            image,
            CROP_START_X,
            CROP_START_Y,
            (int) temp.getWidth() - RIGHT_CROP_PIXELS,
            (int) temp.getHeight()
        );

        return ImageUtils.enhanceOpacity(temp, ALPHA_THRESHOLD);
    }

    @Override
    public EntityType getType() {
        return EntityType.GIANT;
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new GiantView();
        }
        return instance;
    }

    public static void setNumFramesPerDirection(
            Map<State, Integer> numFramesPerDirection
    ) {
        GiantView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
}
