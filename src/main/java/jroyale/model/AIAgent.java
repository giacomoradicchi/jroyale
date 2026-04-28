package jroyale.model;

import java.util.Random;

import jroyale.model.cards.Card;
import jroyale.model.cards.Deck;
import jroyale.model.towers.Tower;
import jroyale.model.troops.Troop;
import jroyale.utils.Enums.Side;

public class AIAgent {

    private static AIAgent instance = null;

    private AIAgent() {}

    public enum AgentDifficulty {
        BASIC(0.25, 0.1),
        STANDARD(0.5, 0.2), 
        EXPERT(0.75, 0.5),
        MASTER(1, 0.75);  

        private double intelligence; // in [0, 1]
        private double proactivity; // in [0, 1]
        
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

    /* 
        TODO: AI has to be:
        * able to recognise different situations (attack, defense, danger)
        * able to decides which cart to play based on opps troops
        * where to drop card based on situation
    */


    private AgentDifficulty difficulty; 
    private AgentAction action;
    private Random random;
    private Deck deck;
    private long idleAccumulator;
    private long idleTime;                  // time between decisions.                                      

    private static final long MIN_IDLE_TIME = 500_000_000L;             // in nanosec
    private static final long MAX_IDLE_TIME = 10_000_000_000L;          // in nanosec
    private static final double MAX_POLYNOMIAL_DEGREE = 1.75;            // max polinomial degree of proactivity function
    private static final double MIN_POLYNOMIAL_DEGREE = 0.25;            // max polinomial degree of proactivity function
    private static final double MAX_NOISE_STDEV = 0.2;                  // the higher the greater noise
    private static final double NEUTRAL_PROBABILITY = 0.5;          
    private static final double DEFENCE_DECISION_THRESHOLD = 0.35;      // it'll defend if intention <= defence threshold
    private static final double ATTACK_DECISION_THRESHOLD = 0.75;       // it'll attack if intention >= attack threshold

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

        //System.out.println("difficulty: " + this.difficulty);
    }

    public void init(AgentDifficulty difficulty, Deck deck) {
        this.action = AgentAction.IDLE;
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
    }

    public void update(long elapsed) {
        deck.update(elapsed);

        //System.out.println("elixir left: " + deck.getElixir());

        switch (action) {
            case AgentAction.ATTACK:
                handleAttack();
                break;
            case AgentAction.DEFEND:
                handleDefence();
                break;
            case AgentAction.IDLE:
                handleIdle(elapsed);
                break;
            default:
                break;
        }
    }

    private void handleAttack() {
        // TODO: attack logic

        System.out.println("attack!");
        deck.selectCard(2);
        if (deck.isSelectedCardDroppable())
            deck.dropSelectedCard(13, 10, Side.OPPONENT);

        // reset to idle
        action = AgentAction.IDLE;
    }

    private void handleDefence() {
        // TODO: defence logic

        System.out.print("defense. Should defend on ");
        if (shouldDefendOnRight()) {
            System.out.println("right.");
        } else {
            System.out.println("left.");
        }
        deck.selectCard(0);

        int row = 5;
        int col;
        if (shouldDefendOnRight()) {
            col = 14;
        } else {
            col = 5;
        }
        if (deck.isSelectedCardDroppable())
            deck.dropSelectedCard(row, col, Side.OPPONENT);

        
        // reset to idle
        action = AgentAction.IDLE;
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
        
        if (leftAITowerHitPoints == 0 && rightAITowerHitPoints == 0) {
            AITowerHealthRight = NEUTRAL_PROBABILITY;
        } else {
            AITowerHealthRight = (double) leftAITowerHitPoints / (leftAITowerHitPoints + rightAITowerHitPoints);
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
        double shouldDefendOnRight = (AIPresenceRight + AITowerHealthRight + playerPresenceRight)/3;

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


        int[] totalPlayerHitPoints = getTotalPlayerHitPoints();
        int[] totalAIHitPoints = getTotalAIHitPoints();

        double attackProbability = 0;

        for (int i = 0; i < totalAIHitPoints.length; i++) {
            double totalHitPoints = totalAIHitPoints[i] + totalPlayerHitPoints[i];
            if (totalHitPoints > 0) {
                // if player_entities-entities ratio is high, intention value is higher (so it will probably attack)
                // otherwise intention value is lower (so it will probably defend)
                attackProbability += (double) totalAIHitPoints[i] / totalHitPoints;
            } else {
                attackProbability += NEUTRAL_PROBABILITY;
            }
        }

        attackProbability /= totalAIHitPoints.length;

        if (deck.getElixir() == Deck.getMaxElixir()) {
            attackProbability = 1;
        }
        
        double proactivity = difficulty.getProactivity();

        double intention = proactivityFunction(attackProbability, getDegreeFromProactivity(proactivity));

        
        // adding little noise:
        double noiseStdev = (1 - difficulty.getIntelligence()) * MAX_NOISE_STDEV;
        double noise = noiseStdev * random.nextGaussian();
        intention += noise;

        // intention must stay between 0 and 1
        intention = Math.clamp(intention, 0, 1);

        //System.out.println("Attack Probability: " + attackProbability + ", proactivity: " + proactivity + ", intention: " + intention);


        if (intention <= DEFENCE_DECISION_THRESHOLD) {
            action = AgentAction.DEFEND;
        } else if (intention >= ATTACK_DECISION_THRESHOLD) {
            action = AgentAction.ATTACK;
        }

        // otherwise, it stays in IDLE.
    }

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

    private int[] getTotalPlayerHitPoints() {
        int[] totalHitPoints = new int[2];

        // 0: total player troop hitpoints
        // 1: total player tower hitpoints
        
        for (Entity e : Model.getInstance().getPlayerEntities()) {
            if (e instanceof Troop) {
                totalHitPoints[0] += e.getHitPoints();
            } else if (e instanceof Tower) {
                totalHitPoints[1] += e.getHitPoints();
            }
        }

        return totalHitPoints;
    }

    private int[] getTotalAIHitPoints() {
        int[] totalHitPoints = new int[2];

        // 0: total AI troop hitpoints
        // 1: total AI tower hitpoints
        
        for (Entity e : Model.getInstance().getOpponentEntities()) {
            if (e instanceof Troop) {
                totalHitPoints[0] += e.getHitPoints();
            } else if (e instanceof Tower) {
                totalHitPoints[1] += e.getHitPoints();
            }
        }

        return totalHitPoints;
    }

    private void resetIdleAccumulator() {
        idleAccumulator = 0;
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
}   
