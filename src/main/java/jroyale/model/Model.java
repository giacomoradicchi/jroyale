package jroyale.model;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import jroyale.shared.TowerIndex;

public class Model implements IModel {
    // 32x18 it's the map size, each Tile 
    // has its own List where character are inserted based on
    // their position (so the collision algoritm will be much
    // more efficient)
    
    private static final int MAP_ROWS = 32;
    private static final int MAP_COLS = 18;
    private Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];
    private boolean[][] reachableTiles = new boolean[MAP_ROWS][MAP_COLS];
    private List<Troop> playerTroops = new LinkedList<>();
    private List<Troop> opponentTroops = new LinkedList<>();

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

    public Model() {
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                this.map[i][j] = new Tile();
                this.reachableTiles[i][j] = true;
            }
        }

        removingReachableTiles();
    }

    private void removingReachableTiles() {
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
        for (Troop playerTroop : playerTroops) {
            playerTroop.shiftPosY(-0.01);
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
    public void addPlayerTroop(Troop troop) {
        playerTroops.add(troop);
    }

    @Override
    public Troop getPlayerTroop(int index) {
        return playerTroops.get(index);
    }

    @Override
    public Troop getOpponentTroop(int index) {
        return opponentTroops.get(index);
    }

    @Override
    public int getNumberOfPlayerTroops() {
        return playerTroops.size();
    }

    @Override
    public int getNumberOfOpponentTroops() {
        return playerTroops.size();
    }
}
