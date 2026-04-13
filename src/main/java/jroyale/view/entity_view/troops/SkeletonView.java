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

public abstract class SkeletonView extends TroopView {

    private static Map<State, Integer> numFramesPerDirection;

    private static final String TROOP_PATH = "skeleton/";
    private static final String HEADER_NAME_FILE = "chr_skeleton_sprite_";

    private static final int NUM_INDEX_DIGITS = 3;
    private final double SCALE = 0.45;
    private static final double shiftX = 0;
    private static final double shiftY = -4;

    // Sprite sheet base indices for different states and sides.
    // 
    // skeleton has the same png's for both player and opponent, so it will always drawned 
    // the player side anyways. 
    // That's why opponent base indexes are set to -1.

    private static final int PLAYER_IDLE_BASE_INDEX = 72;
    private static final int OPPONENT_IDLE_BASE_INDEX = -1;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = -1;
    private static final int PLAYER_ATTACK_BASE_INDEX = 81;
    private static final int OPPONENT_ATTACK_BASE_INDEX = -1;


    // instance methods

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
    public void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame,
            State state, Side side) {
        
        // skeleton has the same png's for both player and opponent, so it will always drawned 
        // the player side anyways.
        AnimationKey key = new AnimationKey(Side.PLAYER, state, direction.fromAngle(angleDirection));
        Image image = animationBuffer.get(key).getFrame(currentFrame);

        double width = image.getWidth() * SCALE;
        double height = image.getHeight() * SCALE;
        int flipped = 0;
        if (Direction.hasToFlip(angleDirection)) 
            flipped = 1;
        
        

        View.getInstance().renderWorldImage(image, centreX + shiftX, centreY + shiftY, Math.pow(-1, flipped) * width, height);
    }

    @Override
    public double getSpritesHeight() {
        return SCALE * SPRITES_HEIGHT;
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
        return EntityType.SKELETONS;
    }

    // static methods

    
    public static void setNumFramesPerDirection(Map<State, Integer> numFramesPerDirection) {
        SkeletonView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }

}
