package jroyale.view.audio;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.util.Duration;

public class AudioManager implements IAudioManager {

    private static AudioManager instance = null;

    private static Map<AudioType, Audio> audioBinder = new EnumMap<>(AudioType.class); 

    private AudioType currentAudio;

    private static final Map<AudioType, Long> lastPlayTime = new EnumMap<>(AudioType.class);
    private static final long COOLDOWN_MS = 100; // non risuonare lo stesso audio per 100ms


    // audio thread pool
    private final ExecutorService audioExecutor = Executors.newCachedThreadPool();

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
        MINIPEKKA_DEPLOY(true, "/jroyale/sfx/mini_pekka/minipekka_deploy_01.wav"),
        MINIPEKKA_DEPLOY_END(true, "/jroyale/sfx/mini_pekka/mini_pekka_deploy_end_06.wav"),

        GIANT_MOVE(true, "/jroyale/sfx/Giant/demon_step_01.wav"),
        GIANT_ATTACK_1(true, "/jroyale/sfx/Giant/giant_attack_swing_01.wav"),
        GIANT_ATTACK_2(true, "/jroyale/sfx/Giant/giant_attack_swing_02.wav"),
        GIANT_ATTACK_3(true, "/jroyale/sfx/Giant/giant_attack_swing_03.wav"),
        GIANT_HIT(true, "/jroyale/sfx/Giant/giant_hit_01.wav"),
        GIANT_DEPLOY(true, "/jroyale/sfx/Giant/giant_deploy_01.wav"),

        PEKKA_MOVE(true, "/jroyale/sfx/PEKKA/pekka_footstep_02.wav"),
        PEKKA_ATTACK(true, "/jroyale/sfx/PEKKA/pekka_atk_01.wav"),
        PEKKA_HIT(true, "/jroyale/sfx/PEKKA/pekka_attack_hit_03.wav"),
        PEKKA_DEPLOY(true, "/jroyale/sfx/PEKKA/pekka_deploy_01.wav"),
        PEKKA_DEPLOY_END(true, "/jroyale/sfx/PEKKA/pekka_deploy_end_03.wav"),

        SEKELTONS_MOVE(true, "/jroyale/sfx/Skeletons/skeleton_step_02.wav"),
        SEKELTONS_ATTACK(true, "/jroyale/sfx/Skeletons/skeleton_atk_03.wav"),
        SEKELTONS_DEPLOY(true, "/jroyale/sfx/Skeletons/skeleton_deploy_03.wav"),

        VALKYRIE_MOVE(true, "/jroyale/sfx/Valkyrie/valk_step_02.wav"),
        VALKYRIE_ATTACK_1(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_04.wav"),
        VALKYRIE_ATTACK_2(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_05.wav"),
        VALKYRIE_ATTACK_3(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_06.wav"),
        VALKYRIE_ATTACK_4(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_07.wav"),
        VALKYRIE_ATTACK_5(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_08.wav"),
        VALKYRIE_ATTACK_6(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_09.wav"),
        VALKYRIE_ATTACK_7(true, "/jroyale/sfx/Valkyrie/valkyrie_atk_10.wav"),
        VALKYRIE_HIT(true, "/jroyale/sfx/Valkyrie/valkyrie_hit_01.wav"),
        VALKYRIE_DEPLOY(true, "/jroyale/sfx/Valkyrie/valkyrie_deploy_02.wav"),
        VALKYRIE_DEPLOY_END(true, "/jroyale/sfx/Valkyrie/valkyrie_deploy_end_02.wav"),

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
        if (audio == null) return;

        long now = System.currentTimeMillis();
        Long last = lastPlayTime.get(type);

        // if not found or it's too early, skip
        if (last != null && (now - last) < COOLDOWN_MS) return; 

        // otherwise, add it to lastPlayTime list and play
        lastPlayTime.put(type, now);

        audioExecutor.submit(() -> audio.play());
    }  

    @Override
    public void stop(AudioType type) {
        Audio audio = audioBinder.get(type);
        if (audio != null) 
            audioExecutor.submit(() -> {
                audio.stop();
            }); 
    }

    @Override
    public void setVolume(AudioType type, double volume) {
        Audio audio = audioBinder.get(type);
        if (audio != null)
            audioExecutor.submit(() -> {
                audio.setVolume(volume);
            });
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
        if (audio != null) 
            audioExecutor.submit(() -> {
                audio.loop();
            });
    }

    @Override
    public void resetCurrentAudio() {
        this.currentAudio = null;
    }
    
    @Override
    public void shutdown() {
        audioExecutor.shutdown();
    }

    // static methods
    public static IAudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
}
