package jroyale.view.game_view.entity_view.troops;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.game_view.animations.AnimationKey;
import jroyale.view.game_view.animations.Direction;
import jroyale.view.game_view.animations.SpriteAnimation;
import jroyale.view.game_view.entity_view.EntityView;

public abstract class TroopView extends EntityView {
    
    protected static final String TROOPS_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/troops/";

    protected final Map<AnimationKey, SpriteAnimation> animationBuffer = new HashMap<>();
    protected final double SPRITES_HEIGHT;
    protected final Image spellIcon;
    private static final int CORNER_RADIUS = 20; // pixels
    protected final Direction direction;
    private static final double OPACITY_TOLERANCE = 0.5; // avoids all pixels whose alpha value is less than OPACITY_TOLERANCE
    private static final int REFERENCE_DIRECTION = 8; // it will be used the last direction's sprite as reference to compute sprite's height

    // pattern for buffer initialization:
    protected static final String ATTACK_PATH = "attack/";
    protected static final String IDLE_PATH = "idle/";
    protected static final String MOVE_PATH = "move/";
    protected static final String FORMAT = ".png";
    private static final Map<State, String> STATE_PATH = getStatePath();

    protected TroopView() {
        initAnimationBuffer();
        direction = new Direction();

        // calculating sprite height in pixel excluding transparent ones.
        Image temp = animationBuffer.get(new AnimationKey(Side.PLAYER, State.IDLE, REFERENCE_DIRECTION)).getFrame(0); 
        SPRITES_HEIGHT = ImageUtils.getAlphaBoundingBox(temp, OPACITY_TOLERANCE).getHeight();

        // setting spell icon
        temp = ImageUtils.roundCorners(ImageUtils.cropToBoundingBox(getRawSpellIcon()), CORNER_RADIUS); // image will be centered by cropping it inside its Bounding Box.
        spellIcon = temp;
    }

    
    @Override
    public Image getSpellIcon() {
        return spellIcon;
    }

    protected void initAnimationBuffer() {

        for (int direction = 0; direction < Direction.NUM_DIRECTIONS; direction++) {

            // generating sprite animation for idle (single frame per direction):
            loadSprites(State.IDLE, Side.PLAYER, direction, getPlayerIdleBaseIndex());
            loadSprites(State.IDLE, Side.OPPONENT, direction, getOpponentIdleBaseIndex());

            // generating sprite animation for moving state:
            loadSprites(State.MOVE, Side.PLAYER, direction, getPlayerMoveBaseIndex());
            loadSprites(State.MOVE, Side.OPPONENT, direction, getOpponentMoveBaseIndex());

            // generatin animation for attack state:
            loadSprites(State.ATTACK, Side.PLAYER, direction, getPlayerAttackBaseIndex());
            loadSprites(State.ATTACK, Side.OPPONENT, direction, getOpponentAttackBaseIndex());

        }

    }

    protected void loadSprites(State state, Side side, int direction, int baseSpriteIndex) {
        if (baseSpriteIndex == -1) {
            // this means there are no sprites to load for a certain side and state.
            return;
        }

        AnimationKey key = new AnimationKey(side, state, direction);
        SpriteAnimation animation = new SpriteAnimation();

        //int numFrames = NUM_FRAMES_PER_DIRECTION.get(state);
        int numFrames = getNumFramesPerDirection(state);

        // populating sprite animation data:
        for (int frame = 0; frame < numFrames; frame++) {
            String path = new StringBuilder()
                .append(TROOPS_PATH_RELATIVE_TO_RESOURCE)
                .append(getTroopPath())
                .append(STATE_PATH.get(state))
                .append(getHeaderNamePath())
                .append(String.format("%0" + getNumIndexDigits() + "d", 
                baseSpriteIndex + direction * numFrames + frame))
                .append(FORMAT)
                .toString();
            
            Image sprite = new Image(this.getClass().getResourceAsStream(path));

            sprite = transformImage(sprite); // each troop can have a different type of transformation for every frame (i.e. cropping)
            
            animation.addSprite(sprite);
        }

        animationBuffer.put(key, animation);
    }

    private static Map<State, String> getStatePath() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<State, String> statePath = new HashMap<>();

        statePath.put(State.IDLE, IDLE_PATH);
        statePath.put(State.MOVE, MOVE_PATH);
        statePath.put(State.ATTACK, ATTACK_PATH);

        return statePath;
    }

    //
    // abstract methods
    // 

    protected abstract Image getRawSpellIcon();

    // methods for base index of a side based on the state of the troop
    protected abstract int getPlayerIdleBaseIndex();

    protected abstract int getOpponentIdleBaseIndex();

    protected abstract int getPlayerMoveBaseIndex();

    protected abstract int getOpponentMoveBaseIndex();

    protected abstract int getPlayerAttackBaseIndex();

    protected abstract int getOpponentAttackBaseIndex();

    public abstract int getNumFramesPerDirection(State state);


    // methods for file path
    protected abstract String getHeaderNamePath();

    protected abstract String getTroopPath();

    protected abstract int getNumIndexDigits();

    // methods for image transforming:
    protected abstract Image transformImage(Image image);
}
