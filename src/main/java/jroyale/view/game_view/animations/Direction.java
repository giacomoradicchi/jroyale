package jroyale.view.game_view.animations;

public class Direction {
    
    // logic constants
    public static final int NUM_DIRECTIONS = 9;
    private static final int BUFFER_SIZE = 1;
    private static final int INITIAL_DIRECTION = -1;
    private static final int START_INDEX = 0;
    
    // trigonometric constants
    private static final double PI_HALF = Math.PI / 2.0;
    private static final double TOLERANCE = Math.PI / 16.0;
    private static final double HYSTERESIS = Math.PI / (NUM_DIRECTIONS * 2.0);
    private static final double DENOMINATOR_ADJUSTMENT = 1.0;

    private int[] directionBuffer = new int[BUFFER_SIZE];
    private int bufferIndex = START_INDEX;
    private boolean bufferFilled = false;
    private int lastDirection = INITIAL_DIRECTION;

    /**
     * returns the appropriate sprite direction index based on the input angle.
     * uses a buffer and hysteresis to avoid flickering between adjacent directions.
     */
    public int fromAngle(double angleDirection) {
        // 1. calculate raw direction
        int rawDirection = computeDirection(angleDirection);

        // 2. update circular buffer
        directionBuffer[bufferIndex] = rawDirection;
        bufferIndex = (bufferIndex + 1) % BUFFER_SIZE;
        if (bufferIndex == START_INDEX) {
            bufferFilled = true;
        }

        // 3. average directions for smoothing
        int size = bufferFilled ? BUFFER_SIZE : bufferIndex;
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += directionBuffer[i];
        }
        int smoothDirection = (int) Math.round(sum / size);

        // 4. hysteresis: update only if the new value is sufficiently far from the previous one
        double lastAngle = directionToAngle(lastDirection);

        if (Math.abs(angleDirection - lastAngle) > HYSTERESIS) {
            lastDirection = smoothDirection;
        }

        return lastDirection;
    }

    private int computeDirection(double angleDirection) {
        double normalizedAngle = angleDirection;

        // mirror the angle if it's in the left hemisphere (to be handled by flipping)
        if (normalizedAngle < -PI_HALF) {
            normalizedAngle = -Math.PI - normalizedAngle;
        } else if (normalizedAngle > PI_HALF) {
            normalizedAngle = Math.PI - normalizedAngle;
        }

        normalizedAngle += PI_HALF; // shift to [0, π]
        normalizedAngle /= Math.PI; // normalize to [0, 1]
        normalizedAngle *= (NUM_DIRECTIONS - DENOMINATOR_ADJUSTMENT); // scale to [0, 8]

        return (int) ((NUM_DIRECTIONS - DENOMINATOR_ADJUSTMENT) - Math.round(normalizedAngle));
    }

    private double directionToAngle(int direction) {
        // inverse of computeDirection
        double angle = (NUM_DIRECTIONS - DENOMINATOR_ADJUSTMENT - direction) / (double)(NUM_DIRECTIONS - DENOMINATOR_ADJUSTMENT);
        angle *= Math.PI;
        angle -= PI_HALF;
        return angle;
    }

    /**
     * determines if the sprite needs to be horizontally flipped based on the angle.
     */
    public static boolean hasToFlip(double angleDirection) {
        return angleDirection < -PI_HALF - TOLERANCE || angleDirection > PI_HALF + TOLERANCE;
    }
}