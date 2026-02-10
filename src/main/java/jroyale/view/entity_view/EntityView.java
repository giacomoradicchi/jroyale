package jroyale.view.entity_view;


import javafx.scene.image.Image;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class EntityView {
    
    public abstract EntityType getType();

    public abstract void render(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side);
}
