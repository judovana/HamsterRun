package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Teleport implements Item, Relocator {
    //will drop the mouse to unassigned  gate. the gate will beocme assigned
    //it MUST drop it NEXT to it, otherwise it will cause endless loop
    //if all gates are assigned, then to random one

    int anim = seed.nextInt(48);

    public Color getMinimapColor() {
        return new Color(255, 192, 203);
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 2) {
            g2d.drawImage(getSprite(0), coordx, coordy, zoom, zoom, null);
        }
        if (level == 3) {
            float opacity = 0.75f;
            anim++;
            if (anim > 49) {
                anim = 0;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(getSprite(anim / 10 + 1), coordx - zoom / 2, coordy - zoom / 2, zoom + zoom, zoom + zoom, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }

    abstract protected BufferedImage getSprite(int id);

    protected static List<Point> getNeighboursForGivenPoint(Point w, World world) {
        BaseBlockNeigbours neighBase = world.getBaseBlockNeigboursByUniversal(w.x, w.y);
        System.out.print(neighBase.toString());
        System.out.println();
        //up to now it is correct. the rat is moving between telepors and never misses
        //also the neighbrs are corrct and w is correct and twoWayTeleports are correct
        List<Point> freeSidePoints = new ArrayList<>(4);
        int xx = w.y % BaseConfig.getConfig().getBaseSize();
        int yy = w.x % BaseConfig.getConfig().getBaseSize();
        List<BlockField> possibleFields = new ArrayList<>(
                Arrays.asList(
                        neighBase.getRightField(xx, yy),
                        neighBase.getLeftField(xx, yy),
                        neighBase.getDownField(xx, yy),
                        neighBase.getUpField(xx, yy)));
        for (BlockField block : possibleFields) {
            if (block != null && block.isPassable()) {
                //they were recovered in swap coord system, we ahve to turn them back
                //so they can be swapped again
                freeSidePoints.add(new Point(block.getUniversalCoords().y, block.getUniversalCoords().x));
            }
        }
        return freeSidePoints;
    }

    protected static void moveRatTo(World world, Rat rat, Point w) {
        System.out.println("Moving from " + rat.getUniversalCoords() + " next to " + w);
        List<Point> freeSidePoints = getNeighboursForGivenPoint(w, world);
        if (freeSidePoints.isEmpty()) {
            rat.setUniversalCoords(w);
        } else {
            Collections.shuffle(freeSidePoints);
            rat.setUniversalCoords(freeSidePoints.get(0));
        }
        System.out.println("And it is: " + rat.getUniversalCoords());
        System.out.println();
    }

}