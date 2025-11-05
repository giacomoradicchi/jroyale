package jroyale.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jroyale.model.towers.ArcerTower;
import jroyale.model.towers.KingTower;
import jroyale.shared.Side;

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
    
    
    private final List<Entity> renderOrderEntities = new ArrayList<>(); // buffer for rendering
    private List<Entity> entities = new ArrayList<>(); // insert order entities

    private long lastTimeStamp;


    public Model() {
        initReachableTiles();

        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                if (/* true ||  */reachableTiles[i][j])
                    this.map[i][j] = new Tile();
            }
        }

        initTowers();
    }

    @Override
    public boolean[][] getReachableTiles() {
        return reachableTiles;
    }

    @Override
    public void update(long now) {
        long elapsed = getElapsed(now);

        updateMap();

        for (Entity e : entities) {
            e.update(elapsed);
        }

        // for debugging:
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                System.out.print("| ");
                if (reachableTiles[i][j]) {
                    if (map[i][j].getEntities().size() > 0) {
                        System.out.print(map[i][j].getEntities().size());
                    } else {
                        System.out.print(" ");
                    }
                    
                }
                    
                else 
                    System.out.print("-");
                System.out.print(" |");
            }
            System.out.println();
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
        addEntity(troop);
    }

    @Override
    public List<Entity> getEntitiesOrderedByPosY() {
        // this method has to be called for each frame, because order might change fast

        // clears renderOrderTroops buffer and puts every troop entry
        renderOrderEntities.clear();
        renderOrderEntities.addAll(entities);
        Collections.sort(renderOrderEntities); // sorting based on Y pos (sorting is 
        // based on double Y coords, not on column; otherwise a simple for loop 
        // scan for j = 0..COLS-1 would have been sufficient)
        return renderOrderEntities;
    }

    

    // 
    // PRIVATE METHODS
    //

    private void updateMap() {
        
        for (Entity e : entities) {
            if (e.isOutsideTile()) { // when an entity is moving, his position change, so it might go outside his tile:
                // in that case, it has to be displaced from the previous tile to the newest.

                // TODO: in the future, if it will exist a troop whose footprintSize is > 1, it is necessary also to 
                // remove entity from the sorrounding cells.
                map[e.getCurrentI()][e.getCurrentJ()].removeEntity(e);
                e.updateCurrentTile();
                map[e.getCurrentI()][e.getCurrentJ()].addEntity(e);
            }

        }
    }

    private void addEntity(Entity e) {
        int i = (int) Math.floor(e.getY());
        int j = (int) Math.floor(e.getX());

        if (!reachableTiles[i][j]) 
            throw new IllegalArgumentException("Unable to drop " + e.getClass().getSimpleName() + " in [" +  i + ", " + j + "]: this part of the map is unreachable.");
        
        
        addEntityToMap(e, i, j, e.getFootPrintSize());
        e.updateCurrentTile();
        entities.add(e);
    }

    private void addEntityToMap(Entity e, int centreI, int centreJ, int footprintSize) {
        int offsetI = centreI - footprintSize/2;
        int offsetJ = centreJ - footprintSize/2;

        // this method works also for entities whose footprint is grather than 1 (e.g. Towers).
        // it adds entity also on the sorrounding cells that are close to (centreI, centreJ) based on
        // footprintSize
        for (int i = 0; i < footprintSize; i++) {
            for (int j = 0; j < footprintSize; j++) {
                if (0 <= i && i < MAP_ROWS && 0 <= j && j < MAP_COLS
                 && reachableTiles[offsetI + i][offsetJ + j]) 
                    map[offsetI + i][offsetJ + j].addEntity(e);
            }
        }
    }

    private long getElapsed(long now) {
        long elapsed = 0;
        if (lastTimeStamp != 0) {
            elapsed = now - lastTimeStamp;
        } 
        lastTimeStamp = now;
        return elapsed;
    }

    private void initTowers() {
        addEntity(new KingTower(Side.PLAYER));
        addEntity(new ArcerTower(Side.PLAYER, ArcerTower.LEFT));
        addEntity(new ArcerTower(Side.PLAYER, ArcerTower.RIGHT));

        addEntity(new KingTower(Side.OPPONENT));
        addEntity(new ArcerTower(Side.OPPONENT, ArcerTower.LEFT));
        addEntity(new ArcerTower(Side.OPPONENT, ArcerTower.RIGHT));   
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
