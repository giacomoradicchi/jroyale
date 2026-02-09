package jroyale.shared;

public class Enums {

    public enum EntityType {
        // towers
        KING_TOWER,
        ARCHER_TOWER,

        // troops
        MINIPEKKA,
        GIANT,
        SKELETON,
        SKELETON_ARMY,
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
}
