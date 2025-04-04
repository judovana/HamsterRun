package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;

public class OneWayTeleport implements Item {
    //wil bound to exact coords as gateway, if they exists. if not, will find new one
    //will continue walking
    //should lead to random spot?
    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours) {

    }

}
