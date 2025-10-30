package jroyale.model;

import javafx.geometry.Point2D;

abstract class Entity {

    // bridges position
    static final Point2D LEFT_BRIDGE_START_POS = new Point2D(3.5,17);
    static final Point2D RIGHT_BRIDGE_START_POS = new Point2D(14.5,17);
    static final Point2D LEFT_BRIDGE_END_POS = new Point2D(3.5,15);
    static final Point2D RIGHT_BRIDGE_END_POS = new Point2D(14.5,15);

    // player towers position
    static final Point2D PLAYER_KING_TOWER_CENTRE = new Point2D(9, 29);
    static final Point2D PLAYER_LEFT_TOWER_CENTRE = new Point2D(3.5, 25.5);
    static final Point2D PLAYER_RIGHT_TOWER_CENTRE = new Point2D(14.5, 25.5);

    // opponent towers position
    static final Point2D OPPONENT_KING_TOWER_CENTRE = new Point2D(9, 3);
    static final Point2D OPPONENT_LEFT_TOWER_CENTRE = new Point2D(3.5, 6.5);
    static final Point2D OPPONENT_RIGHT_TOWER_CENTRE = new Point2D(14.5, 6.5);

    private double x, y;
    private double width, height;

    Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
