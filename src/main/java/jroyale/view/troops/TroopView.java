package jroyale.view.troops;

import javafx.scene.canvas.GraphicsContext;
import jroyale.view.View.TroopType;

public abstract class TroopView {
    
    protected static final String TROOPS_PATH_RELATIVE_TO_RESOURCE = "/jroyale/images/troops/";

    public abstract void render(GraphicsContext gc, double centreX, double centreY, double angleDirection, int side, double dx, double dy);
    
}
