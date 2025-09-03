package jroyale.model;

abstract class Entity {
    private double x, y;
    private double width, height;

    Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
