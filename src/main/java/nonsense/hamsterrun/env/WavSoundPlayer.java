package nonsense.hamsterrun.env;

import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class WavSoundPlayer {

    private final String what;
    private boolean finished = false;

    public WavSoundPlayer(String what) {
        this.what = what;
    }

    public static Clip rawPlayCatched(String what) {
        try {
            return rawPlay(what);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    public static Clip rawPlay(String what) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        return WavSoundPlayer.rawPlay(what, 1f);
    }

    public static Clip rawPlay(String what, float volume) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        URL runUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sounds/effects/" + what + ".wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(runUrl.openStream());
        Clip clip = AudioSystem.getClip();
//        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
//        float range = gainControl.getMaximum() - gainControl.getMinimum();
//        float gain = (range * volume) + gainControl.getMinimum();
//        gainControl.setValue(gain);
        clip.open(audioStream);
        clip.start();//oh, this is actually unblocking....
        return clip;
    }

    public static void main(String[] args) throws Exception {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        System.out.println("There are " + mixers.length + " mixer info objects");
        for (Mixer.Info mixerInfo : mixers) {
            System.out.println("mixer name: " + mixerInfo.getName());
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                System.out.println("  Line.Info: " + lineInfo);
            }
        }

        WavSoundPlayer w = new WavSoundPlayer("chroup");
        w.rawPlayAsync();
        while (!w.finished) {
            Thread.sleep(50);
            System.out.println("Waiting?");
        }

    }

    public boolean isFinished() {
        return finished;
    }

    public String getWhat() {
        return what;
    }

    public void rawPlayAsync() {
        new Thread() {
            public void run() {
                try {
                    Clip clip = WavSoundPlayer.rawPlay(what);
                    Thread.sleep(clip.getMicrosecondLength() / 1000); // Convert
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finished = true;
                }
            }
        }.start();
    }
}
