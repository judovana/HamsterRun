package nonsense.hamsterrun.env.traps;

import java.awt.Color;
import java.awt.Graphics2D;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

//will try to turn rat to oposite direction and will be taking energy away
public class Fire implements Item {
    //will remove energy on passing through
    public Color getMinimapColor() {
        return Color.red;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours) {

    }

}
