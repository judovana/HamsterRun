package nonsense.hamsterrun.ratcontroll;

public class Score {
    private final String skin;
    private final String id;
    private final long time;
    private final int score;
    private final long tscore;

    public Score(String skin, String id, long time, int score, long tscore) {
        this.skin = skin;
        this.id = id;
        this.time = time;
        this.score = score;
        this.tscore = tscore;
    }

    public int getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

    public long getTscore() {
        return tscore;
    }

    @Override
    public String toString() {
        return toTString("");
    }

    public String toTString(String prefix) {
        return prefix + "\\o/ " + skin + '/' + id + "at " + (time / 1000 / 60) + "minutes with score of" + score + "/" + tscore;
    }


}
