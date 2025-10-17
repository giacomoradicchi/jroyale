package jroyale.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jroyale.utils.ImageUtils;

public class KingTower {
    final static int PLAYER = 0;
    final static int OPPONENT = 1;

    private final static double SCALE = 0.35;
    private final static String[] KINGTOWER_RELATIVE_PATH = new String[] {
        "/jroyale/images/towers/player/king_tower.png", // player
        "/jroyale/images/towers/opponent/king_tower.png" // opponent
    };

    private int side; // player = 0, opponent = 1 
    private Image kingTowerImage;

    KingTower(int side) {
        if (side == PLAYER || side == OPPONENT) {
            this.side = side;
        } else {
            throw new IllegalArgumentException("Invalid side: " + side);
        }

        this.kingTowerImage = new Image(getClass().getResourceAsStream(KINGTOWER_RELATIVE_PATH[this.side]));

        // adjusting tower image
        this.kingTowerImage = ImageUtils.enhanceOpacity(kingTowerImage);
    }

    void drawKingTower(GraphicsContext gc, double centreGraphicX, double centreGraphicY, double globalScale) {
        gc.drawImage(
            kingTowerImage, 
            centreGraphicX - kingTowerImage.getWidth() * SCALE * globalScale / 2, 
            centreGraphicY - kingTowerImage.getHeight() * SCALE * globalScale / 2,
            kingTowerImage.getWidth() * SCALE * globalScale,
            kingTowerImage.getHeight() * SCALE * globalScale
        );
    }

}
