package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;

public class Empty implements Item {

    public Color getMinimapColor() {
        return Color.white;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {

    }

    public void drawThumbnail(Graphics2D g2d, int size, int level) {
        g2d.drawImage(SpritesProvider.getFloor(5), 0, 0, size, size, null);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {

    }
}
