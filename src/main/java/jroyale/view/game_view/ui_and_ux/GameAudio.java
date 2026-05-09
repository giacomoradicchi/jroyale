package jroyale.view.game_view.ui_and_ux;

import jroyale.view.audio.AudioManager.AudioType;
import jroyale.view.audio.IAudioManager;

public class GameAudio {

    private static GameAudio instance = null;

    private static final int RUNNING_OUT_TIME_LIMIT_IN_SEC = 60;
    private static final int RUNNING_OUT_TIME_DANGER_LIMIT_IN_SEC = 30;

    private int currentSecondsLeft;
    private IAudioManager audioManager = IAudioManager.getInstance();

    private GameAudio() {}

    public void handleGameAudio(int secondsLeft) {
        if (currentSecondsLeft == secondsLeft) return;

        // new second to handle
        currentSecondsLeft = secondsLeft;

        if (currentSecondsLeft > RUNNING_OUT_TIME_LIMIT_IN_SEC && audioManager.getCurrentAudio() != AudioType.GAME_MUSIC_2MIN) {
            audioManager.switchCurrentAudio(AudioType.GAME_MUSIC_2MIN);
        }

        switch (currentSecondsLeft) {
            case RUNNING_OUT_TIME_LIMIT_IN_SEC:
                audioManager.play(AudioType.GAME_MUSIC_60SEC_WARN);
                audioManager.switchCurrentAudio(AudioType.GAME_MUSIC_60SEC);
                break;
            
            case RUNNING_OUT_TIME_DANGER_LIMIT_IN_SEC:
                audioManager.switchCurrentAudio(AudioType.GAME_MUSIC_30SEC);

            // count down 
            case 10:
                audioManager.play(AudioType.COUNT_10);
                break;
            case 9:
                audioManager.play(AudioType.COUNT_9);
                break;
            case 8:
                audioManager.play(AudioType.COUNT_8);
                break;
            case 7:
                audioManager.play(AudioType.COUNT_7);
                break;
            case 6:
                audioManager.play(AudioType.COUNT_6);
                break;
            case 5:
                audioManager.play(AudioType.COUNT_5);
                break;
            case 4:
                audioManager.play(AudioType.COUNT_4);
                break;
            case 3:
                audioManager.play(AudioType.COUNT_3);
                break;
            case 2:
                audioManager.play(AudioType.COUNT_2);
                break;
            case 1:
                audioManager.play(AudioType.COUNT_1);
                break;
        
            default:
                break;
        }
    }

    // static methods
    public static GameAudio getInstance() {
        if (instance == null) {
            instance = new GameAudio();
        }

        return instance;
    }
}
