package jroyale.view.troops;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.shared.Side;

public class MiniPekkaView extends TroopView {

    private static MiniPekkaView instance;

    private static final String MINIPEKKA_RELATIVE_PATH = "smlbiobot cr-assets-png master assets-sc_chr_mini_pekka_out/";
    private static final String NAME_FILE = "chr_mini_pekka_sprite_";
    private MiniPekkaView() {
        // empty constructor
    }

    int count = 0;
    
    @Override
    public void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int side, double dx, double dy) {
        Image image = new Image(this.getClass().getResourceAsStream(TROOPS_PATH_RELATIVE_TO_RESOURCE + MINIPEKKA_RELATIVE_PATH + NAME_FILE + getStringNumber(angleDirection) + ".png"));
        double width = 2 * dx; 
        double height = width * image.getWidth() / image.getHeight();
        /* centreX = 300;
        centreY = 300; */

        gc.drawImage(image, centreX - width/2, centreY - height/2, width, height);
    }

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new MiniPekkaView();
        }
        return instance;
    }

    private void setNumber() {
        count = (count + 1) % (12*4);
    }

    private String getStringNumber(double angleDirection) {
        setNumber();
        System.out.println(angleDirection);
        return String.format("%03d", getOffset(angleDirection)*12 + count / 4);

    }

    private int getOffset(double angleDirection) {

       
        
        if (angleDirection < -Math.PI/2) {
            angleDirection = -Math.PI/2;
        } else if (angleDirection > Math.PI/2) {
            angleDirection = +Math.PI/2;
        }

        angleDirection += Math.PI/2; // angle in [0, π]
        angleDirection /= Math.PI; // angle in [0, 1]
        angleDirection *= 8; // angle in [0, 8]

        return 8 - (int) Math.floor(angleDirection);
    }
    
}


