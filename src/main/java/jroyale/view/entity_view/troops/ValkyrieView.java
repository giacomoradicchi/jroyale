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

public class ValkyrieView extends TroopView {

    private static ValkyrieView instance = null;

    private static Map<State, Integer> numFramesPerDirection;

    private static final Image RAW_SPELL_ICON = new Image(ValkyrieView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/valkyrie.png"));

    private static final String TROOP_PATH = "valkyrie/";
    private static final String HEADER_NAME_FILE = "chr_valkyrie_sprite_";

    private static final String SWIRL_RELATIVE_PATH = "valkyrie/attack/swirl/chr_valkyrie_sprite.png";
    private static final Image SWIRL_IMAGE = new Image(ValkyrieView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + SWIRL_RELATIVE_PATH));
    

    private static final int NUM_INDEX_DIGITS = 3;
    private static final double shiftX = 0;
    private static final double shiftY = -10;
    private static final double ALPHA_THRESHOLD = 0.5;

    private static final double HEIGHT_IN_TILES = 2;

    // Sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 72;
    private static final int OPPONENT_IDLE_BASE_INDEX = 153;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = 81;
    private static final int PLAYER_ATTACK_BASE_INDEX = 271;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 163;

    private ValkyrieView() {}

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
        return ValkyrieView.numFramesPerDirection.get(state);
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
        Image temp = image;
        temp = ImageUtils.enhanceOpacity(temp);
        // TODO: farlo più robusto
        temp = ImageUtils.crop(image, 0, 0, (int) temp.getWidth() - 20, (int) temp.getHeight());
        return ImageUtils.enhanceOpacity(temp, 1*ALPHA_THRESHOLD);
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

        View.getInstance().renderWorldImage(image, centreX + shiftX, centreY + shiftY, Math.pow(-1, flipped) * width, height, 1);

        if(state == State.ATTACK)
            // swirl rendering
            View.getInstance().renderWorldImage(SWIRL_IMAGE, centreX + shiftX, centreY + shiftY, width, height, 1);
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
            instance = new ValkyrieView();
        }
        return instance;
    }

    public static void setNumFramesPerDirection(Map<State, Integer> numFramesPerDirection) {
        ValkyrieView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
    
}
