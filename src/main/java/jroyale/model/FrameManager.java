package jroyale.model;

public class FrameManager {

    private final long ELAPSED_BETWEEN_FRAMES;

    private long accumulator; // it will increase for each frame by elapsed
    private Entity entity; // entity on which it will be changed currentFrame
    

    public FrameManager(Entity e) {
        this.entity = e;
        ELAPSED_BETWEEN_FRAMES = getElapsedBetweenFrames(e.getFPSAnimation());
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

    private static long getElapsedBetweenFrames(int fpsAnimation) {
        // assuming FPS Animation won't be 0
        return 1_000_000_000L / fpsAnimation;
    }

    private void resetAccumulator() {
        accumulator -= ELAPSED_BETWEEN_FRAMES;
    }
}
