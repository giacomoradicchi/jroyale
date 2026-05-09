package jroyale.view.audio;

import java.util.EnumMap;
import java.util.Map;
import javafx.util.Duration;
import jroyale.model.troops.MiniPekka;

public class AudioManager implements IAudioManager {

    private static AudioManager instance = null;

    private static Map<AudioType, Audio> audioBinder = new EnumMap<>(AudioType.class); 

    private AudioType currentAudio;

    public enum AudioType {
        START_SOUND(true, "/jroyale/sfx/supercell_jingle.wav"),
        LOADING_SOUND(true, "/jroyale/sfx/scroll_loading_01.wav"),

        // ux menu
        MENU_CLICK(true, "/jroyale/sfx/menu_click_06.wav"),
        MENU_STARTGAME(true, "/jroyale/sfx/attack_button_01.wav"),

        // ux game
        GAME_SPELL_NOT_READY(true, "/jroyale/sfx/spell_not_ready_01.wav"),
        GAME_SPELL_CAST(true, "/jroyale/sfx/spellcast01.wav"),

        // sfx troops
        MINIPEKKA_MOVE(true, "/jroyale/sfx/mini_pekka/minipekka_step_03.wav"),
        MINIPEKKA_ATTACK(true, "/jroyale/sfx/mini_pekka/mini_pekka_atk_12.wav"),
        MINIPEKKA_HIT(true, "/jroyale/sfx/mini_pekka/mini_pekka_hit_03.wav"),

        // music
        MENU_MUSIC(false, "/jroyale/sfx/long_audios/menu_03.wav"), 
        GAME_MUSIC_2MIN(false, "/jroyale/sfx/long_audios/2min_loop_battle_01.wav"),
        GAME_MUSIC_60SEC_WARN(false, "/jroyale/sfx/long_audios/60_sec_warn_02_v2.wav"),
        GAME_MUSIC_60SEC(false, "/jroyale/sfx/long_audios/60_sec_music_loop_01.wav"),
        GAME_MUSIC_30SEC(false, "/jroyale/sfx/long_audios/30_sec_music_loop_01.wav"),
        SCROLL_DRAW(true, "/jroyale/sfx/long_audios/scroll_draw_01.wav"),
        SCROLL_LOSE(true, "/jroyale/sfx/long_audios/scroll_lose_01.wav"),
        SCROLL_WIN(true, "/jroyale/sfx/long_audios/scroll_win_02.wav"),

        // counting audios
        COUNT_10(true, "/jroyale/sfx/counters/10_cd_02.wav"),
        COUNT_9 (true, "/jroyale/sfx/counters/9_cd_02.wav"),
        COUNT_8 (true, "/jroyale/sfx/counters/8_cd_02.wav"),
        COUNT_7 (true, "/jroyale/sfx/counters/7_cd_02.wav"),
        COUNT_6 (true, "/jroyale/sfx/counters/6_cd_02.wav"),
        COUNT_5 (true, "/jroyale/sfx/counters/5_cd_02.wav"),
        COUNT_4 (true, "/jroyale/sfx/counters/4_cd_02.wav"),
        COUNT_3 (true, "/jroyale/sfx/counters/3_cd_02.wav"),
        COUNT_2 (true, "/jroyale/sfx/counters/2_cd_02.wav"),
        COUNT_1 (true, "/jroyale/sfx/counters/1_cd_02.wav"),
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
    public void setVolume(AudioType type, double volume) {
        Audio audio = audioBinder.get(type);
        if (audio != null) audio.setVolume(volume);  
    }

    @Override
    public Duration getDuration(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) return audio.getDuration();

        return null;
    }

    /* @Override
    public boolean isAudioPlaying(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) return audio.isPlaying();

        return false;
    } */

    @Override
    public void switchCurrentAudio(AudioType audio) {
        stopCurrentAudio();
        currentAudio = audio;
        play(currentAudio);
    }

    @Override
    public void stopCurrentAudio() {
        stop(currentAudio);
    }

    @Override
    public AudioType getCurrentAudio() {
        return currentAudio;
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
