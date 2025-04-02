package nonsense.hamsterrun.sprites;

import java.awt.image.BufferedImage;
import nonsense.hamsterrun.sprites.SpritesProvider;

public class RatSpriteSet {


    private final BufferedImage[][] sits;
    private final BufferedImage[][] runs;

    public RatSpriteSet(BufferedImage run1, BufferedImage run2, BufferedImage sit1) {
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
            runRight[x] = SpritesProvider.rotate(runUp[x], 90);
            runDown[x] = SpritesProvider.rotate(runUp[x], 180);
            runLeft[x] = SpritesProvider.rotate(runUp[x], 270);
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

}
