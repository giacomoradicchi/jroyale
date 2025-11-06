package jroyale.utils;

public class Point {
    private double x;
    private double y;

    public Point() {} // empty constructor

    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    public void setPoint(Point p){
        this.x=p.x;
        this.y=p.y;
    }

    public Point add(Point p){
        return new Point(this.x + p.x, this.y + p.y);
    }

    public Point subtract(Point p){
        return new Point(this.x - p.x, this.y - p.y);
    }

    public Point multiply(double scalar){
        return new Point(this.x * scalar, this.y * scalar);
    }

    public double distance(Point p){
        double dx = this.x - p.x;
        double dy = this.y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Point normalize(){
        double magnitude = Math.sqrt(x * x + y * y);
        if (magnitude == 0) {
            return new Point(0, 0);
        }
        return new Point(x / magnitude, y / magnitude);
    }

    public Point interpolate(Point p, double t){
        double newX = this.x + (p.x - this.x) * t;
        double newY = this.y + (p.y - this.y) * t;
        return new Point(newX, newY);
    }

    public double magnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public String toString() {
        return "[x: " + x + ", y: " + y + "]";
    }

}
