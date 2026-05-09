package jroyale.view.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Audio {

    private AudioPlayer player;

    private interface AudioPlayer {

        public void play();

        public void stop();

        public void setVolume(double volume);

        public Duration getDuration();

        public boolean isPlaying();
        
        public void loop();
    }

    private class FastAudioPlayer implements AudioPlayer { // for short audios that have to be played fast (loads it in RAM)

        private AudioClip sound;

        public FastAudioPlayer(String URL) {
            this.sound = new AudioClip(URL);
        }

        @Override
        public void play() {
            sound.play();
        }

        @Override
        public void stop() {
            sound.stop();
        }

        @Override
        public void setVolume(double volume) {
            sound.setVolume(volume);
        }

        @Override
        public Duration getDuration() {
            throw new IllegalAccessError("Fast Audios doesn't have duration method");
        }

        @Override
        public boolean isPlaying() {
            return sound.isPlaying();
        }

        @Override
        public void loop() {
            throw new IllegalAccessError("cannot loop Fast Audios");
        }
    }

    private class LargeAudioPlayer implements AudioPlayer { // for long duration audios (i.e. background music)

        private MediaPlayer sound;

        public LargeAudioPlayer(String URL) {
            this.sound = new MediaPlayer(new Media(URL));
            this.sound.setOnEndOfMedia(() -> sound.stop()); // starts from 0 when is completed
        }

        @Override
        public void play() {
            sound.play();
        }

        @Override
        public void stop() {
            sound.stop();
        }

        @Override
        public void setVolume(double volume) {
            sound.setVolume(volume);
        }

        @Override
        public Duration getDuration() {
            return sound.getTotalDuration();
        }

        @Override
        public boolean isPlaying() {
            return sound.getCurrentTime().toMillis() < sound.getTotalDuration().toMillis();
        }

        @Override
        public void loop() {
            sound.setCycleCount(MediaPlayer.INDEFINITE);
        }

    }

    public Audio(String audioName, boolean fastPlayback) {
        String URL = getClass().getResource(audioName).toExternalForm();
        
        if (fastPlayback) player = new FastAudioPlayer(URL);
        else this.player = new LargeAudioPlayer(URL);
    }

    public void play() {
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public void setVolume(double volume) {
        player.setVolume(volume);
    }

    public Duration getDuration() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void loop() {
        player.loop();
    }

}
