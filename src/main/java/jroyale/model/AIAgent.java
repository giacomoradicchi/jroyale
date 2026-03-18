package jroyale.model;

public class AIAgent {

    public enum AgentIntelligence {
        BASIC(0.25),
        STANDARD(0.5), 
        EXPERT(0.75),
        MASTER(1);  

        double intelligence; // in [0, 1]
        
        private AgentIntelligence(double intelligence) {
            this.intelligence = intelligence;
        }

        public double getIntelligenceValue() {
            return intelligence;
        }
    }


    private AgentIntelligence intelligence; 

}
