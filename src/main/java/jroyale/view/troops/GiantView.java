package jroyale.view.troops;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Side;
import jroyale.utils.ImageUtils;

public class GiantView extends TroopView {

    public static final int NUM_FRAMES_PER_DIRECTION = 16;
    
    private static GiantView instance;

    private static final String RELATIVE_PATH = "smlbiobot cr-assets-png 7099244ef1f3eb18014f6dbe5b10b222119083c9 assets-sc_chr_giant_out/";
    private static final String HEADER_NAME_FILE = "chr_giant_sprite_";
    private static final String FORMAT = ".png";
    private static final int NUM_FRAMES = 461;


    private GiantView() {
        super();
    }

    
    @Override
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, int side, double dx, double dy) {

        Image image = spriteBuffer.get(getFrameIndex(angleDirection, currentFrame));
        double width = 2.5 * 2 * dx; 
        double height = width * image.getWidth() / image.getHeight();
        

        gc.drawImage(image, centreX - Math.pow(-1, isFlippedOnX(angleDirection)) * width/2, centreY - height/2, Math.pow(-1, isFlippedOnX(angleDirection)) * width, height);
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

    private byte isFlippedOnX(double angleDirection) {
        if (angleDirection < -Math.PI/2  - Math.PI/8 || angleDirection > Math.PI/2 + Math.PI/8) {
            return 1;
        }
        return 0;
    }

    private int getFrameIndex(double angleDirection, int currentFrame) {
        return 288 + getOffsetDirection(angleDirection)*NUM_FRAMES_PER_DIRECTION + currentFrame;
    }

    private String getStringNumber(int number) {
        return String.format("%03d", number);
    }

    private int getOffsetDirection(double angleDirection) {

       
        
        if (angleDirection < -Math.PI/2) {
            angleDirection = -Math.PI - angleDirection;
        } else if (angleDirection > Math.PI/2) {
            angleDirection = +Math.PI - angleDirection;
        }

        angleDirection += Math.PI/2; // angle in [0, π]
        angleDirection /= Math.PI; // angle in [0, 1]
        angleDirection *= 8; // angle in [0, 8]

        return 8 - (int) Math.floor(angleDirection);
    }
    
}


