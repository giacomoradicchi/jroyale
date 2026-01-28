package jroyale.view.troops;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;
import jroyale.utils.ImageUtils;

public class GiantView extends TroopView {

    public static final Map<State, Integer> NUM_FRAMES_PER_DIRECTION = getNumFramesPerDirection();
    
    private static GiantView instance;

    private static final Image RAW_SPELL_ICON = new Image(GiantView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/giant.png"));



    
    

    private static final String TROOP_PATH = "giant/";
    private static final String ATTACK_PATH = "attack/";
    private static final String IDLE_PATH = "idle/";
    private static final String MOVE_PATH = "move/";
    private static final String HEADER_NAME_FILE = "chr_giant_sprite_";
    private static final String FORMAT = ".png";

    private static final Map<State, String> STATE_PATH = getStatePath();
    private static final int NUM_INDEX_DIGITS = 3;
    private static final int NUM_FRAMES = 461;
    private final double SCALE = 0.65;
    private static final double shiftX = 4;
    private static final double shiftY = -12;

    // Sprite sheet base indices for different states and sides
    private static final int PLAYER_IDLE_BASE_INDEX = 0;
    private static final int OPPONENT_IDLE_BASE_INDEX = 0;
    private static final int PLAYER_MOVE_BASE_INDEX = 0;
    private static final int OPPONENT_MOVE_BASE_INDEX = 0;
    private static final int PLAYER_ATTACK_BASE_INDEX = 0;
    private static final int OPPONENT_ATTACK_BASE_INDEX = 0;


    private GiantView() {
        super();
    }

    
    @Override
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side, double globalScale) {

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

        if (state == State.ATTACK && side == Side.OPPONENT) {
            // TODO: draw also the opponent image on top (specific for giant troop).
        }
        
    }

    // giant: 189 x 185 -> 116 x 96
    // minipekka: 163 x 166 -> 93 x 72

    // 154
    // 

    protected void initAnimationBuffer() {
        // TODO: build a specific buffer initialization for giant troop

    }
    

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new GiantView();
        }
        return instance;
    }

    
    private static Map<State, Integer> getNumFramesPerDirection() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<State, Integer> numFrames = new HashMap<>();

        numFrames.put(State.MOVE, 16);
        numFrames.put(State.IDLE, 1);
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
        Image temp = image;
        temp = ImageUtils.enhanceOpacity(temp);
        // TODO: farlo più robusto
        temp = ImageUtils.crop(image, 0, 0, (int) temp.getWidth() - 30, (int) temp.getHeight());
        return ImageUtils.enhanceOpacity(temp);
    }
    
}


