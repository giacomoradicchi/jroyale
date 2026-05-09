package jroyale.view.game_view.ui_and_ux;

import javafx.scene.paint.Color;
import jroyale.view.IMainGUI;
import jroyale.view.game_view.IGameView;

public class DragPlacementPreview {

    private static DragPlacementPreview instance = null;

    private static final double ALPHA_FILL = 0.5;
    private static final double ALPHA_STROKE = 1.0;
    private static final double NORMALIZED_LINE_WIDTH = 0.009;
    private static final double CORNER_ROUNDNESS = 0.5;
    private static final Color FILL_COLOR = Color.TEAL;
    private static final Color STROKE_COLOR = Color.WHITE;
    private static final long TOTAL_ANIMATION_PERIOD = (long) (2 * Math.pow(10, 9)); // nanosec
    private static final double MAX_RANGE_SCALE = 0.125; 
    private static final double DEFAULT_SCALE_ANIMATION = 0.8;

    private long t0 = 0;
    private long tLast = 0;
    private double scaleAnimation = 1.0;
    private boolean shouldUpdate = false;

    private DragPlacementPreview() {}
    
    public void render(double centreX, double centreY) {

        IGameView view = IGameView.getInstance();
        IMainGUI gui = view.getGUI();
        
        // fill
        gui.fillWorldRoundedRect(
            centreX, 
            centreY, 
            view.getDx() * scaleAnimation, 
            view.getDy() * scaleAnimation, 
            view.getDx() * CORNER_ROUNDNESS * scaleAnimation, 
            view.getDy() * CORNER_ROUNDNESS * scaleAnimation,
            ALPHA_FILL, 
            FILL_COLOR
        );

        // outline
        gui.strokeWorldRoundedRect(
            centreX, 
            centreY, 
            view.getDx() * scaleAnimation, 
            view.getDy() * scaleAnimation, 
            view.getDx() * CORNER_ROUNDNESS * scaleAnimation, 
            view.getDy() * CORNER_ROUNDNESS * scaleAnimation,
            NORMALIZED_LINE_WIDTH * gui.getCanvasWidth(),
            ALPHA_STROKE,
            STROKE_COLOR
        );
    }

    private double getScaleAnimation() {
        double scale = DEFAULT_SCALE_ANIMATION;
        long elapsed = (tLast - t0) % TOTAL_ANIMATION_PERIOD;

        if (elapsed < TOTAL_ANIMATION_PERIOD/2) {
            scale += elapsed/(TOTAL_ANIMATION_PERIOD/2.0) * (MAX_RANGE_SCALE);
        } else {
            scale += MAX_RANGE_SCALE;
            scale -= (elapsed-TOTAL_ANIMATION_PERIOD/2)/(TOTAL_ANIMATION_PERIOD/2.0) * (MAX_RANGE_SCALE);
        }

        return scale;
    }

    public void startAnimation() {
        shouldUpdate = true;
    }

    public void update(long now) {
        if (shouldUpdate) {
            tLast = now;
            scaleAnimation = getScaleAnimation();
        }
    }

    public void stopAnimation() {
        shouldUpdate = false;
        resetAnimation();
    }

    private void resetAnimation() {
        t0 = 0;
        tLast = 0;
    }

    // static methods

    public static DragPlacementPreview getInstance() {
        if (instance == null) {
            instance = new DragPlacementPreview();
        }

        return instance;
    }
}
