package jroyale.view.audio;

import javafx.util.Duration;
import jroyale.view.audio.AudioManager.AudioType;

public interface IAudioManager {

    public void loadAllAudio();
    
    public void play(AudioType audio);

    public void stop(AudioType audio);

    public Duration getDuration(AudioType audio);

    public boolean isAudioPlaying(AudioType type);

    public void loop(AudioType type);
     
    // static methods
    public static IAudioManager getInstance() {
        return AudioManager.getInstance();
    }
}
