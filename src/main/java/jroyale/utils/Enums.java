package jroyale.utils;

public class Enums {

    public enum EntityType {
        // towers
        KING_TOWER,
        ARCHER_TOWER,

        // troops
        MINIPEKKA,
        GIANT,
        SKELETONS,
        SKELETON_ARMY,
        PEKKA,
        VALKYRIE,
        KNIGHT
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
