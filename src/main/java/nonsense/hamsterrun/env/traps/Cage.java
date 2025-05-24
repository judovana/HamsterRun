package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

//can be reached only once collected keys > number of rats * constant is reached.
//once reached, rats win
//hhmhm all rats must enter in limited time? Score?
public class Cage implements Item {

    int anim = seed.nextInt(10);

    public Color getMinimapColor() {
        return Color.orange;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 2) {
            g2d.drawImage(SpritesProvider.gate, coordx, coordy, zoom, zoom, null);
        }
    }

    public void drawHighlight(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 3) {
            anim++;
            if (anim > 10) {
                anim = 0;
            }
            g2d.setColor(Color.yellow);
            g2d.drawOval(coordx - anim, coordy - anim, zoom + 2 * anim, zoom + 2 * +anim);
        }
    }

    protected Image getSprite() {
        return SpritesProvider.gate;
    }

    @Override
    public void drawThumbnail(Graphics2D g2d, int size) {
        drawInto(g2d, 0, 0, size, 2, null, 0, 0);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToMoveQueue(SoundsBuffer.piskLong);
    }
}
