package jroyale.model; 


import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Troop { // TODO: farlo abstract
    private String name;
    private Image pic;

    private static Map<String, Integer> SPEEDS = new HashMap<>() {
        {   // category - associated speed [tiles/minutes]
            // based on: https://clashroyale.fandom.com/wiki/Cards
            put("Very Slow", 30);
            put("Slow", 45);
            put("Medium", 60);
            put("Fast", 90);
            put("Very Fast", 120);
        }
    };

    private static final Point2D LEFT_BRIDGE_POS = new Point2D(3.5,17);
    private static final Point2D RIGHT_BRIDGE_POS = new Point2D(14.5,17);


    private Point2D position;
    private Point2D target; // TODO: necessary to calculate speed vector
    private Point2D speed;

    public Troop(String name, Image pic, double x, double y) {
        this.name = name;
        this.pic = pic;
        this.position = new Point2D(x, y);
        this.speed = new Point2D(0, 0);
        if (x < Model.MAP_COLS / 2) { // if is on the left side
            this.target = LEFT_BRIDGE_POS;
        } else { // if is on the right side
            this.target = RIGHT_BRIDGE_POS; 
        }
        updateSpeed();
    }

    public Troop(String name, Image pic, int n, int m) {
        // The constructor puts the troop in the centre of the cell (n, m).
        // In order to achieve this, it's necessary to shift the posX and posY by +0.5,
        // which is half a cell. In this way, the placing won't be in the top left corner; 
        // instead, it will be in the cell's centre.

        this(name, pic, m + 0.5, n + 0.5);
    }

    public double getPosX() {
        return position.getX();
    }

    public double getPosY() {
        return position.getY();
    }

    public void moove() {
        updateSpeed();
        shiftPosition(speed);
    }

    // private methods

    private void shiftPosition(Point2D shift) {
        position = position.add(shift);
    }

    private void updateSpeed() {
        // TODO
    }
    
}
