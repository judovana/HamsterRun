package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Vegetable implements Item {

    private static final float maxEnergy = 10;
    int energy = seed.nextInt(((int) maxEnergy - 2)) + 2;

    public Color getMinimapColor() {
        return Color.green;
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToEatQueue(SoundsBuffer.eat);
    }


    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 2) {
            int finalSize = (int) ((float) energy / maxEnergy * (float) zoom);
            g2d.drawImage(getSprite(), coordx + (zoom - finalSize) / 2, coordy + (zoom - finalSize) / 2, finalSize, finalSize, null);
        }
    }

    @Override
    public void drawThumbnail(Graphics2D g2d, int size) {
        drawInto(g2d, 0, 0, size, 2, null, 0, 0);
    }

    protected abstract Image getSprite();

    abstract public int eat(Rat eater, World world);

    public boolean eaten() {
        return (energy <= 0);
    }
}
