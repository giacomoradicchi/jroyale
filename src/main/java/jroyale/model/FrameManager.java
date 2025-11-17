package jroyale.model;

public class FrameManager {

    public static final int FPS_ANIMATION = 12;
    private static final long ELAPSED_BETWEEN_FRAMES = getElapsedBetweenFrames();

    private long accumulator; // it will increase for each frame by elapsed
    private Entity entity; // entity on which it will be changed currentFrame
    

    public FrameManager(Entity e) {
        this.entity = e;
    }

    public void updateFrame(long elapsed) {
        // assuming elapsed > 0
        accumulator += elapsed;

        while (shouldUpdateFrame()) {
            entity.goToNextFrame();
            resetAccumulator();
        }
    }

    private boolean shouldUpdateFrame() {
        return accumulator >= ELAPSED_BETWEEN_FRAMES;
    }

    private static long getElapsedBetweenFrames() {
        // assuming FPS Animation won't be 0
        return 1_000_000_000L / FPS_ANIMATION;
    }

    private void resetAccumulator() {
        accumulator -= ELAPSED_BETWEEN_FRAMES;
    }
}
