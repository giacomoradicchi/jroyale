package jroyale.model;

public class Model implements IModel{
    // 18x32 it's the map size, each Tile 
    // has its own List where character are inserted based on
    // their position (so the collision algoritm will be much
    // more efficient)
    
    private static final int MAP_ROWS = 32;
    private static final int MAP_COLS = 18;
    private Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];
    private boolean[][] reachableTiles = new boolean[MAP_ROWS][MAP_COLS];
    
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
    public int[] getPlayerTowerPosition() {
        // TODO Auto-generated method stub
        return new int[]{};
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }
    
}
