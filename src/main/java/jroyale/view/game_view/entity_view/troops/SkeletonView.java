package jroyale.view.game_view.entity_view.troops;

import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.audio.AudioManager.AudioType;
import jroyale.view.game_view.IGameView;
import jroyale.view.game_view.animations.AnimationKey;
import jroyale.view.game_view.animations.Direction;

public abstract class SkeletonView extends TroopView {

    private static Map<State, Integer> numFramesPerDirection;

    private static final String TROOP_PATH = "skeleton/";
    private static final String HEADER_NAME_FILE = "chr_skeleton_sprite_";

    private static final int NUM_INDEX_DIGITS = 3;

    private static final double SHIFT_X = 0;
    private static final double SHIFT_Y = -4;
    private static final double HEIGHT_IN_TILES = 1.2;

    // render layer depth
    private static final int RENDER_LAYER = 1;

    private static final int NOT_FLIPPED = 0;
    private static final int FLIPPED = 1;

    // audio time
    private static final int HIT_FRAME = 2;
    private static final double VOLUME_SFX = 0.5;
    private static final double VOLUME_WALK_SFX = 0.3;

    // sprite sheet base indices for different states and sides
    //
    // skeleton has the same pngs for both player and opponent,
    // so it will always use the player side.
    // opponent base indexes are set to -1 for this reason.

    private static final int PLAYER_IDLE_BASE_INDEX = 72;
    private static final int OPPONENT_IDLE_BASE_INDEX = -1;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = -1;
    private static final int PLAYER_ATTACK_BASE_INDEX = 81;
    private static final int OPPONENT_ATTACK_BASE_INDEX = -1;

    private final IGameView gameView = IGameView.getInstance();

    protected SkeletonView() {
        super();

        audioManager.setVolume(AudioType.SEKELTONS_DEPLOY, VOLUME_SFX);
        audioManager.setVolume(AudioType.SEKELTONS_ATTACK, VOLUME_SFX);
        audioManager.setVolume(AudioType.SEKELTONS_MOVE, VOLUME_WALK_SFX);
    }

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
    public void renderEntity(
            double centreX,
            double centreY,
            double angleDirection,
            int currentFrame,
            State state,
            Side side
    ) {
        // skeleton has the same pngs for both player and opponent,
        // so it always renders using player animations
        AnimationKey key = new AnimationKey(
            Side.PLAYER,
            state,
            direction.fromAngle(angleDirection)
        );

        Image image = animationBuffer.get(key).getFrame(currentFrame);

        double width = image.getWidth() * getImageScale();
        double height = image.getHeight() * getImageScale();

        int flipped = NOT_FLIPPED;
        if (Direction.hasToFlip(angleDirection)) {
            flipped = FLIPPED;
        }

        gameView.getGUI().renderWorldImage(
            image,
            centreX + SHIFT_X,
            centreY + SHIFT_Y,
            Math.pow(-1, flipped) * width,
            height,
            false,
            RENDER_LAYER
        );
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

   @Override
    public void playMoveAudio(int currentFrame) {
        // empty
    }

    @Override
    public void playAttackAudio(int currentFrame) {    
        if (currentFrame == HIT_FRAME)
            audioManager.play(AudioType.SEKELTONS_ATTACK);
    }

    @Override
    public void playDeployAudio() {
        audioManager.play(AudioType.SEKELTONS_DEPLOY);
    }

    // static methods

    public static void setNumFramesPerDirection(
            Map<State, Integer> numFramesPerDirection
    ) {
        SkeletonView.numFramesPerDirection = Map.copyOf(numFramesPerDirection);
    }
}