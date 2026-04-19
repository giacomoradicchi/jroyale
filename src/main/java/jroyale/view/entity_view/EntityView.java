package jroyale.view.entity_view;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;
import jroyale.view.IView;
import jroyale.view.View;

public abstract class EntityView {

    // Health bar dimensions relative to tile size
    private static final double HEALTH_BAR_WIDTH_MULTIPLIER = 2.5;
    private static final double HEALTH_BAR_HEIGHT_MULTIPLIER = 0.5;
    private static final double HEALTH_BAR_VERTICAL_OFFSET_MULTIPLIER = 1.5;

    // Corner arc radii (0 = sharp corners)
    private static final double ARC_X = 0;
    private static final double ARC_Y = 0;

    // Stroke thickness
    private static final double BACKGROUND_ALPHA = 0.75;
    private static final double BORDER_STROKE_LINE_WIDTH = 2;
    private static final double ALPHA = 1;

    // Player health bar colors
    private static final Color PLAYER_STROKE_COLOR = Color.rgb(20, 20, 150);
    private static final Color PLAYER_FILL_LIGHT = Color.rgb(100, 100, 255);
    private static final Color PLAYER_FILL_DARK = Color.rgb(50, 50, 150);

    // Enemy health bar colors
    private static final Color ENEMY_STROKE_COLOR = Color.rgb(150, 20, 20);
    private static final Color ENEMY_FILL_LIGHT = Color.rgb(255, 100, 100);
    private static final Color ENEMY_FILL_DARK = Color.rgb(150, 50, 50);

    
    public abstract EntityType getType();

    public void render(double centreX, double centreY, int currentHealth, int maxHealth, double shadowRadius, double angleDirection, int currentFrame, State state, Side side) {
        renderShadow(centreX, centreY, shadowRadius);
        renderEntity(centreX, centreY, angleDirection, currentFrame, state, side); 

        if (currentHealth < maxHealth)
            renderHealth(currentHealth, maxHealth, side, centreX, centreY - getHeightInPixels() / 2);
    }

    private void renderHealth(int currentHealth, int maxHealth, Side side, double centerX, double centerY) {
        
        IView view = View.getInstance();

        double rectWidth  = view.getDx() * HEALTH_BAR_WIDTH_MULTIPLIER;
        double rectHeight = view.getDy() * HEALTH_BAR_HEIGHT_MULTIPLIER;

        // Offset the bar above the entity's center
        double shiftY = -view.getDy() * HEALTH_BAR_VERTICAL_OFFSET_MULTIPLIER;

        Color cFill, cStroke;
        Color cFillDark;
        if (side == Side.PLAYER) {
            cStroke   = PLAYER_STROKE_COLOR;
            cFill     = PLAYER_FILL_LIGHT;
            cFillDark = PLAYER_FILL_DARK;
        } else {
            cStroke   = ENEMY_STROKE_COLOR;
            cFill     = ENEMY_FILL_LIGHT;
            cFillDark = ENEMY_FILL_DARK;
        }

        // Draw the empty (background) health bar
        view.fillWorldRoundedRect(centerX, centerY + shiftY, rectWidth, rectHeight,
                ARC_X, ARC_Y, BACKGROUND_ALPHA, cFill);

        // Calculate the filled portion width based on current health percentage
        double percentage   = (double) currentHealth / maxHealth;
        double healthWidth  = rectWidth * percentage;

        // Align the filled bar to the left edge of the background bar
        double healthBarCenterX = centerX - rectWidth / 2 + healthWidth / 2;

        // Draw the filled (current health) portion of the bar
        view.fillWorldRoundedRect(healthBarCenterX, centerY + shiftY, healthWidth, rectHeight,
                ARC_X, ARC_Y, ALPHA, cFillDark);

        // Draw the border around the full health bar
        view.strokeWorldRoundedRect(centerX, centerY + shiftY, rectWidth, rectHeight,
                rectHeight / 2, rectHeight / 2, BORDER_STROKE_LINE_WIDTH, ALPHA, cStroke);
    }

    private void renderShadow(double centreX, double centreY, double shadowRadius) {
        View.getInstance().renderWorldShadow(centreX, centreY, shadowRadius);
    }

    protected double getImageScale() {
        /* 
        hr = sprites height in pixels   (excluding outside transparent pixels)  -> SPRITES_HEIGHT
        h  = sprites height in pixels   (including outside transparent pixels)  -> image.getHeight()
        dy = tiles height in pixels                                             -> View.getInstance().getDy()
        ht = troop height in tiles                                              -> HEIGHT_IN_TILES

        formula:
        hr * scale = ht * dy (the scale factor must satisfy this formula, so the height will be troop height in tiles * tiles height in pixels)
        -> scale = (ht * dy) / hr
         */

        return getHeightInTiles() * View.getInstance().getDy() / getSpritesHeight();
    }

    private double getHeightInPixels() {    // height in pixel of troop (based on map proportion)
        return getHeightInTiles() * View.getInstance().getDy();
    }

    // abstract methods

    public abstract Image getSpellIcon();

    protected abstract void renderEntity(double centreX, double centreY, double angleDirection, int currentFrame, State state, Side side);

    protected abstract double getSpritesHeight(); // height in pixel of the original sprites (unscaled) excluding transparent pixel not in bounds 

    protected abstract double getHeightInTiles();
}
