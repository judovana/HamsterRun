package nonsense.hamsterrun.env;

import java.util.ArrayList;
import java.util.List;

public class SoundsBuffer {

    public static final String turbo = "turbo";
    public static final String eat = "chroup";
    public static final String piskMuch = "pisk1";
    public static final String piskChr = "pisk3";
    public static final String piskLong = "pisk5";
    public static final String piskWeird = "pisk4";
    public static final String brbliFall = "brblibrbli2";
    public static final String brbliTele = "brblibrbli3";
    public static final String brbliFire = "brblibrbli1";
    public static final String piskFire = "pisk2";
    public static final String brbliTunel = "brblibrbli4";
    private final SoundQueue moveQueue = createSoundQueue();
    private final SoundQueue eatQueue = createSoundQueue();
    private final SoundQueue harmQueue = createSoundQueue();

    protected SoundQueue createSoundQueue() {
        return new SoundQueue();
    }

    public void addToMoveQueue(String clip) {
        moveQueue.enqueue(clip);
    }

    public void addToEatQueue(String clip) {
        eatQueue.enqueue(clip);
    }

    public void addToHarmQueue(String clip) {
        harmQueue.enqueue(clip);
    }

    public void kill() {
        if (harmQueue!=null) {
            harmQueue.kill();
        }
        if (eatQueue!=null) {
            eatQueue.kill();
        }
        if (moveQueue!=null) {
            moveQueue.kill();
        }

    }

    private static class SoundQueue implements Runnable {
        private static int id;
        private static int LIMIT = 15;
        List<String> queue = new ArrayList<>(10);
        WavSoundPlayer player = null;
        private boolean live = true;

        public SoundQueue() {
            id++;
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.setName("Sound queue " + id);
            t.start();
        }

        @Override
        public void run() {
            while (live) {
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
                            if (player.getWhat().equals(what)) {
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
            if (queue.size() > LIMIT) {
                queue.clear();
            }
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

        public void kill() {
            live=false;
        }
    }

    public static class NoSound extends SoundsBuffer {
        protected SoundQueue createSoundQueue() {
            return null;
        }

        public void addToMoveQueue(String clip) {

        }

        public void addToEatQueue(String clip) {

        }

        public void addToHarmQueue(String clip) {

        }
    }
}
