package jroyale.utils;

public class Enums {

    public enum EntityType {
        // towers
        KING_TOWER(false),
        ARCHER_TOWER(false),

        // troops
        MINIPEKKA(true),
        GIANT(false),
        SKELETONS(true),
        SKELETON_ARMY(true),
        PEKKA(true),
        VALKYRIE(true);

        private boolean canDefend;

        private EntityType(boolean canDefend) {
            this.canDefend = canDefend;
        }

        public boolean canDefend() {
            return canDefend;
        }
    }

    public enum Side {
        PLAYER,
        OPPONENT
    }

    public enum State {
        IDLE,
        MOVE,
        ATTACK
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        IMPOSSIBLE
    }
    
}
