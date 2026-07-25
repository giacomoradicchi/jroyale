package jroyale.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jroyale.model.cards.Card;
import jroyale.model.cards.Deck;
import jroyale.model.cards.GiantCard;
import jroyale.model.cards.MiniPekkaCard;
import jroyale.model.cards.PekkaCard;
import jroyale.model.cards.SkeletonArmyCard;
import jroyale.model.cards.SkeletonCard;
import jroyale.model.cards.ValkyrieCard;
import jroyale.model.towers.ArcherTower;
import jroyale.model.towers.KingTower;
import jroyale.model.towers.Tower;
import jroyale.model.troops.Troop;
import jroyale.utils.Config;
import jroyale.utils.Enums.Side;

public class Model implements IModel {

    private static Model instance;

    // map dimension constants
    private static final int MAP_ROWS = 32;
    private static final int MAP_COLS = 18;
    private static final double TILE_SIZE = 1;
    
    // drop logic and tile constants
    private static final double NORMALIZED_DROP_LIMIT = 1.0/3;
    private static final int UNREACHABLE_CORNER_SIZE = 4;
    private static final int BRIDGE_START_OFFSET = 2;
    private static final int BRIDGE_END_OFFSET = 4;
    private static final int MAP_HALF_DIVISOR = 2;
    
    // time conversion constants
    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    private static final int ZERO_VALUE = 0;
    private static final long INITIAL_ACCUMULATOR = 0L;
    private static final long INITIAL_TIMESTAMP = 0L;

    private Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];
    private final boolean[][] reachableTiles = new boolean[MAP_ROWS][MAP_COLS];

    private boolean[][] playerDroppableTiles = new boolean[MAP_ROWS][MAP_COLS];
    private boolean[][] opponentDroppableTiles = new boolean[MAP_ROWS][MAP_COLS];

    private Deck playerDeck;
    
    private final List<Entity> renderOrderEntities = new ArrayList<>(); // buffer for rendering
    private final List<Entity> toRemoveEntities = new ArrayList<>(); // buffer for entities to remove
    private List<Entity> playerEntities = new ArrayList<>();
    private List<Entity> opponentEntities = new ArrayList<>();
    
    // towers
    private Tower playerKingTower, opponentKingTower, playerLeftTower, opponentLeftTower, playerRightTower, opponentRightTower;

    private boolean gameOver = false;

    // time variables
    private long maxTimeNanoSec;
    private long accumulator;
    private long lastTimeStamp;

    private Model() {}

    @Override
    public void init(int maxTimeSec) {
        init(maxTimeSec * NANOS_PER_SECOND);
    }

    private void init(long maxTimeNanoSec) {
        this.maxTimeNanoSec = maxTimeNanoSec; // adding +1 sec because time will be counted from maxTimeNanoSec included to 1 instead of maxTimeNanoSec-1 to 0

        initReachableTiles();

        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                if (reachableTiles[i][j])
                    this.map[i][j] = new Tile();
            }
        }
        
        playerDeck = new Deck(new Card[] {
            MiniPekkaCard.getInstance(),
            GiantCard.getInstance(),
            SkeletonCard.getInstance(),
            SkeletonArmyCard.getInstance(),
            PekkaCard.getInstance(),
            ValkyrieCard.getInstance()
        });

        initTowers();
        initDroppableTiles();

        initAIAgent(new Deck(new Card[] {
            MiniPekkaCard.getInstance(),
            GiantCard.getInstance(),
            SkeletonCard.getInstance(),
            SkeletonArmyCard.getInstance(),
            PekkaCard.getInstance(),
            ValkyrieCard.getInstance()
        }));
    }

    @Override
    public void reset() {
        renderOrderEntities.clear();
        toRemoveEntities.clear();
        playerEntities.clear();
        opponentEntities.clear();
        gameOver = false;
        accumulator = INITIAL_ACCUMULATOR;
        lastTimeStamp = INITIAL_TIMESTAMP;
        ITowerTargetSelector.getInstance().reset();
        map = new Tile[MAP_ROWS][MAP_COLS];
        playerDroppableTiles = new boolean[MAP_ROWS][MAP_COLS];
        opponentDroppableTiles = new boolean[MAP_ROWS][MAP_COLS];
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
        if (i < ZERO_VALUE || i >= MAP_ROWS || j < ZERO_VALUE || j >= MAP_COLS)
            return false;
        
        return reachableTiles[i][j];
    }

    @Override
    public boolean isPlayerEntityDroppableOnTile(int i, int j) {
        if (i < ZERO_VALUE || i >= MAP_ROWS || j < ZERO_VALUE || j >= MAP_COLS)
            return false;
        
        return playerDroppableTiles[i][j];
    }

    @Override
    public boolean isOpponentEntityDroppableOnTile(int i, int j) {
        if (i < ZERO_VALUE || i >= MAP_ROWS || j < ZERO_VALUE || j >= MAP_COLS)
            return false;
        
        return opponentDroppableTiles[i][j];
    }

    @Override
    public void update(long now) {
        long elapsed = getElapsed(now);
        accumulator += elapsed;

        for (Entity e : playerEntities) {
            e.update(elapsed);
        }

        for (Entity e : opponentEntities) {
            e.update(elapsed);
        }

        updateMap();

        playerDeck.update(elapsed);
        AIAgent.getInstance().update(elapsed);
        checkGameOver();
    }

    private void checkGameOver() {
        if (isTimeExceeded() || isPlayerKingTowerDestroyed() || isOpponentKingTowerDestroyed()) {
            gameOver = true;
        }
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    private boolean isTimeExceeded() {
        return getTimeLeftSec() == 0;
    }

    @Override
    public boolean isPlayerKingTowerDestroyed() {
        return playerKingTower.getHitPoints() == ZERO_VALUE;
    }

    @Override
    public boolean isOpponentKingTowerDestroyed() {
        return opponentKingTower.getHitPoints() == ZERO_VALUE;
    }

    @Override
    public boolean isPlayerLeftTowerDestroyed() {
        return playerLeftTower.getHitPoints() == ZERO_VALUE;
    }

    @Override
    public boolean isOpponentLeftTowerDestroyed() {
        return opponentLeftTower.getHitPoints() == ZERO_VALUE;
    }

    @Override
    public boolean isPlayerRightTowerDestroyed() {
        return playerRightTower.getHitPoints() == ZERO_VALUE;
    }

    @Override
    public boolean isOpponentRightTowerDestroyed() {
        return opponentRightTower.getHitPoints() == ZERO_VALUE;
    }

    @Override
    public int getTimeLeftSec() {
        return (int) Math.ceil(Math.max(ZERO_VALUE, (double) (maxTimeNanoSec - accumulator) / NANOS_PER_SECOND));
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
    public double getTileSize() {
        return TILE_SIZE;
    }

    @Override
    public void addTroop(Troop troop) {
        addEntity(troop);

        // notify AI that player has dropped a troop
        if (troop.getSide() == Side.PLAYER)
            AIAgent.getInstance().handlePlayerCardDropped();
    }

    @Override
    public void setSelectedCard(int cardIndex) {
        playerDeck.selectCard(cardIndex);
    }

    @Override
    public boolean dropPlayerCard(int row, int col) {
        if (playerDeck.isSelectedCardDroppable()) {
            playerDeck.dropSelectedCard(row, col, Side.PLAYER);
            return true;
        }

        return false;
    }

    @Override
    public List<Entity> getEntitiesOrderedByPosY() {
        sortRenderOrderEntities();
        return renderOrderEntities;
    }

    private void sortRenderOrderEntities() {
        renderOrderEntities.clear();
        renderOrderEntities.addAll(playerEntities);
        renderOrderEntities.addAll(opponentEntities);
        Collections.sort(renderOrderEntities); 
    }

    @Override
    public List<Entity> getEntitiesOnTile(int i, int j) {
        if (i < ZERO_VALUE || i >= MAP_ROWS || j < ZERO_VALUE || j >= MAP_COLS || !reachableTiles[i][j])
            return new ArrayList<>();
        
        return map[i][j].getEntities();
    }

    @Override
    public List<Entity> getPlayerEntities() {
        return playerEntities;
    }

    @Override
    public List<Entity> getOpponentEntities() {
        return opponentEntities;
    }

    @Override
    public Card getFirstHandPlayerCard() {
        return playerDeck.getCurrentFirstCard();
    }

    @Override
    public Card getSecondHandPlayerCard() {
        return playerDeck.getCurrentSecondCard();
    }

    @Override
    public Card getThirdHandPlayerCard() {
        return playerDeck.getCurrentThirdCard();
    }

    @Override
    public Card getFourthHandPlayerCard() {
        return playerDeck.getCurrentFourthCard();
    }

    @Override
    public byte getPlayerElixirLeft() {
        return playerDeck.getElixir();
    }

    @Override
    public byte getMaxElixir() {
        return Deck.getMaxElixir();
    }

    @Override
    public int getAvailableDeckCards() {
        return Deck.AVAILABLE_CARDS_SIZE;
    }

    @Override
    public double getPlayerElixirChargeTimeProgress() {
        return playerDeck.getChargeTimeProgress();
    }

    private void updateMap() {
        updateMap(playerEntities);
        updateMap(opponentEntities);
    }

    private void updateMap(List<Entity> entities) {
        Iterator<Entity> itEntities = entities.iterator();
        toRemoveEntities.clear();

        while (itEntities.hasNext()) {
            Entity e = itEntities.next();
            if (e.getHitPoints() == ZERO_VALUE) {
                toRemoveEntities.add(e);
            }
            if (e.isOutsideTile()) { 
                removeEntityFromMap(e, e.getCurrentI(), e.getCurrentJ(), e.getFootPrintSize());
                e.updateCurrentTile();
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

        if (e.getSide() == Side.PLAYER) 
            playerEntities.remove(e);
        else 
            opponentEntities.remove(e);

        if (e instanceof ArcherTower) {
            updateDroppableTiles((ArcherTower) e);
        }
    }

    private void updateDroppableTiles(ArcherTower tower) {
        if (tower.getSide() == Side.OPPONENT) {
            updatePlayerDroppableTiles(tower);
        } else {
            updateOpponentDroppableTiles(tower);
        }
    }

    private void updatePlayerDroppableTiles(ArcherTower opponentTower) {
        int cols = getColsCount();
        int rows = getRowsCount();
        int startI = (int) Math.ceil(rows * NORMALIZED_DROP_LIMIT);
        int endI = rows;

        int startJ;
        int endJ;
        if (opponentTower.getCurrentJ() < cols / MAP_HALF_DIVISOR) {
            startJ = ZERO_VALUE;
            endJ = cols / MAP_HALF_DIVISOR;
        } else {
            endJ = cols;
            startJ = endJ / MAP_HALF_DIVISOR;
        }

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                playerDroppableTiles[i][j] = reachableTiles[i][j];
            }
        }
    }

    private void updateOpponentDroppableTiles(ArcherTower opponentTower) {
        int cols = getColsCount();
        int rows = getRowsCount();
        int startI = ZERO_VALUE;
        int endI = (int) Math.floor(rows * (1.0 - NORMALIZED_DROP_LIMIT));

        int startJ;
        int endJ;
        if (opponentTower.getCurrentJ() < cols / MAP_HALF_DIVISOR) {
            startJ = ZERO_VALUE;
            endJ = cols / MAP_HALF_DIVISOR;
        } else {
            endJ = cols;
            startJ = endJ / MAP_HALF_DIVISOR;
        }

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                opponentDroppableTiles[i][j] = reachableTiles[i][j];
            }
        }
    }

    private void addEntity(Entity e) {

        int i = (int) Math.floor(e.getY());
        int j = (int) Math.floor(e.getX());

        if (!reachableTiles[i][j]) 
            throw new IllegalArgumentException("Unable to drop entity in unreachable area.");
        
        addEntityToMap(e, i, j, e.getFootPrintSize());
        e.updateCurrentTile();

        if (e.getSide() == Side.PLAYER)
            playerEntities.add(e);
        else 
            opponentEntities.add(e);
    }

    private void addEntityToMap(Entity e, int centreI, int centreJ, int footprintSize) {
        int offsetI = centreI - footprintSize / MAP_HALF_DIVISOR;
        int offsetJ = centreJ - footprintSize / MAP_HALF_DIVISOR;

        for (int i = 0; i < footprintSize; i++) {
            for (int j = 0; j < footprintSize; j++) {
                int finalI = offsetI + i;
                int finalJ = offsetJ + j;
                if (0 <= finalI && finalI < MAP_ROWS && 0 <= finalJ && finalJ < MAP_COLS && reachableTiles[finalI][finalJ]) 
                    map[finalI][finalJ].addEntity(e);
            }
        }
    }

    private void removeEntityFromMap(Entity e, int centreI, int centreJ, int footprintSize) {
        int offsetI = centreI - footprintSize / MAP_HALF_DIVISOR;
        int offsetJ = centreJ - footprintSize / MAP_HALF_DIVISOR;

        for (int i = 0; i < footprintSize; i++) {
            for (int j = 0; j < footprintSize; j++) {
                int finalI = offsetI + i;
                int finalJ = offsetJ + j;
                if (0 <= finalI && finalI < MAP_ROWS && 0 <= finalJ && finalJ < MAP_COLS && reachableTiles[finalI][finalJ]) 
                    map[finalI][finalJ].removeEntity(e);

                if (e instanceof Tower && e.getSide() == Side.PLAYER)
                    playerDroppableTiles[finalI][finalJ] = reachableTiles[finalI][finalJ];

                if (e instanceof Tower && e.getSide() == Side.OPPONENT)
                    opponentDroppableTiles[finalI][finalJ] = reachableTiles[finalI][finalJ];
            }
        }
    }

    private boolean isTileOccupied(int i, int j) {
        if (i < ZERO_VALUE || i >= MAP_ROWS || j < ZERO_VALUE || j >= MAP_COLS || !reachableTiles[i][j])
            throw new IllegalArgumentException("Tile unreachable.");
        return map[i][j].isOccupied();
    }

    private long getElapsed(long now) {
        long elapsed = ZERO_VALUE;
        if (lastTimeStamp != INITIAL_TIMESTAMP) {
            elapsed = now - lastTimeStamp;
        } 
        lastTimeStamp = now;
        return elapsed;
    }

    private void initTowers() {
        playerKingTower = new KingTower(Side.PLAYER);
        addTower(playerKingTower);
        playerLeftTower = new ArcherTower(Side.PLAYER, ArcherTower.LEFT);
        addTower(playerLeftTower);
        playerRightTower = new ArcherTower(Side.PLAYER, ArcherTower.RIGHT);
        addTower(playerRightTower);

        opponentKingTower = new KingTower(Side.OPPONENT);
        addTower(opponentKingTower);
        opponentLeftTower = new ArcherTower(Side.OPPONENT, ArcherTower.LEFT);
        addTower(opponentLeftTower);
        opponentRightTower = new ArcherTower(Side.OPPONENT, ArcherTower.RIGHT);
        addTower(opponentRightTower);   
    }

    private void addTower(Tower tower) {
        addEntity(tower);
        ITowerTargetSelector.getInstance().addTower(tower);
    }

    private void initReachableTiles() {
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                this.reachableTiles[i][j] = true;
            }
        }

        for (int j = 0; j < UNREACHABLE_CORNER_SIZE; j++) {
            this.reachableTiles[ZERO_VALUE][j] = false;
            this.reachableTiles[MAP_ROWS - 1][j] = false;
            this.reachableTiles[ZERO_VALUE][MAP_COLS - 1 - j] = false;
            this.reachableTiles[MAP_ROWS - 1][MAP_COLS - 1 - j] = false;
        }

        int midRow = MAP_ROWS / MAP_HALF_DIVISOR;
        for (int j = 0; j < MAP_COLS; j++) {
            this.reachableTiles[midRow - 1][j] = false;
            this.reachableTiles[midRow][j] = false;
        }

        for (int j = BRIDGE_START_OFFSET; j <= BRIDGE_END_OFFSET; j++) {
            this.reachableTiles[midRow - 1][j] = true;
            this.reachableTiles[midRow][j] = true;
            this.reachableTiles[midRow - 1][MAP_COLS - 1 - j] = true;
            this.reachableTiles[midRow][MAP_COLS - 1 - j] = true;
        }
    }

    private void initDroppableTiles() {
        int mid = (int) Math.floor(ArenaData.LEFT_BRIDGE_START_POS.getY());

        for (int i = 0; i < mid; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                opponentDroppableTiles[i][j] = reachableTiles[i][j] && !isTileOccupied(i, j);
            }
        }
        
        for (int i = mid; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                playerDroppableTiles[i][j] = reachableTiles[i][j] && !isTileOccupied(i, j);
            }
        }
    }

    private void initAIAgent(Deck AIDeck) {
        AIAgent.getInstance().init(Config.getInstance().getDifficulty(), AIDeck);
    }

    @Override
    public Tower getOpponentKingTower() { 
        return opponentKingTower; 
    }

    @Override
    public Tower getOpponentLeftTower() { 
        return opponentLeftTower; 
    }

    @Override
    public Tower getOpponentRightTower() { 
        return opponentRightTower; 
    }

    public static IModel getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
}
