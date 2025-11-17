package jroyale.utils;

public class Point {
    private double x;
    private double y;

    public Point() {} // empty constructor

    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }

    public Point(Point p){
        this(p.getX(), p.getY());
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

    public boolean isZeroVector() {
        return x == 0 && y == 0;
    }

    public Point add(Point p){
        x += p.getX();
        y += p.getY();
        return this;
    }

    public Point addX(double shiftX) {
        x += shiftX;
        return this;
    }

    public Point addY(double shiftY) {
        y += shiftY;
        return this;
    }

    public Point subtract(Point p){
        x -= p.getX();
        y -= p.getY();
        return this;
    }

    public Point multiply(double scalar){
        x *= scalar;
        y *= scalar;
        return this;
    }


    public double distance(Point p){
        return distance(p.getX(), p.getY(), getX(), getY());
    }

    public double distance(double x, double y){
        return distance(getX(), getY(), x, y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public Point normalize(){
        double magnitude = Math.sqrt(x * x + y * y);
        if (magnitude == 0) {
            return new Point(0, 0);
        }
        x /= magnitude;
        y /= magnitude;
        return this;
    }

    public Point interpolate(Point p, double t){
        this.x = getX() + (p.getX() - getX()) * t;
        this.y = getY() + (p.getY() - getY()) * t;
        return this;
    }

    public double magnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    // rotate through point (0,0)
    public Point rotate(double degrees) {
        double magnitude = magnitude();
        double angle = Math.atan2(getY(), getX());
        angle += degrees * Math.PI / 180.0;

        x = magnitude * Math.cos(angle);
        y = magnitude * Math.sin(angle);
        return this;
    } 

    public double dotProduct(Point vector) {
        return dotProduct(vector.getX(), vector.getY());
    }

    public double dotProduct(double x, double y) {
        return getX() * x + getY() * y;
    }

    public boolean equals(Point p) {
        return (getX() == p.getX()) && (getY() == p.getY());
    }

    public String toString() {
        return "[x: " + x + ", y: " + y + "]";
    }

}
