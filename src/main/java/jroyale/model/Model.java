package jroyale.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jroyale.shared.TowerIndex;
import jroyale.utils.Point;

public class Model implements IModel {
    // 32x18 is the map size, each Tile 
    // has its own List where character are inserted based on
    // their position (so the collision algoritm will be much
    // more efficient)
    
    static final int MAP_ROWS = 32;
    static final int MAP_COLS = 18;
    private Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];
    private boolean[][] reachableTiles = new boolean[MAP_ROWS][MAP_COLS];

    // logic coords explaination:
    // for the X coords: since there are 18 cols, we will use a 
    // coord-system whose origin is 0 and his head is 18-. X-coords will 
    // be continue, so it has to be double.

    // logic coords for Towers:
    
    private final static Point[] TOWERS_CENTRE = getTowersCentre();
    
    private final List<Troop> renderOrderTroops = new ArrayList<>();
    private List<Troop> troops = new LinkedList<>();
    private long lastTimeStamp;


    public Model() {
        initReachableTiles();

        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                if (reachableTiles[i][j])
                    this.map[i][j] = new Tile();
            }
        }
    }

    @Override
    public boolean[][] getReachableTiles() {
        return reachableTiles;
    }

    // location tower X
    @Override
    public double getTowerCentreX(int towerType) {
        if (towerType < 0 || towerType >= TowerIndex.NUM_TOWERS) {
            throw new IllegalArgumentException("Invalid tower type: " + towerType);
        }
        return TOWERS_CENTRE[towerType].getX();
    }

    // location tower Y
    @Override
    public double getTowerCentreY(int towerType) {
        if (towerType < 0 || towerType >= TowerIndex.NUM_TOWERS) {
            throw new IllegalArgumentException("Invalid tower type: " + towerType);
        }
        return TOWERS_CENTRE[towerType].getY();
    }

    @Override
    public void update(long now) {
        long elapsed = 0;
        if (lastTimeStamp != 0) {
            elapsed = now - lastTimeStamp;
        } 
        lastTimeStamp = now;
        for (Troop troop : troops) {
            troop.moove(elapsed);
        }

    }

    @Override
    public int getRowsCount() {
        return MAP_ROWS;
    }

    @Override
    public int getColsCount() {
        return MAP_COLS;    
    }

    @Override
    public void addPlayerTroop(PlayerTroop troop) {
        troops.add(troop);
    }

    @Override
    public List<Troop> getTroopsOrderedByPosY() {
        // this method has to be called for each frame, because order might change fast

        // clears renderOrderTroops buffer and puts every troop entry
        renderOrderTroops.clear(); 
        renderOrderTroops.addAll(troops);
        Collections.sort(renderOrderTroops); // sorting based on Y pos
        return renderOrderTroops;
    }

    

    // 
    // PRIVATE METHODS
    //

    private static Point[] getTowersCentre() {
        Point[] centres = new Point[TowerIndex.NUM_TOWERS];

        centres[TowerIndex.PLAYER_KING_TOWER] = Entity.PLAYER_KING_TOWER_CENTRE;
        centres[TowerIndex.PLAYER_LEFT_TOWER] = Entity.PLAYER_LEFT_TOWER_CENTRE;
        centres[TowerIndex.PLAYER_RIGHT_TOWER] = Entity.PLAYER_RIGHT_TOWER_CENTRE;

        centres[TowerIndex.OPPONENT_KING_TOWER] =  Entity.OPPONENT_KING_TOWER_CENTRE;
        centres[TowerIndex.OPPONENT_LEFT_TOWER] =  Entity.OPPONENT_LEFT_TOWER_CENTRE;
        centres[TowerIndex.OPPONENT_RIGHT_TOWER] = Entity.OPPONENT_RIGHT_TOWER_CENTRE;
        return centres;
    }

    private void initReachableTiles() {
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                this.reachableTiles[i][j] = true;
            }
        }
        // removing unreachable tiles:
        // first 4 cells and last 4 cells in the first row and the last row
        // will be unreachable by players.

        for (int j = 0; j < 4; j++) {
            this.reachableTiles[0][j] = false;
            this.reachableTiles[MAP_ROWS-1][j] = false;
        }
        for (int j = 0; j < 4; j++) {
            this.reachableTiles[0][MAP_COLS - 1 - j] = false;
            this.reachableTiles[MAP_ROWS - 1][MAP_COLS - 1 - j] = false;
        }

        // also some of the cells in the middle won't be reachable:

        for (int j = 0; j < MAP_COLS; j++) {
            this.reachableTiles[MAP_ROWS/2 - 1][j] = false;
            this.reachableTiles[MAP_ROWS/2][j] = false;
        }

        this.reachableTiles[MAP_ROWS/2 - 1][3] = true;
        this.reachableTiles[MAP_ROWS/2][3] = true;

        this.reachableTiles[MAP_ROWS/2 - 1][MAP_COLS - 1 - 3] = true;
        this.reachableTiles[MAP_ROWS/2][MAP_COLS - 1 - 3] = true;
    }
}
