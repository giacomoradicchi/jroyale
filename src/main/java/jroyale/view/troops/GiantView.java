package jroyale.view.troops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Side;
import jroyale.shared.State;
import jroyale.utils.ImageUtils;

public class GiantView extends TroopView {

    public static final Map<Byte, Integer> NUM_FRAMES_PER_DIRECTION = getNumFramesPerDirection();
    
    private static GiantView instance;

    private static final Image RAW_SPELL_ICON = new Image(GiantView.class.getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + "spellIcon/giant.png"));

    private static final String RELATIVE_PATH = "smlbiobot cr-assets-png master assets-sc_chr_giant_out/";
    private static final String HEADER_NAME_FILE = "chr_giant_sprite_";
    private static final String FORMAT = ".png";
    private static final int NUM_FRAMES = 461;

    private final double SCALE = 0.65;
    private static final double shiftX = 4;
    private static final double shiftY = -12;


    private GiantView() {
        super();
    }

    
    @Override
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, byte state, byte side, double globalScale) {

        Image image = spriteBuffer.get(getFrameIndex(angleDirection, currentFrame, state));
        
        double width = image.getWidth() * SCALE * globalScale;
        double height = image.getHeight() * SCALE * globalScale;

        gc.drawImage(
            image, 
            shiftX + centreX - width/2 + isFlippedOnX(angleDirection) * width, 
            shiftY + centreY - height/2, 
            Math.pow(-1, isFlippedOnX(angleDirection)) * width, 
            height
        );
    }

    // giant: 189 x 185 -> 116 x 96
    // minipekka: 163 x 166 -> 93 x 72

    // 154
    // 

    @Override
    protected List<Image> getSpriteBuffer() {
        List<Image> buffer = new ArrayList<>();

        for (int i = 0; i < NUM_FRAMES; i++) {
            Image image = new Image(this.getClass().getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH + HEADER_NAME_FILE + getStringNumber(i) + FORMAT));
            image = ImageUtils.enhanceOpacity(image);
            image = ImageUtils.crop(image, 0, 0, (int) image.getWidth() - 30, (int) image.getHeight());
            buffer.add(image);
        }

        return buffer;
    }

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new GiantView();
        }
        return instance;
    }

    private int getFrameIndex(double angleDirection, int currentFrame, byte state) {
        return Math.max(0, Math.min(160 + getOffsetDirection(angleDirection) * NUM_FRAMES_PER_DIRECTION.get(state) + currentFrame, NUM_FRAMES - 1));
    }
    // 288
    private String getStringNumber(int number) {
        return String.format("%03d", number);
    }

    private static Map<Byte, Integer> getNumFramesPerDirection() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<Byte, Integer> numFrames = new HashMap<>();

        numFrames.put(State.WALK, 16);
        numFrames.put(State.SPAWN, 16);
        numFrames.put(State.ATTACK, 10);

        return numFrames;
    }
    // 448

    private int getOffsetState(byte state) {
        switch (state) {
            case State.IDLE:
                return 316;

            case State.SPAWN:
                return 0;

            case State.WALK:
                return 0;

            case State.ATTACK:
                return 0;
        
            default:
                return 0;
        }
    }

    @Override
    public Image getRawSpellIcon() {
        return RAW_SPELL_ICON;
    }
    
}


