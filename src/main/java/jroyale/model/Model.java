package jroyale.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jroyale.shared.TowerIndex;

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
    private final static Point2D.Double PLAYER_KING_TOWER_CENTRE = new Point2D.Double(9, 29);
    private final static Point2D.Double PLAYER_LEFT_TOWER_CENTRE = new Point2D.Double(3.5, 25.5);
    private final static Point2D.Double PLAYER_RIGHT_TOWER_CENTRE = new Point2D.Double(14.5, 25.5);

    private final static Point2D.Double OPPONENT_KING_TOWER_CENTRE = new Point2D.Double(9, 3);
    private final static Point2D.Double OPPONENT_LEFT_TOWER_CENTRE = new Point2D.Double(3.5, 6.5);
    private final static Point2D.Double OPPONENT_RIGHT_TOWER_CENTRE = new Point2D.Double(14.5, 6.5);
    private final static Point2D.Double[] TOWERS_CENTRE = getTowersCentre();
    
    private final List<Troop> renderOrderTroops = new ArrayList<>();
    private List<Troop> troops = new LinkedList<>();


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
    public void update() {
        for (Troop troop : troops) {
            troop.moove();
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

    private static Point2D.Double[] getTowersCentre() {
        Point2D.Double[] centres = new Point2D.Double[TowerIndex.NUM_TOWERS];

        centres[TowerIndex.PLAYER_KING_TOWER] = PLAYER_KING_TOWER_CENTRE;
        centres[TowerIndex.PLAYER_LEFT_TOWER] = PLAYER_LEFT_TOWER_CENTRE;
        centres[TowerIndex.PLAYER_RIGHT_TOWER] = PLAYER_RIGHT_TOWER_CENTRE;

        centres[TowerIndex.OPPONENT_KING_TOWER] =  OPPONENT_KING_TOWER_CENTRE;
        centres[TowerIndex.OPPONENT_LEFT_TOWER] =  OPPONENT_LEFT_TOWER_CENTRE;
        centres[TowerIndex.OPPONENT_RIGHT_TOWER] = OPPONENT_RIGHT_TOWER_CENTRE;
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
