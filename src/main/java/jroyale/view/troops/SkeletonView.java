package jroyale.view.troops;

import java.util.HashMap;
import java.util.Map;

import jroyale.shared.Enums.State;

public class SkeletonView extends TroopView {
    
    public static final Map<State, Integer> NUM_FRAMES_PER_DIRECTION = getNumFramesPerDirection();

    private static SkeletonView instance;

    private SkeletonView() {
        super();
    }

    public static TroopView getInstance() {
        if (instance == null) {
            instance = new SkeletonView();
        }
        return instance;
    }

    private static Map<State, Integer> getNumFramesPerDirection() {
        // num of frames per direction change based on troop state (wheather is walking/running or attacking)
        Map<State, Integer> numFrames = new HashMap<>();

        numFrames.put(State.MOVE, 8);
        numFrames.put(State.IDLE, 1);
        numFrames.put(State.ATTACK, 4);

        return numFrames;
    }
}
