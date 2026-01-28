package jroyale.view;

public class Direction {
    
    public static final int NUM_DIRECTIONS = 9;
    
    private static final double TOLERANCE = Math.PI/8;

    public static int fromAngle(double angleDirection) {
        if (angleDirection < -Math.PI/2) {
            angleDirection = -Math.PI - angleDirection;
        } else if (angleDirection > Math.PI/2) {
            angleDirection = +Math.PI - angleDirection;
        }

        angleDirection += Math.PI/2; // angle in [0, π]
        angleDirection /= Math.PI; // angle in [0, 1]
        angleDirection *= (NUM_DIRECTIONS - 1); // angle in [0, 8]

        return (NUM_DIRECTIONS - 1) - (int) Math.floor(angleDirection);
    }

    public static boolean hasToFlip(double angleDirection) {
        return angleDirection < -Math.PI/2 - TOLERANCE || angleDirection > Math.PI/2 + TOLERANCE;
    }
}
