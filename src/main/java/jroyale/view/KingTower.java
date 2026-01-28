package jroyale.view;

import javafx.scene.image.Image;
import jroyale.shared.Enums.Side;
import jroyale.utils.ImageUtils;

public class KingTower extends Tower{
    private final static double SCALE = 0.4;
    private final static double PLAYER_X_OFFSET = 1;
    private final static double OPPONENT_X_OFFSET = -1; 
    private final static double Y_OFFSET = 6;
    private final static String[] KINGTOWER_RELATIVE_PATH = new String[] {
        "/jroyale/images/towers/player/king_tower.png", // player
        "/jroyale/images/towers/opponent/king_tower.png" // opponent
    };


    KingTower(Side side) {
        super(getKingTowerImage(checkSide(side)), checkSide(side), SCALE, getXOffset(checkSide(side)), Y_OFFSET);
    }

    private static Side checkSide(Side side) {
        if (side == Side.PLAYER || side == Side.OPPONENT) {
            return side;
        } else {
            throw new IllegalArgumentException("Invalid side: " + side);
        }
    }

    private static Image getKingTowerImage(Side side) {
        Image kingTowerImage = new Image(KingTower.class.getResourceAsStream(KINGTOWER_RELATIVE_PATH[side.ordinal()]));
        // adjusting tower image
        kingTowerImage = ImageUtils.enhanceOpacity(kingTowerImage);

        return kingTowerImage;
    }

    private static double getXOffset(Side side) {
        return (side == Side.PLAYER) ? PLAYER_X_OFFSET : OPPONENT_X_OFFSET;
    }

}
