package jroyale.view.troops;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class TroopView {
    
    protected static final String TROOPS_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/troops/";

    protected final List<Image> spriteBuffer;

    protected static final int NUM_DIRECTIONS = 9;

    protected TroopView() {
        spriteBuffer = getSpriteBuffer();
    }

    protected byte isFlippedOnX(double angleDirection) {
        if (angleDirection < -Math.PI/2  - Math.PI/8 || angleDirection > Math.PI/2 + Math.PI/8) {
            return 1;
        }
        return 0;
    }

    protected static int getOffsetDirection(double angleDirection) {
        
        if (angleDirection < -Math.PI/2) {
            angleDirection = -Math.PI - angleDirection;
        } else if (angleDirection > Math.PI/2) {
            angleDirection = +Math.PI - angleDirection;
        }

        angleDirection += Math.PI/2; // angle in [0, π]
        angleDirection /= Math.PI; // angle in [0, 1]
        angleDirection *= (NUM_DIRECTIONS - 1); // angle in [0, 8]

        return (NUM_DIRECTIONS - 1) - (int) Math.floor(angleDirection);
    }

    // abstract methods

    public abstract void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, byte state, byte side, double globalScale);
    
    protected abstract List<Image> getSpriteBuffer();
}
