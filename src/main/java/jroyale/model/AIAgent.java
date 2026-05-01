package jroyale.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import jroyale.model.cards.Card;
import jroyale.model.cards.Deck;
import jroyale.model.towers.Tower;
import jroyale.model.troops.Troop;
import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.Side;

public class AIAgent {

    private static AIAgent instance = null;

    /* 
        TODO: AI has to be:
        * able to recognise different situations (attack, defense, danger)
        * able to decides which cart to play based on opps troops
        * where to drop card based on situation
    */                             

    private static final long MIN_IDLE_TIME = 1_000_000_000L;                 // in nanosec
    private static final long MAX_IDLE_TIME = 3_000_000_000L;               // in nanosec
    private static final double MAX_POLYNOMIAL_DEGREE = 1.75;               // max polinomial degree of proactivity function
    private static final double MIN_POLYNOMIAL_DEGREE = 0.25;               // max polinomial degree of proactivity function
    private static final double MAX_NOISE_STDEV = 0.2;                      // the higher the greater noise
    private static final double NEUTRAL_INTENTION = 0.5;          
    private static final double DEFENCE_DECISION_THRESHOLD = 0.45;          // it'll defend if intention <= defence threshold
    private static final double ATTACK_DECISION_THRESHOLD = 0.55;           // it'll attack if intention >= attack threshold
    private static final double TOWER_WEIGHT = 0.3;
    private static final int DEFENCE_DROP_ROW = 5;
    private static final int DEFENCE_DROP_COL_RIGHT = 13;
    private static final int DEFENCE_DROP_COL_LEFT = 5;
    private static final int ATTACK_DROP_ROW = 13;
    private static final int ATTACK_DROP_COL = 10;
    private static final int ATTACK_CARD_INDEX = 2;                         // TODO: remove when handleAttack will be modified
    private static final int FALLBACK_CARD_INDEX = 0;
    private static final double MIN_ELIXIR_COST = 1.0;                      
    private static final int DEFENCE_SIDE_FACTOR_COUNT = 3;
    private static final double NORMALIZE_WAIT_AFTER_OTHER_SIDE_TIME = 0.5; // defines what fraction of idle time to wait after one side has completed a move.
    private static final int TROOP_INDEX = 0;
    private static final int TOWER_INDEX = 1;
    private static final int PLAYER_INDEX = 0;
    private static final int AI_INDEX = 1;
    private static final int AGENTS = 2; // player and opponent
    private static final int FEATURES_AMOUNT = 2;

    private static final Map<EntityType, Map<EntityType, Double>> DEFENCE_SCORE = Map.of(
        // entitytype e1 -> map of entities e2 that best defend e1. defend score goes from 0 to 1.
        EntityType.MINIPEKKA, Map.of(
            EntityType.SKELETON_ARMY,   1.0,
            EntityType.PEKKA,           0.8,
            EntityType.MINIPEKKA,       0.6,
            EntityType.VALKYRIE,        0.4,
            EntityType.SKELETONS,       0.2
        ),
        EntityType.GIANT, Map.of(
            EntityType.SKELETON_ARMY,   1.0,
            EntityType.PEKKA,           0.8,
            EntityType.MINIPEKKA,       0.6,
            EntityType.VALKYRIE,        0.4,
            EntityType.SKELETONS,       0.2
        ),
        EntityType.PEKKA, Map.of(
            EntityType.SKELETON_ARMY,   1.0,
            EntityType.PEKKA,           0.8,
            EntityType.MINIPEKKA,       0.6,
            EntityType.VALKYRIE,        0.4,
            EntityType.SKELETONS,       0.2
        ),
        EntityType.SKELETONS, Map.of(
            EntityType.VALKYRIE,        1.0,
            EntityType.SKELETON_ARMY,   0.8,
            EntityType.SKELETONS,       0.6,
            EntityType.MINIPEKKA,       0.4,
            EntityType.PEKKA,           0.2
        ),
        EntityType.SKELETON_ARMY, Map.of(
            EntityType.VALKYRIE,        1.0,
            EntityType.SKELETON_ARMY,   0.8,
            EntityType.SKELETONS,       0.6,
            EntityType.MINIPEKKA,       0.4,
            EntityType.PEKKA,           0.2
        ),
        EntityType.VALKYRIE, Map.of(
            EntityType.MINIPEKKA,       1.0,
            EntityType.PEKKA,           0.8,
            EntityType.VALKYRIE,        0.6,
            EntityType.SKELETONS,       0.4,
            EntityType.SKELETON_ARMY,   0.2
        )
    );

    private IModel model = IModel.getInstance();
    private AgentDifficulty difficulty; 
    private AgentAction leftAction;
    private AgentAction rightAction;
    private double leftIntention = NEUTRAL_INTENTION;
    private double rightIntention = NEUTRAL_INTENTION;
    private Random random;
    private Deck deck;
    private long leftIdleAccumulator;
    private long rightIdleAccumulator;
    private long idleTime;                  // time between decisions.  
    private long waitAfterOtherSideTime;  
    private boolean hasWaitedPlayer;    
    private double[][] totalLeftHitPoints = new double[AGENTS][FEATURES_AMOUNT];
    private double[][] totalRightHitPoints = new double[AGENTS][FEATURES_AMOUNT];
    private double[] attackIntentions = new double[FEATURES_AMOUNT];
    private HashMap<EntityType, Double> troopSetWeighted = new HashMap<>();
    private ArrayList<Integer> cardIndexes = new ArrayList<>();
    private HashMap<Integer, Double> cardScores = new HashMap<>();

    public enum AgentDifficulty {
        BASIC       (0.25,   0.25),
        STANDARD    (0.50,   0.50), 
        EXPERT      (0.75,   0.75),
        MASTER      (1.00,   1.00);  

        private double intelligence;    // in [0, 1]
        private double proactivity;     // in [0, 1]
        
        private AgentDifficulty(double intelligence, double proactivity) {
            this.intelligence = intelligence;
            this.proactivity = proactivity;
        }

        public double getIntelligence() {
            return intelligence;
        }

        public double getProactivity() {
            return proactivity;
        }
    }

    private enum AgentAction {
        ATTACK,
        DEFEND,
        IDLE;
    }

    private AIAgent() {}

    public void init(String difficulty, Deck deck) {
        switch (difficulty.strip().toUpperCase()) {
            case "BASIC":
                init(AgentDifficulty.BASIC, deck);
                break;
            case "STANDARD":
                init(AgentDifficulty.STANDARD, deck);
                break;
            case "EXPERT":
                init(AgentDifficulty.EXPERT, deck);
                break;
            case "MASTER":
                init(AgentDifficulty.MASTER, deck);
                break;
        
            default:
                init(AgentDifficulty.STANDARD, deck);
                break;
        }

    }

    public void init(AgentDifficulty difficulty, Deck deck) {
        this.leftAction = AgentAction.IDLE;
        this.rightAction = AgentAction.IDLE;
        setDeck(deck);
        setIntelligence(difficulty); 
        this.random = new Random();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setIntelligence(AgentDifficulty difficulty) {
        this.difficulty = difficulty;
        this.idleTime = getIdleTime();
        this.waitAfterOtherSideTime = (long) (idleTime * NORMALIZE_WAIT_AFTER_OTHER_SIDE_TIME);
    }

    public void handlePlayerCardDropped() {
        if (!hasWaitedPlayer) {
            resetLeftToIdle();
            resetRightToIdle();
            hasWaitedPlayer = true;
            System.out.println("ciao");
        }
    }

    public void update(long elapsed) {
        deck.update(elapsed);

        if (shouldHandleLeftFirst()) {
            handleLeftSide(elapsed);
            handleRightSide(elapsed);
        } else {
            handleRightSide(elapsed);
            handleLeftSide(elapsed);
        }

    }

    private boolean shouldHandleLeftFirst() {  
        // assuming that it should first defend and then attack.

        // left should defend and right shouldn't (left has to be handled first)
        if (leftIntention < NEUTRAL_INTENTION && rightIntention >= NEUTRAL_INTENTION) return true;

        // left shouldn't defend and right should (left has to be handled after right)
        if (leftIntention >= NEUTRAL_INTENTION && rightIntention < NEUTRAL_INTENTION) return false;

        // the most important side is the one whose intention is closer to 0 (urge to defend) or 1 (urge to attack)
        return Math.abs(leftIntention - NEUTRAL_INTENTION) > Math.abs(rightIntention - NEUTRAL_INTENTION);
    }

    private void handleLeftSide(double elapsed) {
        switch (leftAction) {
            case AgentAction.ATTACK:
                handleLeftAttack();
                break;
            case AgentAction.DEFEND:
                handleLeftDefence();
                break;
            case AgentAction.IDLE:
                handleLeftIdle(elapsed);
                break;
            default:
                break;
        }
    }

    private void handleRightSide(double elapsed) {
        switch (rightAction) {
            case AgentAction.ATTACK:
                handleRightAttack();
                break;
            case AgentAction.DEFEND:
                handleRightDefence();
                break;
            case AgentAction.IDLE:
                handleRightIdle(elapsed);
                break;
            default:
                break;
        }
    }

    private void handleLeftIdle(double elapsed) {
        leftIdleAccumulator += elapsed;

        // update intention
        if (leftIntention != NEUTRAL_INTENTION) leftIntention = NEUTRAL_INTENTION;

        if (leftIdleAccumulator >= idleTime) { // AI will attack / defend next time
            decideNextLeftAction();
        }
        
    }

    private void handleRightIdle(double elapsed) {
        rightIdleAccumulator += elapsed;

        // update intention
        if (leftIntention != NEUTRAL_INTENTION) leftIntention = NEUTRAL_INTENTION;

        if (rightIdleAccumulator >= idleTime) { // AI will attack / defend next time
            decideNextRightAction();
        }
    }

    private void handleLeftAttack() {
        // TODO: left attack logic

        // update intention
        System.out.println("ATTACK LEFT");

        // reset time
        resetPlayerWait();
        resetLeftToIdle();
        makeRightWait();
    }

    private void handleRightAttack() {
        // TODO: right attack logic

        // update intention
        System.out.println("ATTACK RIGHT");

        resetPlayerWait();
        makeLeftWait();
        resetRightToIdle();
    }

    private void handleLeftDefence() {
        deck.selectCard(selectLeftDefenceCard());

        // update intention
        
        if (deck.isSelectedCardDroppable()) {
            deck.dropSelectedCard(DEFENCE_DROP_ROW, DEFENCE_DROP_COL_LEFT, Side.OPPONENT);
            System.out.println("DEFENCE LEFT");
            makeRightWait();
        }
            

        resetPlayerWait();
        resetLeftToIdle();
    }

    private void handleRightDefence() {
        deck.selectCard(selectRightDefenceCard());

        // update intention
        
        if (deck.isSelectedCardDroppable()) {
            deck.dropSelectedCard(DEFENCE_DROP_ROW, DEFENCE_DROP_COL_RIGHT, Side.OPPONENT);
            System.out.println("DEFENCE RIGHT");
            makeLeftWait();
        }
            

        resetPlayerWait();
        resetRightToIdle();
    }

    //-------------------------------
    // DECISION METHODS (FOR IDLE)
    //-------------------------------

    private void decideNextLeftAction() {

        // AI will decide wheather attack or not based on health.
        // his intention will be modified by some gaussian noise, whose stddev changes 
        // based on AI difficulty.

        leftIntention = getSthocasticIntention(getLeftRawIntention());

        if (leftIntention <= DEFENCE_DECISION_THRESHOLD) {
            leftAction = AgentAction.DEFEND;
        } else if (leftIntention >= ATTACK_DECISION_THRESHOLD) {
            leftAction = AgentAction.ATTACK;
        }

        // otherwise, stays in IDLE
        
    }

    private void decideNextRightAction() {

        // AI will decide wheather attack or not based on health.
        // his intention will be modified by some gaussian noise, whose stddev changes 
        // based on AI difficulty.

        rightIntention = getSthocasticIntention(getRightRawIntention());

        if (rightIntention <= DEFENCE_DECISION_THRESHOLD) {
            rightAction = AgentAction.DEFEND;
        } else if (rightIntention >= ATTACK_DECISION_THRESHOLD) {
            rightAction = AgentAction.ATTACK;
        }

        // otherwise, stays in IDLE
    }

    private double getSthocasticIntention(double rawIntention) {
        // camp so the intention will stay between 0 and 1
        return Math.clamp(
            addNoise(
                proactivityFunction(
                    rawIntention, 
                    difficulty.getProactivity()
                )
            ),
            0,  
            1
        );
    }

    private double addNoise(double value) {
        return value + getNoiseStd() * random.nextGaussian();
    }

    private double getNoiseStd() {
        return (1 - difficulty.getIntelligence()) * MAX_NOISE_STDEV;
    }

    private double getLeftRawIntention() {
        calculateTotalLeftHitPoints();

        for (int feature = 0; feature < FEATURES_AMOUNT; feature++) {
            double totalAIHitPoints = totalLeftHitPoints[AI_INDEX][feature];
            double totalHitPoints = totalLeftHitPoints[PLAYER_INDEX][feature] + totalAIHitPoints;

            if (totalHitPoints > 0) {
                // if player_entities-entities ratio is high, intention value is higher (so it will probably attack)
                // otherwise intention value is lower (so it will probably defend)
                attackIntentions[feature] = totalAIHitPoints / totalHitPoints;
            } else {
                attackIntentions[feature] = NEUTRAL_INTENTION;
            }
        }

        double rawIntention = attackIntentions[TROOP_INDEX] * (1-TOWER_WEIGHT) + attackIntentions[TOWER_INDEX] * TOWER_WEIGHT;

        return rawIntention;
    }

    private double getRightRawIntention() {
        calculateTotalRightHitPoints();

        for (int feature = 0; feature < FEATURES_AMOUNT; feature++) {
            double totalAIHitPoints = totalRightHitPoints[AI_INDEX][feature];
            double totalHitPoints = totalRightHitPoints[PLAYER_INDEX][feature] + totalAIHitPoints;

            if (totalHitPoints > 0) {
                // if player_entities-entities ratio is high, intention value is higher (so it will probably attack)
                // otherwise intention value is lower (so it will probably defend)
                attackIntentions[feature] = totalAIHitPoints / totalHitPoints;
            } else {
                attackIntentions[feature] = NEUTRAL_INTENTION;
            }
        }

        double rawIntention = attackIntentions[TROOP_INDEX] * (1-TOWER_WEIGHT) + attackIntentions[TOWER_INDEX] * TOWER_WEIGHT;

        return rawIntention;
    }

    private void calculateTotalLeftHitPoints() {
        double mapHeight = model.getTileSize() * model.getRowsCount();
        double mapWidth = model.getTileSize() * model.getColsCount();

        // calculating player hitpoints
        for (Entity e : Model.getInstance().getPlayerEntities()) {
            if (e.getX() > mapWidth/2) continue;    // skip entities on right side of the map

            if (e instanceof Troop) {
                double positionWeight = e.getY() / mapHeight; 
                totalLeftHitPoints[PLAYER_INDEX][TROOP_INDEX] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalLeftHitPoints[PLAYER_INDEX][TOWER_INDEX] += e.getHitPoints();
            }
        }

        // calculating AI hitpoints
        for (Entity e : Model.getInstance().getOpponentEntities()) {
            if (e.getX() > mapWidth/2) continue;    // skip entities on right side of the map

            if (e instanceof Troop) {
                double positionWeight = e.getY() / mapHeight; 
                totalLeftHitPoints[AI_INDEX][TROOP_INDEX] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalLeftHitPoints[AI_INDEX][TOWER_INDEX] += e.getHitPoints();
            }
        }
    }

    private void calculateTotalRightHitPoints() {
        double mapHeight = model.getTileSize() * model.getRowsCount();
        double mapWidth = model.getTileSize() * model.getColsCount();

        // calculating player hitpoints
        for (Entity e : Model.getInstance().getPlayerEntities()) {
            if (e.getX() < mapWidth/2) continue;    // skip entities on left side of the map

            if (e instanceof Troop) {
                double positionWeight = e.getY() / mapHeight; 
                totalRightHitPoints[PLAYER_INDEX][TROOP_INDEX] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalRightHitPoints[PLAYER_INDEX][TOWER_INDEX] += e.getHitPoints();
            }
        }

        // calculating AI hitpoints
        for (Entity e : Model.getInstance().getOpponentEntities()) {
            if (e.getX() < mapWidth/2) continue;    // skip entities on left side of the map

            if (e instanceof Troop) {
                double positionWeight = e.getY() / mapHeight; 
                totalRightHitPoints[AI_INDEX][TROOP_INDEX] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalRightHitPoints[AI_INDEX][TOWER_INDEX] += e.getHitPoints();
            }
        }
    }

    //-------------------------------
    // DECISION METHODS (FOR DEFENCE)
    //-------------------------------

    private int selectLeftDefenceCard() {

        calculateLeftTroopSetWeighted();
        calculateCardIndexes();

        if (cardIndexes.isEmpty()) return FALLBACK_CARD_INDEX; 

        calculateCardScores();

        return getBestCardIndex();
    }

    private int selectRightDefenceCard() {

        calculateRightTroopSetWeighted();
        calculateCardIndexes();

        if (cardIndexes.isEmpty()) return FALLBACK_CARD_INDEX; 

        calculateCardScores();

        return getBestCardIndex();
    }
    

    private void calculateLeftTroopSetWeighted() {
        calculateTroopSetWeighted(false);
    } 

    private void calculateRightTroopSetWeighted() {
        calculateTroopSetWeighted(true);
    }

    private void calculateTroopSetWeighted(boolean rightSide) {
        // 1. detecting all troop types on left side of the field
        troopSetWeighted.clear();

        double mapWidth = model.getTileSize() * model.getColsCount();
        double mapHeight = model.getTileSize() * model.getRowsCount();

        for (Entity e : model.getPlayerEntities()) {
            if (!(e instanceof Troop)                       // is not a troop
            || (!rightSide && e.getX() >= mapWidth/2)       // troop is on right side but it should be in left one
            || ( rightSide && e.getX() <  mapWidth/2)      // troop is on left side but it should be in right one
            ) continue;

            // calculating weight based on Y position
            EntityType type = e.getType();
            double weight = 1 - e.getY()/mapHeight; // weight is higher if player troop is closer to opponent towers

            if (troopSetWeighted.getOrDefault(type, 0.0) < weight) // put or replace previous key if weight value is higher 
                troopSetWeighted.put(type, weight);
        }
    }

    private void calculateCardIndexes() {
        cardIndexes.clear(); // getting indexes of the only cards that can defend
        for (int i = 0; i < Deck.AVAILABLE_CARDS_SIZE; i++) {
            if (deck.getCurrentCard(i).getType().canDefend()) {
                cardIndexes.add(i);
            }
        }
    }

    private void calculateCardScores() {
        cardScores.clear();
        for (int cardIndex : cardIndexes) {
            double cardScore = 0;
            Card defenceCard = deck.getCurrentCard(cardIndex);
            for (EntityType playerType : troopSetWeighted.keySet()) {
                Map<EntityType, Double> defenceScoreForType = DEFENCE_SCORE.get(playerType);
                if (defenceScoreForType == null) continue;

                EntityType cardType = defenceCard.getType();
                double weight = troopSetWeighted.get(playerType);
                double defenceScore = defenceScoreForType.getOrDefault(cardType, 0.0) * weight;
                double elixirCost = Math.max(MIN_ELIXIR_COST, defenceCard.getCardStats().getElixirCost());  // avoid division by 0
                double elixirScore = Math.min(1.0, deck.getElixir()/elixirCost); // elixirScore has to be in [0, 1]
                cardScore += (defenceScore + elixirScore)/2; // total score is the mean between defenceScore and elixirScore
            }
            
            cardScores.put(cardIndex, cardScore);
        }
    }

    private int getBestCardIndex() {
        int bestCardIndex = cardIndexes.getFirst();
        double bestScore = cardScores.get(bestCardIndex);
        for (int i = 1; i < cardIndexes.size(); i++) {
            int cardIndex = cardIndexes.get(i);
            double score = cardScores.get(cardIndex);
            if (score > bestScore) {
                bestCardIndex = cardIndex;
                bestScore = score;
            }
        }

        return bestCardIndex;
    }

    
    //-------------------------------
    // RESET AND WAIT METHODS
    //-------------------------------

    private void resetLeftToIdle() {
        leftIdleAccumulator = 0;
        leftAction = AgentAction.IDLE;
    }

    private void resetRightToIdle() {
        rightIdleAccumulator = 0;
        rightAction = AgentAction.IDLE;
    }

    private void resetPlayerWait() {
        hasWaitedPlayer = false;
    }

    private void makeLeftWait() {
        leftAction = AgentAction.IDLE;
        leftIdleAccumulator = Math.max(0, leftIdleAccumulator - waitAfterOtherSideTime);
    }

    private void makeRightWait() {
        rightAction = AgentAction.IDLE;
        rightIdleAccumulator = Math.max(0, rightIdleAccumulator - waitAfterOtherSideTime);
    }


    /* private void handleAttack() {
        // TODO: attack logic
        deck.selectCard(ATTACK_CARD_INDEX);
        if (deck.isSelectedCardDroppable())
            deck.dropSelectedCard(ATTACK_DROP_ROW, ATTACK_DROP_COL, Side.OPPONENT);
        action = AgentAction.IDLE;
    }

    private void handleDefence() {
        boolean shouldDefendOnRight = shouldDefendOnRight();
        deck.selectCard(selectDefenceCard(shouldDefendOnRight));

        int col = shouldDefendOnRight ? DEFENCE_DROP_COL_RIGHT : DEFENCE_DROP_COL_LEFT;

        if (deck.isSelectedCardDroppable())
            deck.dropSelectedCard(DEFENCE_DROP_ROW, col, Side.OPPONENT);

        action = AgentAction.IDLE;
    }

    private int selectDefenceCard(boolean rightSide) {
        IModel model = Model.getInstance();

        // 1. detecting all troop types on left side of the field
        HashMap<EntityType, Double> troopSetWeighted = new HashMap<>();

        double midMap = model.getTileSize() * model.getColsCount() / 2;
        double heightMap = model.getTileSize() * model.getRowsCount();

        for (Entity e : model.getPlayerEntities()) {
            if (!(e instanceof Troop)               // is not a troop
            || (!rightSide && e.getX() >= midMap)   // troop is on right side but it should be in left one
            || ( rightSide && e.getX() <  midMap)   // troop is on left side but it should be in right one
            ) continue;

            // calculating weight based on Y position
            EntityType type = e.getType();
            double weight = 1 - e.getY()/heightMap; // weight is higher if player troop is closer to opponent towers

            if (troopSetWeighted.getOrDefault(type, 0.0) < weight) // put or replace previous key if weight value is higher 
                troopSetWeighted.put(type, weight);
        }

        ArrayList<Integer> cardIndexes = new ArrayList<>(); // getting indexes of the only cards that can defend
        for (int i = 0; i < Deck.AVAILABLE_CARDS_SIZE; i++) {
            if (deck.getCurrentCard(i).getType().canDefend()) {
                cardIndexes.add(i);
            }
        }

        if (cardIndexes.isEmpty()) return FALLBACK_CARD_INDEX; 

        // 2. calculating card scores
        HashMap<Integer, Double> cardScores = new HashMap<>();
        for (int cardIndex : cardIndexes) {
            double cardScore = 0;
            Card defenceCard = deck.getCurrentCard(cardIndex);
            for (EntityType playerType : troopSetWeighted.keySet()) {
                Map<EntityType, Double> defenceScoreForType = DEFENCE_SCORE.get(playerType);
                if (defenceScoreForType == null) continue;

                EntityType cardType = defenceCard.getType();
                double weight = troopSetWeighted.get(playerType);
                double defenceScore = defenceScoreForType.getOrDefault(cardType, 0.0) * weight;
                double elixirCost = Math.max(MIN_ELIXIR_COST, defenceCard.getCardStats().getElixirCost());  // avoid division by 0
                double elixirScore = Math.min(1.0, deck.getElixir()/elixirCost); // elixirScore has to be in [0, 1]
                cardScore += (defenceScore + elixirScore)/2; // total score is the mean between defenceScore and elixirScore
            }
            
            cardScores.put(cardIndex, cardScore);
        }

        // 3. extracting maximum score
        int bestCardIndex = cardIndexes.getFirst();
        double bestScore = cardScores.get(bestCardIndex);
        for (int i = 1; i < cardIndexes.size(); i++) {
            int cardIndex = cardIndexes.get(i);
            double score = cardScores.get(cardIndex);
            if (score > bestScore) {
                bestCardIndex = cardIndex;
                bestScore = score;
            }
        }

        return bestCardIndex;
    }

    private boolean shouldDefendOnRight() {

        // 0 -> should defend on left
        // 1 -> should defend on right

        double AIPresenceRight;         // from 0 to 1. 0 -> AI is mainly on the right, so it should defend on left. 1 -> viceversa
        double AITowerHealthRight;      // from 0 to 1. 0 -> right tower has more hitpoints than the left one, so it should defend on left. 1 -> viceversa
        double playerPresenceRight;     // from 0 to 1. 0 -> there's more player (enemy) troops in the left, so we should defend on left. 1 -> viceversa

        // 1. decision based on AI hit points
        int leftAIHitPoints = 0;
        int rightAIHitPoints = 0;

        IModel model = Model.getInstance();
        for (Entity e : model.getOpponentEntities()) {
            if (e.getX() < model.getTileSize() * model.getColsCount() / 2) { // left side of the field
                leftAIHitPoints += e.getHitPoints();
            } else {
                rightAIHitPoints += e.getHitPoints();
            }
        }

        if (leftAIHitPoints == 0 && rightAIHitPoints == 0) {
            AIPresenceRight = NEUTRAL_PROBABILITY;
        } else {
            AIPresenceRight = (double) leftAIHitPoints / (leftAIHitPoints + rightAIHitPoints);
        }

        // 2. decision based on tower health
        int leftAITowerHitPoints = model.getOpponentLeftTower().getHitPoints();
        int rightAITowerHitPoints = model.getOpponentRightTower().getHitPoints();
        int totalAITowerHitPoints = leftAITowerHitPoints + rightAITowerHitPoints;
        
        if (totalAITowerHitPoints == 0) {
            AITowerHealthRight = NEUTRAL_PROBABILITY;
        } else {
            AITowerHealthRight = (double) leftAITowerHitPoints / totalAITowerHitPoints;
        }

        // 3. decision based on player weighted hit points
        double leftPlayerHitPoints = 0;
        double rightPlayerHitPoints = 0;
        double mapHeight = model.getTileSize() * model.getRowsCount();

        for (Entity e : model.getPlayerEntities()) {
            double weight = (1 - e.getY()/mapHeight); // the closer to ai towers, the higher the weight

            if (e.getX() < model.getTileSize() * model.getColsCount() / 2) { // left side of the field
                leftPlayerHitPoints += e.getDamage() * weight;
            } else {
                rightPlayerHitPoints += e.getDamage() * weight;
            }
        }

        playerPresenceRight = rightPlayerHitPoints / (leftPlayerHitPoints + rightPlayerHitPoints);

        // 4. final mean:
        double shouldDefendOnRight = (AIPresenceRight + AITowerHealthRight + playerPresenceRight)/DEFENCE_SIDE_FACTOR_COUNT;

        return shouldDefendOnRight > NEUTRAL_PROBABILITY;
    }

    private void handleIdle(long elapsed) {
        idleAccumulator += elapsed;

        if (idleAccumulator >= idleTime) { // AI will attack / defend next time
            decideNextAction();     
            resetIdleAccumulator();
        }
    }

    private void decideNextAction() {

        // AI will decide wheather attack or not based on health and aggressivity.
        // his intention will be modified by some gaussian noise, whose stddev changes 
        // based on AI difficulty.


        double[] totalPlayerHitPoints = getTotalPlayerHitPoints();
        double[] totalAIHitPoints = getTotalAIHitPoints();

        double[] attackProbabilities = new double[totalAIHitPoints.length]; 

        for (int i = 0; i < totalAIHitPoints.length; i++) {
            double totalHitPoints = totalAIHitPoints[i] + totalPlayerHitPoints[i];
            if (totalHitPoints > 0) {
                // if player_entities-entities ratio is high, intention value is higher (so it will probably attack)
                // otherwise intention value is lower (so it will probably defend)
                attackProbabilities[i] = (double) totalAIHitPoints[i] / totalHitPoints;
            } else {
                attackProbabilities[i] = NEUTRAL_PROBABILITY;
            }
        }

        double attackProbability = attackProbabilities[0] * (1-TOWER_WEIGHT) + attackProbabilities[1] * TOWER_WEIGHT;

        double proactivity = difficulty.getProactivity();

        double intention = proactivityFunction(attackProbability, getDegreeFromProactivity(proactivity));

        
        // adding little noise:
        double noiseStdev = (1 - difficulty.getIntelligence()) * MAX_NOISE_STDEV;
        double noise = noiseStdev * random.nextGaussian();
        intention += noise;

        // intention must stay between 0 and 1
        intention = Math.clamp(intention, 0, 1);

        //System.out.println("Attack Probability: " + attackProbability + ", proactivity: " + proactivity + ", intention: " + intention);

        System.out.print("intention: " + intention + ". ");
        if (intention <= DEFENCE_DECISION_THRESHOLD) {
            action = AgentAction.DEFEND;
            System.out.println("defence");
        } else if (intention >= ATTACK_DECISION_THRESHOLD) {
            action = AgentAction.ATTACK;
            System.out.println("attack");
        } else {
            System.out.println("idle");
        }
        
        // otherwise, it stays in IDLE.
    }
 */
    private double proactivityFunction(double x, double degree) {
        // this function was built for x in [0, 1]

        // edge cases
        if (x <= 0) return 0;  
        if (x >= 1) return 1;

        // alpha > 1 (pushes x to the limits):
        // if 0 <= x < 0.5 -> 0 <= output <= x (pushes x to 0)
        // if x = 0.5 -> output = x 
        // if 0.5 < x <= 1 -> x <= output <= 1 (pushes x to 1)

        // 0 < alpha < 1 (pushes x to 0.5):
        // if 0 <= x < 0.5 -> 0 <= x <= output 
        // if x = 0.5 -> output = x = 0.5
        // if 0.5 < x <= 1 -> output <= x <= 1 

        return 1.0 / (1.0 + Math.pow((1-x)/x, degree)); 
    }

    private double getDegreeFromProactivity(double proactivity) {
        // considering input in [0, 1]
        return MIN_POLYNOMIAL_DEGREE * (1 - proactivity) + MAX_POLYNOMIAL_DEGREE * proactivity;
    }

    private double[] getTotalPlayerHitPoints() {
        double[] totalHitPoints = new double[2];

        // 0: total player troop hitpoints
        // 1: total player tower hitpoints

        IModel model = Model.getInstance();
        double mapHeight = model.getTileSize() * model.getRowsCount();

        for (Entity e : Model.getInstance().getPlayerEntities()) {
            if (e instanceof Troop) {
                double positionWeight = e.getY() / mapHeight; // the more is closer to AI map side, the higher the weight
                totalHitPoints[0] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalHitPoints[1] += e.getHitPoints();
            }
        }

        return totalHitPoints;
    }

    private double[] getTotalAIHitPoints() {
        double[] totalHitPoints = new double[2];

        // 0: total AI troop hitpoints
        // 1: total AI tower hitpoints

        IModel model = Model.getInstance();
        double mapHeight = model.getTileSize() * model.getRowsCount();
        
        for (Entity e : Model.getInstance().getOpponentEntities()) {
            if (e instanceof Troop) {
                double positionWeight = 1 - e.getY() / mapHeight; // AI can attack when most of its troops are in its side
                totalHitPoints[0] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalHitPoints[1] += e.getHitPoints();
            }
        }

        return totalHitPoints;
    }

    private long getIdleTime() {
        // the smarter the AI is the shorter the idle time.
        double intelligenceValue = difficulty.getIntelligence();
        
        // weighted mean between min idle time and max idle time. (linear interpolation)
        // if it's smart, it tends to min time. otherwise, tends to max time.
        // properly works when intelligenceValue is in [0, 1].

        long weightedTime = (long) (intelligenceValue * MIN_IDLE_TIME + (1-intelligenceValue) * MAX_IDLE_TIME);

        // clamp to min and max just to be sure it is inside bounds.
        return Math.clamp(weightedTime, MIN_IDLE_TIME, MAX_IDLE_TIME);
    }

    // static methods
    public static AIAgent getInstance() {
        if (instance == null) 
            instance = new AIAgent();

        return instance;
    }


    //----------------------
    // OLD METHODS (COPIED)
    //---------------------
    // TODO: REMOVE THEM
    //---------------------


    /* 
    private double[] getTotalPlayerHitPoints() {
        double[] totalHitPoints = new double[2];

        // 0: total player troop hitpoints
        // 1: total player tower hitpoints

        double mapHeight = model.getTileSize() * model.getRowsCount();

        for (Entity e : Model.getInstance().getPlayerEntities()) {
            if (e instanceof Troop) {
                double positionWeight = e.getY() / mapHeight; // the more is closer to AI map side, the higher the weight
                totalHitPoints[0] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalHitPoints[1] += e.getHitPoints();
            }
        }

        return totalHitPoints;
    }

    private double[] getTotalAIHitPoints() {
        double[] totalHitPoints = new double[2];

        // 0: total AI troop hitpoints
        // 1: total AI tower hitpoints

        double mapHeight = model.getTileSize() * model.getRowsCount();
        
        for (Entity e : Model.getInstance().getOpponentEntities()) {
            if (e instanceof Troop) {
                double positionWeight = 1 - e.getY() / mapHeight; // AI can attack when most of its troops are in its side
                totalHitPoints[0] += e.getHitPoints() * positionWeight;
            } else if (e instanceof Tower) {
                totalHitPoints[1] += e.getHitPoints();
            }
        }

        return totalHitPoints;
    } 
        
    
    
    
    
    
    
    */


}   
