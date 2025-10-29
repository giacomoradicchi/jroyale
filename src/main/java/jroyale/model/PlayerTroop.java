package jroyale.model;


import java.util.LinkedList;
import java.util.Iterator;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import jroyale.shared.Side;

public class PlayerTroop extends Troop {

    private static final Point2D LEFT_BRIDGE_POS = new Point2D(3.5,17);
    private static final Point2D RIGHT_BRIDGE_POS = new Point2D(14.5,17);
    private Iterator<Point2D> itTargets;
    

    public PlayerTroop(String name, Image pic, double x, double y) {
        super(name, pic, x, y);
        itTargets = defaultRoute.iterator();
    }

    public PlayerTroop(String name, Image pic, int n, int m) {
        super(name, pic, n, m);
        itTargets = defaultRoute.iterator();
    }

    @Override
    public int getSide() {
        return Side.PLAYER;
    }

    @Override
    public void goToNextTarget() {

        if (itTargets.hasNext()) {
            // new target
            target = itTargets.next();
        } else {
            // has reached the end.
            position = new Point2D(target.getX(), target.getY());
            speed = new Point2D(0, 0);
        }
    }

    @Override
    protected void initTargetList() {
        this.defaultRoute = new LinkedList<>();

        if (getPosX() < Model.MAP_COLS / 2) { 
            // if is on the left side
            this.defaultRoute.add(LEFT_BRIDGE_POS);
            this.defaultRoute.add(new Point2D(3.5, 6.5));
        } else { 
            // if is on the right side
            this.defaultRoute.add(RIGHT_BRIDGE_POS); 
            this.defaultRoute.add(new Point2D(14.5, 6.5));
        }
    }

    @Override
    protected void setFirstTarget() {
        target = defaultRoute.get(0);
    }
    
}
