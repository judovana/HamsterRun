package nonsense.hamsterrun.sprites;

import java.awt.image.BufferedImage;

public class RatSpriteSet {


    private final BufferedImage[][] sits;
    private final BufferedImage[][] eats;
    private final BufferedImage[][] runs;
    private final BufferedImage[][] falls;

    public RatSpriteSet(BufferedImage[] run, BufferedImage sit1, BufferedImage[] fall, BufferedImage eat) {
        sits = rotateImages(sit1);
        runs = rotateImages(run);
        falls = rotateImages(fall);
        if (eat == null) {
            eats = sits;
        } else {
            eats = rotateImages(eat);
        }
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
            runRight[x] = SpritesProvider.rotate(runUp[x], 90);
            runDown[x] = SpritesProvider.rotate(runUp[x], 180);
            runLeft[x] = SpritesProvider.rotate(runUp[x], 270);
        }
        runs = new BufferedImage[][]{runUp, runRight, runDown, runLeft};
        return runs;
    }

    public BufferedImage getSit(int direction, int anim) {
        return sits[direction][anim];
    }

    public BufferedImage getEat(int direction, int anim) {
        return eats[direction][anim];
    }

    public BufferedImage getRun(int direction, int anim) {
        return runs[direction][anim];
    }

    public BufferedImage getFall(int direction, int anim) {
        return falls[direction][anim];
    }

    public int getRuns() {
        return runs[0].length;
    }

    public int getFalls() {
        return falls[0].length;
    }
}
