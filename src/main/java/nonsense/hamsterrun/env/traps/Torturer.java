package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//most ikely more shapes as in trapdoor
public class Torturer implements Item {
    //will remove energy on passing through
    // will remove more when speed is high

    int anim = seed.nextInt(20);

    public Color getMinimapColor() {
        return Color.red;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int xx, int yy) {
        if (level == 2) {
            anim++;
            if (anim > 20) {
                anim = 0;
            }
        }
        double dz = (double) zoom;
        for (double x = 0.0; x < 1; x = x + 0.68) {
            for (double y = 0.0; y < 1; y = y + 0.68) {
                if (level == 3) {
                    g2d.drawImage(SpritesProvider.whirlStay[anim / 10 % 2], coordx + (int) (dz * x), coordy + (int) (dz * y), zoom / 3, zoom / 3, null);
                }
                if (level == 2) {
                    g2d.drawImage(getSprite(), coordx + (int) (dz * x), coordy + (int) (dz * y), zoom / 3, zoom / 3, null);
                }
            }
        }
    }

    protected BufferedImage getSprite() {
        return SpritesProvider.whirlMove[anim % 2];
    }
}
