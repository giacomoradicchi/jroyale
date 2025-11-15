package jroyale.model;

public class FrameManager {

    private long accumulator; // it will increase for each frame by elapsed
    private Entity entity; // entity on which it will be changed currentFrame
    private final long ELAPSED_BETWEEN_FRAMES; // it won't change once created

    public FrameManager(Entity e) {
        this.entity = e;
        ELAPSED_BETWEEN_FRAMES = getElapsedBetweenFrames();
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

    private long getElapsedBetweenFrames() {
        // assuming FPS Animation won't be 0
        return 1_000_000_000L / (long) entity.getFPSAnimation();
    }

    private void resetAccumulator() {
        accumulator -= ELAPSED_BETWEEN_FRAMES;
    }
}
