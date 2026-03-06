package jroyale.view;

import javafx.scene.paint.Color;

public class DragPlacementPreview {

    private static DragPlacementPreview instance = null;

    private static final double ALPHA_FILL = 0.5;
    private static final double LINE_WIDTH = 4;
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

        // fill
        View.getInstance().fillWorldRoundedRect(
            centreX, 
            centreY, 
            View.getInstance().getDx() * scaleAnimation, 
            View.getInstance().getDy() * scaleAnimation, 
            View.getInstance().getDx() * CORNER_ROUNDNESS * scaleAnimation, 
            View.getInstance().getDy() * CORNER_ROUNDNESS * scaleAnimation,
            ALPHA_FILL, 
            FILL_COLOR
        );

        // outline
        View.getInstance().strokeWorldRoundedRect(
            centreX, 
            centreY, 
            View.getInstance().getDx() * scaleAnimation, 
            View.getInstance().getDy() * scaleAnimation, 
            View.getInstance().getDx() * CORNER_ROUNDNESS * scaleAnimation, 
            View.getInstance().getDy() * CORNER_ROUNDNESS * scaleAnimation,
            LINE_WIDTH,
            1.0, // fully visible 
            STROKE_COLOR
        );

        /* 
        gc.setGlobalAlpha(1); // back to full opacity
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(DEFAULT_LINEWIDTH * globalScale);
        gc.strokeRoundRect(
            centreX - dx/2 * scaleAnimation, 
            centreY - dy/2 * scaleAnimation, 
            dx * scaleAnimation, 
            dy * scaleAnimation, 
            dx * CORNER_ROUNDNESS * scaleAnimation, 
            dy * CORNER_ROUNDNESS * scaleAnimation
        );
        
        gc.save();

        if (t0 == 0) t0 = now;
        
        double scaleAnimation = getScaleAnimation(now);

        // fill
        gc.setGlobalAlpha(ALPHA_FILL);
        gc.setFill(FILL_COLOR);
        gc.fillRoundRect(
            centreX - dx/2 * scaleAnimation, 
            centreY - dy/2 * scaleAnimation, 
            dx * scaleAnimation, 
            dy * scaleAnimation, 
            dx * CORNER_ROUNDNESS * scaleAnimation, 
            dy * CORNER_ROUNDNESS * scaleAnimation
        );

        // outline
        gc.setGlobalAlpha(1); // back to full opacity
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(DEFAULT_LINEWIDTH * globalScale);
        gc.strokeRoundRect(
            centreX - dx/2 * scaleAnimation, 
            centreY - dy/2 * scaleAnimation, 
            dx * scaleAnimation, 
            dy * scaleAnimation, 
            dx * CORNER_ROUNDNESS * scaleAnimation, 
            dy * CORNER_ROUNDNESS * scaleAnimation
        );

        gc.restore(); */
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
