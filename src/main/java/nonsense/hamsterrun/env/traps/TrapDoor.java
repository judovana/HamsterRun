package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;

public class TrapDoor extends InvisibleTrapDoor implements Item {

    private int type = seed.nextInt(2);

    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours) {
        if (level == 2) {
            if (closed) {
                g2d.drawImage(SpritesProvider.trapdoor[type], coordx + zoom / 4, coordy + zoom / 4, zoom - zoom / 2, zoom - zoom / 2,
                        null);
            } else {
                g2d.setColor(new Color(20, 20, 20));
                g2d.fillRect(coordx + zoom / 4, coordy + zoom / 4, zoom - zoom / 2, zoom - zoom / 2);
            }
        }
    }
}
