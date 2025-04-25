package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;

//can be reached only once collected keys > number of rats * constant is reached.
//once reached, rats win
public class Cage implements Item {

    public Color getMinimapColor() {
        return Color.orange;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {

    }

    public void drawThumbnail(Graphics2D g2d, int size, int level) {

    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {

    }
}
