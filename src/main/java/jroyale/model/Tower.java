package jroyale.model;

class Tower extends Entity {
    
    static double WIDTH; // in map-unit
    static double HEIGHT; // in map-unit

    Tower(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
    }
}
