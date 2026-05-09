package jroyale.view.audio;

import javafx.util.Duration;
import jroyale.view.audio.AudioManager.AudioType;

public interface IAudioManager {

    public void loadAllAudio();
    
    public void play(AudioType audio);

    public void stop(AudioType audio);

    public void setVolume(AudioType audio, double volume);

    public Duration getDuration(AudioType audio);

    public void switchCurrentAudio(AudioType audio);

    public void stopCurrentAudio();

    public AudioType getCurrentAudio();

    public void loop(AudioType type);
     
    // static methods
    public static IAudioManager getInstance() {
        return AudioManager.getInstance();
    }
}
