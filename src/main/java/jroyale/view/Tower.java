package jroyale.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Tower {

    private final double SCALE;
    private final double X_OFFSET;
    private final double Y_OFFSET;

    private int side; // player = 0, opponent = 1 
    private Image towerImage;

    Tower(Image towerImage, int side, double SCALE, double X_OFFSET, double Y_OFFSET) {
        this.towerImage = towerImage;
        this.side = side;
        this.SCALE = SCALE;
        this.X_OFFSET = X_OFFSET;
        this.Y_OFFSET = Y_OFFSET;
    }

    void drawTower(GraphicsContext gc, double centreX, double centreY, double globalScale) {
        gc.drawImage(
            towerImage, 
            centreX - towerImage.getWidth() * SCALE * globalScale / 2 + X_OFFSET * globalScale, 
            centreY - towerImage.getHeight() * SCALE * globalScale / 2 + Y_OFFSET * globalScale,
            towerImage.getWidth() * SCALE * globalScale,
            towerImage.getHeight() * SCALE * globalScale
        );
    }

    int getSide() {
        return side;
    }

}
