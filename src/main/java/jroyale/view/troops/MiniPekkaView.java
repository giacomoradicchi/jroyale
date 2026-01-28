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

public class MiniPekkaView extends TroopView {

    public static final Map<State, Integer> NUM_FRAMES_PER_DIRECTION = getNumFramesPerDirection();

    private static MiniPekkaView instance;

    private static final Image RAW_SPELL_ICON = new Image(MiniPekkaView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/minipekka.png"));

    private static final String TROOP_PATH = "minipekka/";
    private static final String ATTACK_PATH = "attack/";
    private static final String IDLE_PATH = "idle/";
    private static final String MOVE_PATH = "move/";
    private static final String HEADER_NAME_FILE = "chr_mini_pekka_sprite_";
    private static final String FORMAT = ".png";
    private static final Map<State, String> STATE_PATH = getStatePath();
    private static final int NUM_INDEX_DIGITS = 3;
    private static final int NUM_FRAMES = 415;
    private final double SCALE = 0.45;

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
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame,
            State state, Side side, double globalScale) {

        /* if(state == State.IDLE) {
            return;
        } */
        

        AnimationKey key = new AnimationKey(side, state, Direction.fromAngle(angleDirection));
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

    /* @Override
    protected List<Image> getSpriteBuffer() {
        List<Image> buffer = new ArrayList<>();

        for (int i = 0; i < NUM_FRAMES; i++) {
            Image image = new Image(this.getClass().getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH + HEADER_NAME_FILE + getStringNumber(i, 3) + FORMAT));
            image = ImageUtils.enhanceOpacity(image);
            buffer.add(image);
        }

        return buffer;
    } */

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new MiniPekkaView();
        }
        return instance;
    }

    private String getStringNumber(int number, int digits) {
        return String.format("%0" + digits + "d", number);
    }

    

    

    private static Map<State, Integer> getNumFramesPerDirection() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<State, Integer> numFrames = new HashMap<>();

        numFrames.put(State.IDLE, 1);
        numFrames.put(State.MOVE, 12);
        numFrames.put(State.ATTACK, 10);

        return numFrames;
    }

    private static Map<State, String> getStatePath() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<State, String> statePath = new HashMap<>();

        statePath.put(State.IDLE, IDLE_PATH);
        statePath.put(State.MOVE, MOVE_PATH);
        statePath.put(State.ATTACK, ATTACK_PATH);

        return statePath;
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
    protected int getNumFramesPerDirection(State state) {
        return NUM_FRAMES_PER_DIRECTION.get(state);
    }

    @Override
    protected String getTroopPath() {
        return TROOP_PATH;
    }

    @Override
    protected String getStatePath(State state) {
        return STATE_PATH.get(state);
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
    protected String getFormat() {
        return FORMAT;
    }

    @Override
    protected Image transformImage(Image image) {
        return ImageUtils.enhanceOpacity(image);
    }
    
}


