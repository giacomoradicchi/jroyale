package jroyale.view.audio;

import java.util.EnumMap;
import java.util.Map;

import jroyale.view.audio.Audio.AudioPlayer;

public class AudioManager implements IAudioManager {

    private static AudioManager instance = null;

    private static Map<AudioType, AudioPlayer> audioBinder = new EnumMap<>(AudioType.class); 

    public enum AudioType {
        START_SOUND,
        LOADING_SOUND
    }

    private AudioManager() {
        //private AudioClip startSound = new AudioClip(getClass().getResource("/jroyale/sfx/supercell_jingle.mp3").toExternalForm());
        //private AudioClip loadingSound = new AudioClip(getClass().getResource("/jroyale/sfx/scroll_loading_01.mp3").toExternalForm());
        //private MediaPlayer loadingPlayer = new MediaPlayer(new Media(loadingSound.getSource()));
        //audioBinder.put(START_SOUND, )
    }
    
    @Override
    public void play(AudioType type) {
        AudioPlayer player = audioBinder.get(type);
        if (player != null) player.play();
    }
    
    // static methods
    public static IAudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
}
