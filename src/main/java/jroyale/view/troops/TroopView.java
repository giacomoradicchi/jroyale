package jroyale.view.troops;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jroyale.view.AnimationKey;
import jroyale.view.Direction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.utils.ImageUtils;
import jroyale.view.SpriteAnimation;

public abstract class TroopView {
    
    protected static final String TROOPS_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/troops/";

    protected final Map<AnimationKey, SpriteAnimation> animationBuffer = new HashMap<>();
    protected final Image spellIcon;
    private static final int CORNER_RADIUS = 20; // pixels

    protected TroopView() {
        initAnimationBuffer();
        Image temp = ImageUtils.roundCorners(ImageUtils.cropToBoundingBox(getRawSpellIcon()), CORNER_RADIUS); // image will be centered by cropping it inside its Bounding Box.
        spellIcon = temp;
    }


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
        AnimationKey key = new AnimationKey(side, state, direction);
        SpriteAnimation animation = new SpriteAnimation();

        //int numFrames = NUM_FRAMES_PER_DIRECTION.get(state);
        int numFrames = getNumFramesPerDirection(state);

        // populating sprite animation data:
        for (int frame = 0; frame < numFrames; frame++) {
            String path = new StringBuilder()
                .append(TROOPS_PATH_RELATIVE_TO_RESOURCE)
                .append(getTroopPath())
                .append(getStatePath(state))
                .append(getHeaderNamePath())
                .append(String.format("%0" + getNumIndexDigits() + "d", 
                baseSpriteIndex + direction * numFrames + frame))
                .append(getFormat())
                .toString();
            
            Image sprite = new Image(this.getClass().getResourceAsStream(path));

            sprite = transformImage(sprite); // each troop can have a different type of transformation for every frame (i.e. cropping)
            
            animation.addSprite(sprite);
        }

        animationBuffer.put(key, animation);
    }

    //
    // abstract methods
    // 

    public abstract void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, double globalScale);

    protected abstract Image getRawSpellIcon();

    // methods for base index of a side based on the state of the troop
    protected abstract int getPlayerIdleBaseIndex();
    protected abstract int getOpponentIdleBaseIndex();
    protected abstract int getPlayerMoveBaseIndex();
    protected abstract int getOpponentMoveBaseIndex();
    protected abstract int getPlayerAttackBaseIndex();
    protected abstract int getOpponentAttackBaseIndex();

    protected abstract int getNumFramesPerDirection(State state);

    // methods for file path
    protected abstract String getTroopPath();
    protected abstract String getStatePath(State state);
    protected abstract String getHeaderNamePath();
    protected abstract int getNumIndexDigits();
    protected abstract String getFormat();

    // methods for image transforming:
    protected abstract Image transformImage(Image image);

    
}
