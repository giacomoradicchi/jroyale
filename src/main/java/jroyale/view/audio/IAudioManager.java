package jroyale.view.audio;

import jroyale.view.audio.AudioManager.AudioType;

public interface IAudioManager {
    
    public void play(AudioType audio);
     
    // static methods
    public static IAudioManager getInstance() {
        return AudioManager.getInstance();
    }
}
