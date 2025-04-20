package nonsense.hamsterrun.env;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.List;

public class SoundsBuffer {

    private static class SoundQueue implements Runnable {
        List<String> queue = new ArrayList<>(10);
        WavSoundPlayer player = null;

        public SoundQueue() {
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(50);
                    String what = peak();
                    if (what == null) {
                        continue;
                    } else {
                        if (player == null || player.isFinished()) {
                            //if there is no playback, item in peak is played
                            player = new WavSoundPlayer(dequeue());
                            player.rawPlayAsync();
                        } else {
                            //if player already plays whats in queue, it is removed
                            if (player.getWhat().equals(what)){
                                dequeue();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void enqueue(String s) {
            System.out.println(s +" " + queue.size());
            queue.add(s);
        }

        private String dequeue() {
            if (queue.size() > 0) {
                String member = queue.get(0);
                queue.remove(0);
                return member;
            } else {
                return null;
            }
        }

        private String peak() {
            if (queue.size() > 0) {
                String member = queue.get(0);
                return member;
            } else {
                return null;
            }
        }
    }

    public static final String turbo = "turbo";
    public static final String eat = "chroup";
    public static final String piskMuch = "pisk1";
    public static final String piskChr = "pisk3";
    public static final String piskLong = "pisk5";
    public static final String brbliFall = "brblibrbli2";
    public static final String brbliTele = "brblibrbli3";
    public static final String brbliFire = "brblibrbli1";
    public static final String piskFire = "pisk2";
    public static final String brbliTunel = "brblibrbli4";


    private final SoundQueue moveQueue = new SoundQueue();
    private final SoundQueue eatQueue = new SoundQueue();
    private final SoundQueue harmQueue = new SoundQueue();

    public void addToMoveQueue(String clip) {
        moveQueue.enqueue(clip);
    }

    public void addToEatQueue(String clip) {
        eatQueue.enqueue(clip);
    }

    public void addHarmQueue(String clip) {
        harmQueue.enqueue(clip);
    }
}
