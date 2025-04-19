package nonsense.hamsterrun.env;

import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class WavSoundPlayer {

    public static void rawPlayAsync(String what) {
        new Thread() {
            public void run() {
                WavSoundPlayer.rawPlayCatched(what);
            }
        }.start();
    }

    public static void rawPlayCatched(String what) {
        try {
            rawPlay(what);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rawPlay(String what) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        URL runUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sounds/effects/" + what + ".wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(runUrl.openStream());
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
}
