package jroyale.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class DragPlacementPreview {

    private static final double ALPHA_FILL = 0.5;
    private static final double DEFAULT_LINEWIDTH = 4;
    private static final double CORNER_ROUNDNESS = 0.5;
    private static final Color FILL_COLOR = Color.TEAL;
    private static final Color STROKE_COLOR = Color.WHITE;
    private static final long TOTAL_ANIMATION_PERIOD = (long) (2 * Math.pow(10, 9)); // nanosec
    private static final double MAX_RANGE_SCALE = 0.125; 
    private static final double DEFAULT_SCALE_ANIMATION = 0.8;

    private static long t0 = 0;
    
    static void render(GraphicsContext gc, double centreX, double centreY, double dx, double dy, double globalScale, long now) {
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

        gc.restore();
    }

    private static double getScaleAnimation(long now) {
        double scale = DEFAULT_SCALE_ANIMATION;
        long elapsed = (now - t0) % TOTAL_ANIMATION_PERIOD;

        if (elapsed < TOTAL_ANIMATION_PERIOD/2) {
            scale += elapsed/(TOTAL_ANIMATION_PERIOD/2.0) * (MAX_RANGE_SCALE);
        } else {
            scale += MAX_RANGE_SCALE;
            scale -= (elapsed-TOTAL_ANIMATION_PERIOD/2)/(TOTAL_ANIMATION_PERIOD/2.0) * (MAX_RANGE_SCALE);
        }

        return scale;
    }

    static void resetAnimation() {
        t0 = 0;
    }
}
