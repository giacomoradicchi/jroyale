package jroyale.model; 

import javafx.scene.image.Image;

abstract class Troop {
    private String name;
    private Image pic;
    private static final double DEFAULT_SPEED = 1; //TODO: determinare la default speed
    private double posX, posY;

    Troop(String name, Image pic, double x, double y) {
        this.name = name;
        this.pic = pic;
        this.posX = x;
        this.posY = y;
    }
}
