package jroyale.model; 

import javafx.scene.image.Image;

public class Troop { // TODO: farlo abstract
    private String name;
    private Image pic;
    private static final double DEFAULT_SPEED = 1; //TODO: determinare la default speed
    private double posX, posY;

    public Troop(String name, Image pic, double x, double y) {
        this.name = name;
        this.pic = pic;
        this.posX = x;
        this.posY = y;
    }

    public Troop(String name, Image pic, int n, int m) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, pic, m + 0.5, n + 0.5);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void shiftPosX(double shiftX) {
        posX += shiftX;
    }

    public void shiftPosY(double shiftY) {
        posY += shiftY;
    }
}
