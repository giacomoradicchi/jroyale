package jroyale.model;

import java.util.ArrayList;
import java.util.List;

import jroyale.model.towers.Tower;
import jroyale.utils.Point;
import jroyale.utils.Enums.Side;

public class TowerTargetSelector {
    
    private static List<Tower> playerTowers = new ArrayList<>();
    private static List<Tower> opponentTowers = new ArrayList<>();

    public static void addTower(Tower tower) {
        if (tower.getSide() == Side.PLAYER) 
            playerTowers.add(tower);
        else 
            opponentTowers.add(tower);
    } 

    public static void removeTower(Tower tower) {
        if (tower.getSide() == Side.PLAYER) 
            playerTowers.remove(tower);
        else 
            opponentTowers.remove(tower);
    }

    public static Tower getClosestEnemyTower(Entity e) {
        List<Tower> targets;

        if (e.getSide() == Side.PLAYER) 
            targets = opponentTowers;
        else 
            targets = playerTowers;
        
        Tower closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Tower tower : targets) {
            if (tower.getHitPoints() == 0) continue; // this means the target is dead

            double distance = Point.distance(e.getX(), e.getY(), tower.getX(), tower.getY());

            if (distance < minDistance) {
                closest = tower;
                minDistance = distance;
            }
        }

        return closest;
    }
}
