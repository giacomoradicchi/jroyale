package jroyale.view.entity_view;


import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public abstract class EntityView {
    
    public abstract EntityType getType();

    public void render(double centreX, double centreY, int currentHealth, int maxHealth, double angleDirection, int currentFrame, State state, Side side) {
        renderEntity(centreX, centreY, angleDirection, currentFrame, state, side);
        renderHealth(currentHealth, maxHealth, side, centreX, centreY - getSpritesHeight() / 2);
    }

    private void renderHealth(int currentHealth, int maxHealth, Side side, double centerX, double centerY) {
        // TODO
    }

    // abstract methods

    protected abstract void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side);

    protected abstract double getSpritesHeight();
}
