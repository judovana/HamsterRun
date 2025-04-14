package nonsense.hamsterrun.env.traps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

//will try to turn rat to oposite direction and will be taking energy away
//burns - a bit, on neighbour fields
public class Fire implements Item {
    //will remove energy on passing through

    int anim = seed.nextInt(48);
    int anim2 = seed.nextInt(6);

    public Color getMinimapColor() {
        return Color.red;
    }


    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 2) {
            anim2++;
            if (anim2 > 5) {
                anim2 = 0;
            }
            g2d.drawImage(SpritesProvider.fire[anim2], coordx, coordy, zoom, zoom, null);
        }
        if (level == 3) {
            float opacity = 0.75f;
            anim++;
            if (anim > 49) {
                anim = 0;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(getSprite(anim / 10 + 1), coordx - zoom / 2, coordy - zoom / 2, zoom + zoom, zoom + zoom, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }

    protected BufferedImage getSprite(int id) {
        return SpritesProvider.glow[id];
    }
}
