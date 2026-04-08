package jroyale.model;

import java.util.Random;

public class AIAgent {

    private static AIAgent instance = null;

    private AIAgent() {}

    public enum AgentIntelligence {
        BASIC(0.25),
        STANDARD(0.5), 
        EXPERT(0.75),
        MASTER(1);  

        private double intelligence; // in [0, 1]
        
        private AgentIntelligence(double intelligence) {
            this.intelligence = intelligence;
        }

        public double getIntelligenceValue() {
            return intelligence;
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


    private AgentIntelligence intelligence; 
    private AgentAction action;
    private Random random;

    private long idleAccumulator;
    

    private long idleTime; // time between decisions.
    private final long MIN_IDLE_TIME = 100_000_000L;    // 100 ms   (10^8 nanosec)
    private final long MAX_IDLE_TIME = 1_000_000_000L;  // 1.0 s    (10^9 nanosec)

    public void init(String intelligence) {
        switch (intelligence.toUpperCase()) {
            case "BASIC":
                init(AgentIntelligence.BASIC);
                break;
            case "STANDARD":
                init(AgentIntelligence.STANDARD);
                break;
            case "EXPERT":
                init(AgentIntelligence.EXPERT);
                break;
            case "MASTER":
                init(AgentIntelligence.MASTER);
                break;
        
            default:
                init(AgentIntelligence.STANDARD);
                break;
        }
    }

    public void init(AgentIntelligence intelligence) {
        this.action = AgentAction.IDLE;
        setIntelligence(intelligence); 
        this.random = new Random();
    }

    public void setIntelligence(AgentIntelligence intelligence) {
        this.intelligence = intelligence;
        this.idleTime = getIdleTime();
    }

    public void update(long elapsed) {
        
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
        // reset to idle
        action = AgentAction.IDLE;
    }

    private void handleDefence() {
        // TODO: defence logic

        System.out.println("defense.");

        // reset to idle
        action = AgentAction.IDLE;
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

        // TODO: modify attackWeight based on health and aggressivity
        double intention = 0.5;

        // adding little noise:
        double intelligenceValue = intelligence.getIntelligenceValue();

        double noiseVariance = (1 - intelligenceValue);
        double noise = Math.sqrt(noiseVariance) * random.nextGaussian();
        intention += noise;

        // intention must stay between 0 and 1
        intention = Math.clamp(intention, 0, 1);

        // decision: if intention is in [0, 0.5[ it attacks. otherwise it defends.
        if (intention < 0.5) {
            action = AgentAction.ATTACK;
        } else {
            action = AgentAction.DEFEND;
        }
    }

    private void resetIdleAccumulator() {
        idleAccumulator = 0;
    }

    private long getIdleTime() {
        // the smarter the AI is the shorter the idle time.
        double intelligenceValue = intelligence.getIntelligenceValue();
        
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
