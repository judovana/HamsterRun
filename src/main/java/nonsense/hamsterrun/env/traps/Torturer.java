package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;

//most ikely more shapes as in trapdoor
public class Torturer implements Item {
    //will remove energy on passing through
    public Color getMinimapColor() {
        return Color.red;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours) {

    }

}
