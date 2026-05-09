package jroyale.view.audio;

import java.util.EnumMap;
import java.util.Map;
import javafx.util.Duration;

public class AudioManager implements IAudioManager {

    private static AudioManager instance = null;

    private static Map<AudioType, Audio> audioBinder = new EnumMap<>(AudioType.class); 

    public enum AudioType {
        START_SOUND(true, "/jroyale/sfx/supercell_jingle.mp3"),
        LOADING_SOUND(true, "/jroyale/sfx/scroll_loading_01.mp3"),

        // ui
        MENU_CLICK(true, "/jroyale/sfx/menu_click_06.mp3"),

        // music
        MENU_MUSIC(false, "/jroyale/sfx/long_audios/menu_03.mp3"), 
        GAME_MUSIC_2MIN(false, "/jroyale/sfx/long_audios/2min_loop_battle_01.mp3"),

        // counting audios
        COUNT_10(true, "/jroyale/sfx/counters/10_cd_02.mp3"),
        COUNT_9 (true, "/jroyale/sfx/counters/9_cd_02.mp3"),
        COUNT_8 (true, "/jroyale/sfx/counters/8_cd_02.mp3"),
        COUNT_7 (true, "/jroyale/sfx/counters/7_cd_02.mp3"),
        COUNT_6 (true, "/jroyale/sfx/counters/6_cd_02.mp3"),
        COUNT_5 (true, "/jroyale/sfx/counters/5_cd_02.mp3"),
        COUNT_4 (true, "/jroyale/sfx/counters/4_cd_02.mp3"),
        COUNT_3 (true, "/jroyale/sfx/counters/3_cd_02.mp3"),
        COUNT_2 (true, "/jroyale/sfx/counters/2_cd_02.mp3"),
        COUNT_1 (true, "/jroyale/sfx/counters/1_cd_02.mp3"),
        ;

        private boolean fastPlayback;
        private String relativePath;

        private AudioType(boolean fastPlayback, String relativePath) {
            this.fastPlayback = fastPlayback;
            this.relativePath = relativePath;
        } 
    }

    private AudioManager() {}

    @Override
    public void loadAllAudio() {
        for (AudioType type : AudioType.values())
            loadAudio(type);
    }

    private void loadAudio(AudioType type) {
        audioBinder.put(type, new Audio(type.relativePath, type.fastPlayback));
    }
    
    @Override
    public void play(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) audio.play();
    }

    @Override
    public void stop(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) audio.stop();    
    }

    @Override
    public Duration getDuration(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) return audio.getDuration();

        return null;
    }

    @Override
    public boolean isAudioPlaying(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) return audio.isPlaying();

        return false;
    }

    @Override
    public void loop(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) audio.loop();
    }
    
    // static methods
    public static IAudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
}
