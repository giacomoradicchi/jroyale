package jroyale.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jroyale.model.cards.Card;
import jroyale.model.cards.Deck;
import jroyale.model.cards.GiantCard;
import jroyale.model.cards.MiniPekkaCard;
import jroyale.model.towers.ArcerTower;
import jroyale.model.towers.KingTower;
import jroyale.model.towers.Tower;
import jroyale.model.troops.Troop;
import jroyale.shared.Side;

public class Model implements IModel {

    private static Model istance;

    // 32x18 is the map size, each Tile 
    // has its own List where character are inserted based on
    // their position (so the collision algoritm will be much
    // more efficient)
    
    public static final int MAP_ROWS = 32;
    public static final int MAP_COLS = 18;
    
    private Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];
    private final boolean[][] reachableTiles = new boolean[MAP_ROWS][MAP_COLS];

    private boolean[][] playerDroppableTiles = new boolean[MAP_ROWS][MAP_COLS];
    private boolean[][] opponentDroppableTiles = new boolean[MAP_ROWS][MAP_COLS];

    private Deck playerDeck;
    private Deck opponentDeck;

    // logic coords explaination:
    // for the X coords: since there are 18 cols, we will use a 
    // coord-system whose origin is 0 and his head is 18-. X-coords will 
    // be continue, so it has to be double.
    
    
    private final List<Entity> renderOrderEntities = new ArrayList<>(); // buffer for rendering
    private final List<Entity> toRemoveEntities = new ArrayList<>(); // buffer for entities to remove (when they die / get destroied)
    private List<Entity> entities = new ArrayList<>(); // insert order entities

    private long lastTimeStamp;


    private Model() {
        initReachableTiles();

        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                if (reachableTiles[i][j])
                    this.map[i][j] = new Tile();
            }
        }

        initTowers();
        initDroppableTiles();
        
        playerDeck = new Deck(new Card[] {
            MiniPekkaCard.getIstance(),
            GiantCard.getIstance()
        });
    }

    public static IModel getIstance() {
        if (istance == null) {
            istance = new Model();
        }
        return istance;
    }

    @Override
    public boolean[][] getReachableTiles() {
        return reachableTiles;
    }

    @Override
    public boolean[][] getPlayerDroppableTiles() {
        return playerDroppableTiles;
    }

    @Override
    public boolean isTileReachable(int i, int j) {
        if (i < 0 || i >= MAP_ROWS || j < 0 || j >= MAP_COLS)
            return false;
        
        return reachableTiles[i][j];
    }

    @Override
    public boolean isPlayerTroopDroppableOnTile(int i, int j) {
        if (i < 0 || i >= MAP_ROWS || j < 0 || j >= MAP_COLS)
            return false;
        
        return playerDroppableTiles[i][j];
    }

    @Override
    public void update(long now) {
        long elapsed = getElapsed(now);

        for (Entity e : entities) {
            e.update(elapsed);
        }

        updateMap();

        // for debugging:
        /* for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                System.out.print("| ");
                if (reachableTiles[i][j]) {
                    if (isTileOccupied(i, j)) {
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
        }  */

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
    public void addTroop(Troop troop) {
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

    @Override
    public List<Entity> getEntitiesOnTile(int i, int j) {
        if (i < 0 || i >= MAP_ROWS || j < 0 || j >= MAP_COLS || !reachableTiles[i][j])
            return new ArrayList<>();
        
        return map[i][j].getEntities();
    }

    // 
    // PRIVATE METHODS
    //

    private void updateMap() {

        Iterator<Entity> itEntities = entities.iterator();

        toRemoveEntities.clear();

        while (itEntities.hasNext()) {
            Entity e = itEntities.next();

            if (e.getHitPoints() == 0) {
                // means it's dead / destroied
                toRemoveEntities.add(e);
            }

            if (e.isOutsideTile()) { // when an entity is moving, his position change, so it might go outside his tile:
                // in that case, it has to be displaced from the previous tile to the newest.

                // remove entity from the sorrounding cells.
                removeEntityFromMap(e, e.getCurrentI(), e.getCurrentJ(), e.getFootPrintSize());
                // updating entity tile position
                e.updateCurrentTile();
                // adding entity to new tiles
                addEntityToMap(e, e.getCurrentI(), e.getCurrentJ(), e.getFootPrintSize());
            }
        }

        for (Entity e : toRemoveEntities) {
            removeEntity(e);
        }
    }

    private void removeEntity(Entity e) {
        e.onDelete();
        removeEntityFromMap(e, e.getCurrentI(), e.getCurrentJ(), e.getFootPrintSize());
        entities.remove(e);
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
                if (0 <= offsetI + i && offsetI + i < MAP_ROWS 
                    && 0 <= offsetJ + j && offsetJ + j < MAP_COLS
                 && reachableTiles[offsetI + i][offsetJ + j]) 
                    map[offsetI + i][offsetJ + j].addEntity(e);
            }
        }
    }

    private void removeEntityFromMap(Entity e, int centreI, int centreJ, int footprintSize) {
        int offsetI = centreI - footprintSize/2;
        int offsetJ = centreJ - footprintSize/2;

        // this method works also for entities whose footprint is grather than 1 (e.g. Towers).
        // it adds entity also on the sorrounding cells that are close to (centreI, centreJ) based on
        // footprintSize
        for (int i = 0; i < footprintSize; i++) {
            for (int j = 0; j < footprintSize; j++) {
                if (0 <= offsetI + i && offsetI + i < MAP_ROWS 
                    && 0 <= offsetJ + j && offsetJ + j < MAP_COLS
                 && reachableTiles[offsetI + i][offsetJ + j]) 
                    map[offsetI + i][offsetJ + j].removeEntity(e);
            }
        }
    }

    private boolean isTileOccupied(int i, int j) {
        if (i < 0 || i >= MAP_ROWS || j < 0 || j >= MAP_COLS || !reachableTiles[i][j])
            throw new IllegalArgumentException("Tile [" + i + ", " + j + "] is unreachable.");
            
        return map[i][j].isOccupied();
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
        addTower(new KingTower(Side.PLAYER));
        addTower(new ArcerTower(Side.PLAYER, ArcerTower.LEFT));
        addTower(new ArcerTower(Side.PLAYER, ArcerTower.RIGHT));

        addTower(new KingTower(Side.OPPONENT));
        addTower(new ArcerTower(Side.OPPONENT, ArcerTower.LEFT));
        addTower(new ArcerTower(Side.OPPONENT, ArcerTower.RIGHT));   
    }

    private void addTower(Tower tower) {
        addEntity(tower);
        TowerTargetSelector.addTower(tower);
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

        for (int j = 2; j <= 4; j++) {
            this.reachableTiles[MAP_ROWS/2 - 1][j] = true;
            this.reachableTiles[MAP_ROWS/2][j] = true;

            this.reachableTiles[MAP_ROWS/2 - 1][MAP_COLS - 1 - j] = true;
            this.reachableTiles[MAP_ROWS/2][MAP_COLS - 1 - j] = true;
        }
    }

    private void initDroppableTiles() {
        // every opponent tower are not damaged

        int start = (int) Math.floor(Entity.LEFT_BRIDGE_START_POS.getY());
        for (int i = start; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                playerDroppableTiles[i][j] = reachableTiles[i][j] && !isTileOccupied(i, j);
            }
        }
    }
}
