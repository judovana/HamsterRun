package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;

import java.awt.Color;
import java.awt.Graphics2D;

public class InvisibleTrapDoor implements Item, Relocator {
    // will always lead to random space

    protected boolean closed = true;

    public static void randomAloneSpot(World world, Rat rat) {
        world.teleportMouse(rat, false, true);
    }

    public Color getMinimapColor() {
        return new Color(200, 200, 200);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToMoveQueue(SoundsBuffer.brbliFall);
    }


    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 2) {
            g2d.setColor(new Color(20, 20, 20));
            if (closed) {
                g2d.drawLine(coordx + zoom / 4, coordy + zoom / 4, coordx + zoom - zoom / 4, coordy + zoom - zoom / 4);
                g2d.drawLine(coordx + zoom / 4, coordy + zoom - zoom / 4, coordx + zoom - zoom / 4, coordy + zoom / 4);
                //g2d.drawLine(coordx, coordy + zoom, coordx + zoom, coordy);
            } else {
                g2d.fillRect(coordx + zoom / 4, coordy + zoom / 4, zoom - zoom / 2, zoom - zoom / 2);
            }
        }
    }

    @Override
    public void drawThumbnail(Graphics2D g2d, int size) {
        drawInto(g2d, 0, 0, size, 2, null, 0, 0);
    }

    public void close() {
        this.closed = true;
    }

    public void open() {
        this.closed = false;
    }

    @Override
    public void relocate(World world, Rat rat) {
        randomAloneSpot(world, rat);
    }
}
