package nonsense.hamsterrun.env.traps;

import java.awt.Color;
import java.awt.Graphics2D;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.sprites.SpritesProvider;

//will swap you with other rat if possible
public class Mushroom implements Item {

    private static final float maxSize = 20;
    private int size = seed.nextInt(((int) maxSize - 10)) + 10;
    int anim = seed.nextInt(10);

    public Color getMinimapColor() {
        return Color.CYAN;
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToEatQueue(SoundsBuffer.eat);
    }


    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 3) {
            int finalSize = (int) ((float) size / maxSize * (float) zoom);
            anim++;
            if (anim>21){
                anim = 0;
            }
            g2d.drawImage(SpritesProvider.getMushroom(((anim/3)%4)), coordx + (zoom - finalSize) / 2, coordy + (zoom - finalSize) / 2, finalSize, finalSize, null);
        }
    }

}
