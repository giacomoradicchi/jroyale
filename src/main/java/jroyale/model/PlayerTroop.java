package jroyale.model;


import java.util.LinkedList;
import java.util.Iterator;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import jroyale.shared.Side;

public class PlayerTroop extends Troop {

    private Iterator<Point2D> itTargets;
    

    public PlayerTroop(String name, Image pic, double x, double y) {
        super(name, pic, x, y, Troop.MEDIUM);
        itTargets = defaultRoute.iterator();
    }

    public PlayerTroop(String name, Image pic, int n, int m) {
        super(name, pic, n, m, Troop.MEDIUM);
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
            this.defaultRoute.add(Entity.LEFT_BRIDGE_START_POS);
            this.defaultRoute.add(Entity.LEFT_BRIDGE_END_POS);
            this.defaultRoute.add(Entity.OPPONENT_LEFT_TOWER_CENTRE);
        } else { 
            // if is on the right side
            this.defaultRoute.add(Entity.RIGHT_BRIDGE_START_POS);
            this.defaultRoute.add(Entity.RIGHT_BRIDGE_END_POS); 
            this.defaultRoute.add(Entity.OPPONENT_RIGHT_TOWER_CENTRE);
        }
        this.defaultRoute.add(Entity.OPPONENT_KING_TOWER_CENTRE);
    }

    @Override
    protected void setFirstTarget() {
        target = defaultRoute.get(0);
    }
    
}
