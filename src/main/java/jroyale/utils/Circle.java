package jroyale.utils;

import javafx.scene.shape.Rectangle;

public class Circle {
    private Point center;
    private double radius;

    public Circle() {
        this.center = new Point();
    } 

    public Circle(double centerX, double centerY, double radius) {
        this.center = new Point(centerX, centerY);
    }

    public boolean collides(Circle c) {
        return this.center.distance(c.center) < (this.radius + c.radius);
    }

    
    // getters

    public double getCenterX() {
        return center.getX();
    }

    public double getCenterY() {
        return center.getY();
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        return radius * 2;
    }

    public double getMinXBounds() {
        return center.getX() - radius;
    }

    public double getMinYBounds() {
        return center.getY() - radius;
    }

    public double getWidthBounds() {
        return getDiameter();
    }

    public double getHeightBounds() {
        return getDiameter();
    }


    // setters

    public void setCenterX(double centerX) {
        center.setX(centerX);
    }

    public void setCenterY(double centerY) {
        center.setY(centerY);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
