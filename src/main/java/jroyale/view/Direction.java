package jroyale.view;
/* package jroyale.view;

public class Direction {
    
    public static final int NUM_DIRECTIONS = 9;
    
    private static final double TOLERANCE = Math.PI/16;

    public static int fromAngle(double angleDirection) {
        if (angleDirection < -Math.PI/2) {
            angleDirection = -Math.PI - angleDirection;
        } else if (angleDirection > Math.PI/2) {
            angleDirection = +Math.PI - angleDirection;
        }

        angleDirection += Math.PI/2; // angle in [0, π]
        angleDirection /= Math.PI; // angle in [0, 1]
        angleDirection *= (NUM_DIRECTIONS - 1); // angle in [0, 8]

        return (NUM_DIRECTIONS - 1) - (int) Math.round(angleDirection);
    }

    public static boolean hasToFlip(double angleDirection) {
        return angleDirection < -Math.PI/2 - TOLERANCE || angleDirection > Math.PI/2 + TOLERANCE;
    }
}
 */

/* public class Direction {
    public static final int NUM_DIRECTIONS = 9;
    private static final double TOLERANCE = Math.PI / 16;
    private static final double HYSTERESIS = Math.PI / (NUM_DIRECTIONS * 2); 

    private int lastDirection = -1;

    public int fromAngle(double angleDirection) {
        int newDirection = computeDirection(angleDirection);

        if (lastDirection == -1) {
            lastDirection = newDirection;
            return newDirection;
        }

        // updates only if new value is sufficiently far from the previous 
        double lastAngle = directionToAngle(lastDirection);

        if (Math.abs(angleDirection - lastAngle) > HYSTERESIS) {
            lastDirection = newDirection;
        }

        return lastDirection;
    }

    private int computeDirection(double angleDirection) {
        if (angleDirection < -Math.PI / 2) {
            angleDirection = -Math.PI - angleDirection;
        } else if (angleDirection > Math.PI / 2) {
            angleDirection = +Math.PI - angleDirection;
        }
        angleDirection += Math.PI / 2;
        angleDirection /= Math.PI;
        angleDirection *= (NUM_DIRECTIONS - 1);
        return (NUM_DIRECTIONS - 1) - (int) Math.round(angleDirection);
    }

    private double directionToAngle(int direction) {
        // inverso di computeDirection
        double angle = (NUM_DIRECTIONS - 1 - direction) / (double)(NUM_DIRECTIONS - 1);
        angle *= Math.PI;
        angle -= Math.PI / 2;
        return angle;
    }

    public static boolean hasToFlip(double angleDirection) {
        return angleDirection < -Math.PI / 2 - TOLERANCE || angleDirection > Math.PI / 2 + TOLERANCE;
    }
} */

public class Direction {
    public static final int NUM_DIRECTIONS = 9;
    private static final double TOLERANCE = Math.PI / 16;
    private static final double HYSTERESIS = Math.PI / (NUM_DIRECTIONS * 2); 
    private static final int BUFFER_SIZE = 1;

    private int[] directionBuffer = new int[BUFFER_SIZE];
    private int bufferIndex = 0;
    private boolean bufferFilled = false;
    private int lastDirection = -1;

    public int fromAngle(double angleDirection) {
        // 1. calcola direzione raw
        int rawDirection = computeDirection(angleDirection);

        // 2. aggiorna buffer
        directionBuffer[bufferIndex] = rawDirection;
        bufferIndex = (bufferIndex + 1) % BUFFER_SIZE;
        if (bufferIndex == 0) bufferFilled = true;

        // 3. media delle direzioni
        int size = bufferFilled ? BUFFER_SIZE : bufferIndex;
        double avg = 0;
        for (int i = 0; i < size; i++) {
            avg += directionBuffer[i];
        }
        int smoothDirection = (int) Math.round(avg / size);

        // 4. histeresis: updates only if new value is sufficiently far from the previous 
        double lastAngle = directionToAngle(lastDirection);

        if (Math.abs(angleDirection - lastAngle) > HYSTERESIS) {
            lastDirection = smoothDirection;
        }

        return lastDirection;
    }

    private int computeDirection(double angleDirection) {
        if (angleDirection < -Math.PI / 2) {
            angleDirection = -Math.PI - angleDirection;
        } else if (angleDirection > Math.PI / 2) {
            angleDirection = +Math.PI - angleDirection;
        }
        angleDirection += Math.PI / 2;
        angleDirection /= Math.PI;
        angleDirection *= (NUM_DIRECTIONS - 1);
        return (NUM_DIRECTIONS - 1) - (int) Math.round(angleDirection);
    }

    private double directionToAngle(int direction) {
        // inverse of computeDirection
        double angle = (NUM_DIRECTIONS - 1 - direction) / (double)(NUM_DIRECTIONS - 1);
        angle *= Math.PI;
        angle -= Math.PI / 2;
        return angle;
    }

    public static boolean hasToFlip(double angleDirection) {
        return angleDirection < -Math.PI / 2 - TOLERANCE || angleDirection > Math.PI / 2 + TOLERANCE;
    }
}