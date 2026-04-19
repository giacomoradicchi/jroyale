package jroyale.view.entity_view.troops;

import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.AnimationKey;
import jroyale.view.Direction;
import jroyale.view.View;
import jroyale.view.entity_view.EntityView;

public class MiniPekkaView extends TroopView {

    private static Map<State, Integer> numFramesPerDirection;

    private static MiniPekkaView instance;

    private static final Image RAW_SPELL_ICON = new Image(MiniPekkaView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/minipekka.png"));

    private static final String TROOP_PATH = "minipekka/";
    private static final String HEADER_NAME_FILE = "chr_mini_pekka_sprite_";
    
    private static final int NUM_INDEX_DIGITS = 3;
    private static final double HEIGHT_IN_TILES = 1.8;

    // Sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 0;
    private static final int OPPONENT_IDLE_BASE_INDEX = 9;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = 108;
    private static final int PLAYER_ATTACK_BASE_INDEX = 0;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 225;



    private MiniPekkaView() {
        super();
    }

    
    @Override
    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame,
            State state, Side side) {
        

        AnimationKey key = new AnimationKey(side, state, direction.fromAngle(angleDirection));
        Image image = animationBuffer.get(key).getFrame(currentFrame);

        double width = image.getWidth() * getImageScale();
        double height = image.getHeight() * getImageScale();
        int flipped = 0;
        if (Direction.hasToFlip(angleDirection)) 
            flipped = 1;

        View.getInstance().renderWorldImage(image, centreX, centreY, Math.pow(-1, flipped) * width, height);
    }

    @Override
    public double getSpritesHeight() {
        return SPRITES_HEIGHT;
    }

    @Override
    protected double getHeightInTiles() {
        return HEIGHT_IN_TILES;
    }

    // instance methods

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
        return ImageUtils.enhanceOpacity(image);
    }

    @Override
    public EntityType getType() {
        return EntityType.MINIPEKKA;
    }

    // static methods

    public static EntityView getInstance() {
        if (instance == null) {
            instance = new MiniPekkaView();
        }
        return instance;
    }

    public static void setNumFramesPerDirection(Map<State, Integer> numFramesPerDirection) {
        MiniPekkaView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
    
}


