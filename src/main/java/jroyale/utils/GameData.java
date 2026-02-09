package jroyale.utils; 

import jroyale.shared.Enums.State;

import jroyale.shared.Enums.EntityType;

import java.util.Map;

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