package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;

public class InvisibleTrapDoor implements Item {
    protected boolean closed = true;

    //will NOT bound to exact coords. will always spit to random place
    //will regenerate the block if possible
    //if it is impossible to regenerate, maybe not fall through?
    //will remain stay
    //should lead to exact known stile spot?
    public Color getMinimapColor() {
        return new Color(200, 200, 200);
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, BaseBlockNeigbours neigbours) {
        g2d.setColor(new Color(20, 20, 20));
        if (closed) {
            g2d.drawLine(coordx, coordy, coordx + zoom, coordy + zoom);
            g2d.drawLine(coordx, coordy + zoom, coordx + zoom, coordy);
        } else {
            g2d.fillRect(coordx + zoom / 4, coordy + zoom / 4, zoom - zoom / 2, zoom - zoom / 2);
        }
    }

    public void close() {
        this.closed=true;
    }
    public void open() {
        this.closed=false;
    }
}
