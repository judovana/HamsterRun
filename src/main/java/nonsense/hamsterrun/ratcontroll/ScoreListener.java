package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class ScoreListener {

    private class Blinker extends Thread {

        Color color = getDefaultColor();
        int counter = 0;

        public Blinker() {
            this.setDaemon(true);
            this.start();
        }

        public void setColor(Color color) {
            this.color = color;
            counter = 9;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (counter % 2 == 1) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                label.setBackground(color);
                                label.repaint();
                            }
                        });
                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                label.setBackground(getDefaultColor());
                                label.repaint();
                            }
                        });
                    }
                    counter--;
                    if (counter < 0) {
                        counter = 0;
                    }
                    Thread.sleep(100);
                } catch (Exception ex) {

                }
            }
        }
    }

    private static Color getDefaultColor() {
        return Color.lightGray;
    }

    private final JLabel label;
    private int lastScore = 0;
    private final Blinker blinker = new Blinker();


    public ScoreListener(JLabel scoreShow, Rat rat) {
        this.label = scoreShow;
        this.label.setOpaque(true);
    }

    public void report(Rat rat, int score) {
        if (lastScore < score) {
            blinker.setColor(Color.green);
        } else if (lastScore > score) {
            blinker.setColor(Color.red);
        } else {
            blinker.setColor(getDefaultColor());
        }
        lastScore = score;
        label.setText(rat.getSkin() + ": " + score);
    }
}
