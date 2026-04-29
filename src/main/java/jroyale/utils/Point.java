package jroyale.utils;

public class Point {
    
    // mathematical and default constants
    private static final double DEFAULT_COORDINATE = 0.0;
    private static final double ZERO_MAGNITUDE = 0.0;
    private static final double DEGREES_TO_RADIANS_FACTOR = Math.PI / 180.0;
    private static final int POWER_OF_TWO = 2;

    private double x;
    private double y;

    public Point() {
        this.x = DEFAULT_COORDINATE;
        this.y = DEFAULT_COORDINATE;
    }

    public Point(double x, double y){
        this.x = x;
        this.y = y;
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

    public Point setPoint(double x, double y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Point setPoint(Point p){
        this.x = p.x;
        this.y = p.y;
        return this;
    }

    public boolean isZeroVector() {
        return x == DEFAULT_COORDINATE && y == DEFAULT_COORDINATE;
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
        return Math.sqrt(Math.pow(x1 - x2, POWER_OF_TWO) + Math.pow(y1 - y2, POWER_OF_TWO));
    }

    public Point normalize(){
        double magnitude = magnitude();
        if (magnitude == ZERO_MAGNITUDE) {
            return new Point(DEFAULT_COORDINATE, DEFAULT_COORDINATE);
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
        double currentMagnitude = magnitude();
        double currentAngle = Math.atan2(getY(), getX());
        currentAngle += degrees * DEGREES_TO_RADIANS_FACTOR;

        x = currentMagnitude * Math.cos(currentAngle);
        y = currentMagnitude * Math.sin(currentAngle);
        return this;
    } 

    public double dotProduct(Point vector) {
        return dotProduct(vector.getX(), vector.getY());
    }

    public double dotProduct(double x, double y) {
        return getX() * x + getY() * y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point)) return false;
        Point p = (Point) obj;
        return Double.compare(p.getX(), getX()) == 0 && Double.compare(p.getY(), getY()) == 0;
    }

    @Override
    public String toString() {
        return "[x: " + x + ", y: " + y + "]";
    }

}