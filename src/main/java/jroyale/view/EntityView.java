package jroyale.view;

import javafx.scene.image.Image;
import jroyale.shared.Enums.EntityType;
import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;

public abstract class EntityView {
    
    public abstract EntityType getType();

    public abstract void render(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side);

    // methods for base index of a side based on the state of the troop
    protected abstract int getPlayerIdleBaseIndex();

    protected abstract int getOpponentIdleBaseIndex();

    protected abstract int getPlayerMoveBaseIndex();

    protected abstract int getOpponentMoveBaseIndex();

    protected abstract int getPlayerAttackBaseIndex();

    protected abstract int getOpponentAttackBaseIndex();

    public abstract int getNumFramesPerDirection(State state);

    // methods for file path
    protected abstract String getHeaderNamePath();

    protected abstract String getTroopPath();

    protected abstract int getNumIndexDigits();

    // methods for image transforming:
    protected abstract Image transformImage(Image image);
}
