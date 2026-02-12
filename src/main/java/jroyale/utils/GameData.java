package jroyale.utils; 

import java.util.Map;

import jroyale.utils.Enums.EntityType;
import jroyale.utils.Enums.State;

public final class GameData {

    private static GameData instance = null;

    private GameData() {}

    private static final Map<EntityType, Map<State, Integer>> ANIMATIONS =
            Map.of(
                EntityType.MINIPEKKA, Map.of(
                        State.MOVE, 12,
                        State.ATTACK, 10,
                        State.IDLE, 1
                ),
                EntityType.GIANT, Map.of(
                        State.MOVE, 16,
                        State.ATTACK, 10,
                        State.IDLE, 1
                ),
                EntityType.SKELETON, Map.of(
                        State.MOVE, 8,
                        State.ATTACK, 4,
                        State.IDLE, 1
                ),
                EntityType.PEKKA, Map.of(
                        State.MOVE, 14,
                        State.ATTACK, 7,
                        State.IDLE, 1
                )
            );

    // Metodo pubblico per ottenere i dati
    public Map<State, Integer> getAnimationSteps(EntityType type) {
        return ANIMATIONS.getOrDefault(type, Map.of(State.IDLE, 1));
    }

    public static GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }
}