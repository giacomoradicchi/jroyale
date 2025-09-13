package jroyale.model;

import java.awt.geom.Point2D;

public class Model implements IModel{
    // 32x18 it's the map size, each Tile 
    // has its own List where character are inserted based on
    // their position (so the collision algoritm will be much
    // more efficient)
    
    private static final int MAP_ROWS = 32;
    private static final int MAP_COLS = 18;
    private Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];
    private boolean[][] reachableTiles = new boolean[MAP_ROWS][MAP_COLS];

    // logic coords explaination:
    // for the X coords: since there are 18 cols, we will use a 
    // coord-system whose origin is 0 and his head is 18-. X-coords will 
    // be continue, so it has to be double.

    // logic coords for Towers:
    // (9, 29)
    private final static Point2D.Float PLAYER_KING_TOWER_CENTRE = new Point2D.Float(9f, 29f);

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

    // location tower: (8.5, 26.5)
    @Override
    public float getPlayerKingTowerCentreX() {
        return (float) PLAYER_KING_TOWER_CENTRE.getX();
    }

    @Override
    public float getPlayerKingTowerCentreY() {
        return (float) PLAYER_KING_TOWER_CENTRE.getY();
    }

    @Override
    public void update() {
        // TODO
    }

    @Override
    public int getRowCount() {
        return MAP_ROWS;
    }

    @Override
    public int getColsCount() {
        return MAP_COLS;    
    }
    
}
