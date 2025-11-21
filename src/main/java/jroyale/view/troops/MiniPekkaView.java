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

public class MiniPekkaView extends TroopView {

    public static final Map<Byte, Integer> NUM_FRAMES_PER_DIRECTION = getNumFramesPerDirection();

    private static MiniPekkaView instance;

    private static final String RELATIVE_PATH = "smlbiobot cr-assets-png master assets-sc_chr_mini_pekka_out/";
    private static final String HEADER_NAME_FILE = "chr_mini_pekka_sprite_";
    private static final String FORMAT = ".png";
    private static final int NUM_FRAMES = 415;
    private final double SCALE = 0.45;


    private MiniPekkaView() {
        super();
    }

    
    @Override
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, byte state, int side, double globalScale) {

        /* if(state == State.IDLE) {
            return;
        } */
        


        Image image = spriteBuffer.get(getFrameIndex(angleDirection, currentFrame, state));

        double width = image.getWidth() * SCALE * globalScale;
        double height = image.getHeight() * SCALE * globalScale;

        gc.drawImage(
            image, 
            centreX - width/2 + isFlippedOnX(angleDirection) * width, 
            centreY - height/2, 
            Math.pow(-1, isFlippedOnX(angleDirection)) * width, 
            height
        );
    }

    @Override
    protected List<Image> getSpriteBuffer() {
        List<Image> buffer = new ArrayList<>();

        for (int i = 0; i < NUM_FRAMES; i++) {
            Image image = new Image(this.getClass().getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH + HEADER_NAME_FILE + getStringNumber(i) + FORMAT));
            image = ImageUtils.enhanceOpacity(image);
            buffer.add(image);
        }

        return buffer;
    }

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new MiniPekkaView();
        }
        return instance;
    }

    private int getFrameIndex(double angleDirection, int currentFrame, byte state) {
        return Math.max(0, Math.min(getOffsetState(state) + getOffsetDirection(angleDirection) * NUM_FRAMES_PER_DIRECTION.get(state) + currentFrame, NUM_FRAMES - 1));
    }

    private String getStringNumber(int number) {
        return String.format("%03d", number);
    }

    private int getOffsetState(byte state) {
        switch (state) {
            case State.IDLE:
                return 316;

            case State.SPAWN:
                return 0;

            case State.WALK:
                return 0;

            case State.ATTACK:
                return 325;
        
            default:
                return 0;
        }
    }

    

    private static Map<Byte, Integer> getNumFramesPerDirection() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<Byte, Integer> numFrames = new HashMap<>();

        numFrames.put(State.IDLE, 1);
        numFrames.put(State.WALK, 12);
        numFrames.put(State.SPAWN, 12);
        numFrames.put(State.ATTACK, 10);

        return numFrames;
    }
    
}


