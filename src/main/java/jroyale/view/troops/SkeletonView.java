package jroyale.view.troops;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.utils.ImageUtils;
import jroyale.view.AnimationKey;
import jroyale.view.Direction;

public abstract class SkeletonView extends TroopView {

    public static final Map<State, Integer> NUM_FRAMES_PER_DIRECTION = getNumFramesPerDirection();

    private static final String TROOP_PATH = "skeleton/";
    private static final String HEADER_NAME_FILE = "chr_skeleton_sprite_";

    private static final int NUM_INDEX_DIGITS = 3;
    private final double SCALE = 0.65;
    private static final double shiftX = 4;
    private static final double shiftY = -20;

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


    private static Map<State, Integer> getNumFramesPerDirection() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<State, Integer> numFrames = new HashMap<>();

        numFrames.put(State.MOVE, 8);
        numFrames.put(State.IDLE, 1);
        numFrames.put(State.ATTACK, 4);

        return numFrames;
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
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame,
            State state, Side side, double globalScale) {
        
        // skeleton has the same png's for both player and opponent, so it will always drawned 
        // the player side anyways.
        AnimationKey key = new AnimationKey(Side.PLAYER, state, Direction.fromAngle(angleDirection));
        Image image = animationBuffer.get(key).getFrame(currentFrame);

        double width = image.getWidth() * SCALE * globalScale;
        double height = image.getHeight() * SCALE * globalScale;
        int flipped = 0;
        if (Direction.hasToFlip(angleDirection)) 
            flipped = 1;

        gc.drawImage(
            image, 
            centreX - width/2 + flipped * width, 
            centreY - height/2, 
            Math.pow(-1, flipped) * width, 
            height
        );
    }

    @Override
    public int getNumFramesPerDirection(State state) {
        return NUM_FRAMES_PER_DIRECTION.get(state);
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

}
