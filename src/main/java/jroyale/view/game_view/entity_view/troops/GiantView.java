package jroyale.view.game_view.entity_view.troops;

import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IMainGUI;
import jroyale.view.game_view.GameView;
import jroyale.view.game_view.IGameView;
import jroyale.view.game_view.animations.AnimationKey;
import jroyale.view.game_view.animations.Direction;
import jroyale.view.game_view.entity_view.EntityView;

public class GiantView extends TroopView {

    private static Map<State, Integer> numFramesPerDirection;
    
    private static GiantView instance;

    private static final Image RAW_SPELL_ICON = new Image(GiantView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/giant.png"));


    private static final String TROOP_PATH = "giant/";
    private static final String HEADER_NAME_FILE = "chr_giant_sprite_";
    private static final double ALPHA_THRESHOLD = 0.25;
    

    private static final int NUM_INDEX_DIGITS = 3;
    private static final double HEIGHT_IN_TILES = 3.2;
    private static final double NORMALIZED_SHIFT_Y = -0.03;

    // Sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 0;
    private static final int OPPONENT_IDLE_BASE_INDEX = 9;
    private static final int PLAYER_MOVE_BASE_INDEX = 144;
    private static final int OPPONENT_MOVE_BASE_INDEX = 0;
    private static final int PLAYER_ATTACK_BASE_INDEX = 0;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 90;


    private GiantView() {
        super();
    }

    
    @Override
    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side) {

        /* Image image = spriteBuffer.get(getFrameIndex(angleDirection, currentFrame, state));
        
        double width = image.getWidth() * SCALE * globalScale;
        double height = image.getHeight() * SCALE * globalScale;

        gc.drawImage(
            image, 
            shiftX + centreX - width/2 + isFlippedOnX(angleDirection) * width, 
            shiftY + centreY - height/2, 
            Math.pow(-1, isFlippedOnX(angleDirection)) * width, 
            height
        ); */

        Side baseSide = side;
        if (state != State.MOVE && side == Side.OPPONENT) {
            baseSide = Side.PLAYER;
        }

        AnimationKey key = new AnimationKey(baseSide, state, direction.fromAngle(angleDirection));
        Image image = animationBuffer.get(key).getFrame(currentFrame);
        
        IMainGUI gui = GameView.getInstance().getGUI();

        double canvasHeight = gui.getCanvasHeight();
        double scale = getImageScale();
        double width = image.getWidth() * scale;
        double height = image.getHeight() * scale;
        double shiftY = NORMALIZED_SHIFT_Y * canvasHeight;
        int flipped = 0;
        if (Direction.hasToFlip(angleDirection)) 
            flipped = 1;

        gui.renderWorldImage(image, centreX, centreY + shiftY, Math.pow(-1, flipped) * width, height, 1);

        if (state != State.MOVE && side == Side.OPPONENT) {
            key = new AnimationKey(side, state, direction.fromAngle(angleDirection));
            image = animationBuffer.get(key).getFrame(currentFrame);

            gui.renderWorldImage(image, centreX, centreY + shiftY, Math.pow(-1, flipped) * width, height, 1);
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
        // TODO: farlo più robusto
        temp = ImageUtils.crop(image, 0, 0, (int) temp.getWidth() - 30, (int) temp.getHeight());
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

    public static void setNumFramesPerDirection(Map<State, Integer> numFramesPerDirection) {
        GiantView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
}


