package nonsense.hamsterrun.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Array;

public class SpriteSet {


    private final BufferedImage[][] sits;
    private final BufferedImage[][] runs;

    public SpriteSet(BufferedImage run1, BufferedImage run2, BufferedImage sit1) {
        sits = rotateImages(sit1);
        runs = rotateImages(run1, run2);
    }

    private BufferedImage[][] rotateImages(BufferedImage... run) {
        final BufferedImage[][] runs;
        BufferedImage[] runUp;
        BufferedImage[] runRight;
        BufferedImage[] runDown;
        BufferedImage[] runLeft;
        runUp = run;
        runRight = new BufferedImage[runUp.length];
        runDown = new BufferedImage[runUp.length];
        runLeft = new BufferedImage[runUp.length];
        for (int x = 0; x < runUp.length; x++) {
            runRight[x] = rotate(runUp[x], 90);
            runDown[x] = rotate(runUp[x], 180);
            runLeft[x] = rotate(runUp[x], 270);
        }
        runs = new BufferedImage[][]{
                runUp, runRight, runDown, runLeft
        };
        return runs;
    }

    public BufferedImage getSit(int direction, int anim) {
        return sits[direction][anim];
    }

    public BufferedImage getRun(int direction, int anim) {
        return runs[direction][anim];
    }

    public static BufferedImage rotate(BufferedImage img, double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int neww = (int) Math.floor(w * cos + h * sin);
        int newh = (int) Math.floor(h * cos + w * sin);

        BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimg.createGraphics();

        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(img, null);
        g.dispose();
        return bimg;
    }
}
