package jroyale.view.troops;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class TroopView {
    
    protected static final String TROOPS_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/troops/";

    protected final List<Image> spriteBuffer;

    protected TroopView() {
        spriteBuffer = getSpriteBuffer();
    }

    // abstract methods

    public abstract void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int currentFrame, int side, double dx, double dy);
    
    protected abstract List<Image> getSpriteBuffer();
}
