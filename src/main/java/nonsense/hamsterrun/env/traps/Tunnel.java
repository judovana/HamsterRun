package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Draws on second level, and if on cross road, the exit from it is random (on strigth, may go even back)
 */
public class Tunnel implements Item {

    public Color getMinimapColor() {
        return new Color(20, 20, 20);
    }

    public int getLevel() {
        return 3; //after rats
    }

    @Override
    //fixme honour neigbours
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, BaseBlockNeigbours neigbours) {
        g2d.setColor(getMinimapColor());
        g2d.fillRect(coordx, coordy, zoom, zoom);
    }

}
