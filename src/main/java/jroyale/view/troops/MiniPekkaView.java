package jroyale.view.troops;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Side;
import jroyale.utils.ImageUtils;

public class MiniPekkaView extends TroopView {

    public static final int NUM_FRAMES_PER_DIRECTION = 12;

    private static MiniPekkaView instance;

    private static final String RELATIVE_PATH = "smlbiobot cr-assets-png master assets-sc_chr_mini_pekka_out/";
    private static final String HEADER_NAME_FILE = "chr_mini_pekka_sprite_";
    private static final String FORMAT = ".png";
    private static final int NUM_FRAMES = 415;
    


    private MiniPekkaView() {
        super();
    }

    
    @Override
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, int side, double dx, double dy) {
        //Image image = new Image(this.getClass().getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + MINIPEKKA_RELATIVE_PATH + NAME_FILE + getStringNumber(getFrameIndex(angleDirection, currentFrame)) + FORMAT));
        Image image = spriteBuffer.get(getFrameIndex(angleDirection, currentFrame));
        image = ImageUtils.enhanceOpacity(image);
        double width = 1.6 * 2 * dx; 
        double height = width * image.getWidth() / image.getHeight();
        /* centreX = 300;
        centreY = 300; */

        gc.drawImage(image, centreX - Math.pow(-1, isFlippedOnX(angleDirection)) * width/2, centreY - height/2, Math.pow(-1, isFlippedOnX(angleDirection)) * width, height);
    }

    @Override
    protected List<Image> getSpriteBuffer() {
        List<Image> buffer = new ArrayList<>();

        for (int i = 0; i < NUM_FRAMES; i++) {
            buffer.add(new Image(this.getClass().getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + RELATIVE_PATH + HEADER_NAME_FILE + getStringNumber(i) + FORMAT)));
        }

        return buffer;
    }

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new MiniPekkaView();
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
        return getOffsetDirection(angleDirection)*NUM_FRAMES_PER_DIRECTION + currentFrame;
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


