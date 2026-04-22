package jroyale.view.game_view.entity_view.troops;

import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.game_view.GameView;
import jroyale.view.game_view.animations.AnimationKey;
import jroyale.view.game_view.animations.Direction;
import jroyale.view.game_view.entity_view.EntityView;

public class PekkaView extends TroopView{

    private static PekkaView instance = null;

    private static Map<State, Integer> numFramesPerDirection;

    private static final Image RAW_SPELL_ICON = new Image(PekkaView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/pekka.png"));

    private static final String TROOP_PATH = "pekka/";
    private static final String HEADER_NAME_FILE = "chr_pekka_sprite_";
    

    private static final int NUM_INDEX_DIGITS = 3;
    private static final double shiftX = 0;
    private static final double shiftY = -25;
    private static final double HEIGHT_IN_TILES = 2.8;

    // Sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 126;
    private static final int OPPONENT_IDLE_BASE_INDEX = 261;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = 135;
    private static final int PLAYER_ATTACK_BASE_INDEX = 333;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 270;

    private PekkaView() {}

    @Override
    protected Image getRawSpellIcon() {
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
        return PekkaView.numFramesPerDirection.get(state);
    }

    @Override
    protected String getHeaderNamePath() {
        return HEADER_NAME_FILE;
    }

    @Override
    protected String getTroopPath() {
        return TROOP_PATH;
    }

    @Override
    protected int getNumIndexDigits() {
        return NUM_INDEX_DIGITS;
    }

    @Override
    protected Image transformImage(Image image) {
        return ImageUtils.enhanceOpacity(image);
    }

    @Override
    public EntityType getType() {
        return EntityType.PEKKA;
    }

    @Override
    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state,
            Side side) {
        AnimationKey key = new AnimationKey(side, state, direction.fromAngle(angleDirection));
        Image image = animationBuffer.get(key).getFrame(currentFrame);

        double width = image.getWidth() * getImageScale();
        double height = image.getHeight() * getImageScale();
        int flipped = 0;
        if (Direction.hasToFlip(angleDirection)) 
            flipped = 1;

        GameView.getInstance().renderWorldImage(image, centreX + shiftX, centreY + shiftY, Math.pow(-1, flipped) * width, height, 1);
    }

    @Override
    public double getSpritesHeight() {
        return SPRITES_HEIGHT;
    }

    @Override
    protected double getHeightInTiles() {
        return HEIGHT_IN_TILES;
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new PekkaView();
        }
        return instance;
    }

    public static void setNumFramesPerDirection(Map<State, Integer> numFramesPerDirection) {
        PekkaView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
    
}
