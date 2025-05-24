package nonsense.hamsterrun.ratcontroll;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class HighScore {
    private final List<Score> scores = new ArrayList();

    public void add(Score score) {
        this.scores.add(score);
    }

    public void draw(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        int h = g2d.getFontMetrics().getHeight();
        int y = 0;
        long sum = 0;
        long maxSum = -1;
        long maxScore = -1;
        long maxTime = -1;
        for (Score score : scores) {
            y += 1;
            sum += score.getScore();
            g2d.drawString(score.toTString("" + y), 10, (2 * y) * h);
            maxSum = Math.max(maxSum, score.getTscore());
            maxScore = Math.max(maxScore, score.getScore());
            maxTime = Math.max(maxTime, score.getTime());
        }
        y++;
        g2d.drawString("final: " + sum + "/" + maxSum, 10, (2 * y) * h);
        y++;
        g2d.drawString("at : " + maxTime / 1000 / 60 + "minutes", 10, (2 * y) * h);
        y++;
        g2d.drawString("and with most individual socre : " + maxScore, 10, (2 * y) * h);
    }
}
