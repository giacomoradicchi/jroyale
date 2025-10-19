package jroyale.view;

import javafx.scene.image.Image;
import jroyale.shared.Side;
import jroyale.utils.ImageUtils;

public class ArcherTower extends Tower{
    private final static double SCALE = 0.39;
    private final static double X_OFFSET = 0;
    private final static double Y_OFFSET = 6;
    private final static String[] ARCHERTOWER_RELATIVE_PATH = new String[] {
        "/jroyale/images/towers/player/archer_tower.png", // player
        "/jroyale/images/towers/opponent/archer_tower.png" // opponent
    };


    ArcherTower(int side) {
        super(getArcherTowerImage(checkSide(side)), checkSide(side), SCALE, X_OFFSET, Y_OFFSET);
    }

    private static int checkSide(int side) {
        if (side == Side.PLAYER || side == Side.OPPONENT) {
            return side;
        } else {
            throw new IllegalArgumentException("Invalid side: " + side);
        }
    }

    private static Image getArcherTowerImage(int side) {
        Image kingTowerImage = new Image(KingTower.class.getResourceAsStream(ARCHERTOWER_RELATIVE_PATH[side]));
        // adjusting tower image
        kingTowerImage = ImageUtils.enhanceOpacity(kingTowerImage);

        return kingTowerImage;
    }

}
