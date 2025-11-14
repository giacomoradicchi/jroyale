package jroyale.model;


import java.util.LinkedList;
import java.util.Iterator;
import jroyale.shared.Side;

import jroyale.utils.Point;

public class PlayerTroop extends Troop {

    private Iterator<Point> itTargets;
    

    public PlayerTroop(String name, double x, double y) {
        super(name, x, y, Troop.FAST, Side.PLAYER);
        itTargets = defaultRoute.iterator();
    }

    public PlayerTroop(String name, int n, int m) {
        super(name, n, m, Troop.FAST, Side.PLAYER);
        itTargets = defaultRoute.iterator();
    }

    @Override
    public void goToNextTarget() {

        if (itTargets.hasNext()) {
            // new target
            target = itTargets.next();
        } else {
            // has reached the end.
            position = new Point(target.getX(), target.getY());
            speed = new Point(0, 0);
        }
    }

    @Override
    protected void initTargetList() {
        this.defaultRoute = new LinkedList<>();

        // for debug
        //this.defaultRoute.add(new Point(-9, 2)); 
        //this.defaultRoute.add(new Point(Model.MAP_COLS/2.0, 8));
        //this.defaultRoute.add(new Point(Model.MAP_COLS/2.0, 18));
    


        if (getX() < Model.MAP_COLS / 2.0) { 
            // if is on the left side
            this.defaultRoute.add(Entity.OPPONENT_LEFT_TOWER_CENTRE);
        } else { 
            // if is on the right side
            this.defaultRoute.add(Entity.OPPONENT_RIGHT_TOWER_CENTRE);
        } 
        this.defaultRoute.add(Entity.OPPONENT_KING_TOWER_CENTRE); 
    }

    @Override
    protected void setFirstTarget() {
        target = defaultRoute.get(0);
    }
    
}
