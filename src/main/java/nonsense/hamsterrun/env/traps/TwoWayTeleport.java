package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;

public class TwoWayTeleport implements Item {
    //wil bound to exact coords as gateway, if they exists. if not, will find new one
    //will create back door on landing
    //they will be defunct for some short time
    //will continue walking
    //must lead to output gate
    public Color getMinimapColor() {
        return new Color(255, 230, 0);
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours) {

    }
}
