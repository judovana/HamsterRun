package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Vegetable implements Item {

    private static final float maxEnergy = 10;
    int energy = seed.nextInt(((int) maxEnergy - 2)) + 2;

    public Color getMinimapColor() {
        return Color.green;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, BaseBlockNeigbours neigbours) {
        int finalSize = (int) ((float) energy / maxEnergy * (float) zoom);
        g2d.drawImage(SpritesProvider.okurka, coordx + (zoom - finalSize) / 2, coordy + (zoom - finalSize) / 2, finalSize, finalSize, null);
    }

    public boolean eat() {
        energy--;
        return (energy <= 0);
    }
}
