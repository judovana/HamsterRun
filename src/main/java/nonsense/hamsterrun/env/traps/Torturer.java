package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;

public class Torturer implements Item {
    //will remove energy on passing through
    public Color getMinimapColor() {
        return Color.green;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, BaseBlockNeigbours neigbours) {

    }

}
