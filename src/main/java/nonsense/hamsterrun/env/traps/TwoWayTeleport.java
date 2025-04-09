package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoWayTeleport implements Item, Relocator {
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

    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getTwoWayTeleport(id);
    }

    @Override
    public void relocate(World world, Rat rat) {
        List<Point> twoWayTeleports = new ArrayList<>();
        for (int x = 0; x < BaseConfig.getConfig().getGridSize() * BaseConfig.getConfig().getBaseSize(); x++) {
            for (int y = 0; y < BaseConfig.getConfig().getGridSize() * BaseConfig.getConfig().getBaseSize(); y++) {
                Item item = world.getBlockField(new Point(x, y)).getItem();
                if (item.getClass().equals(TwoWayTeleport.class)) {
                    System.out.println("Found teleport at " + x + ":" + y + "; rat is at " + rat.getUniversalCoords());
                    twoWayTeleports.add(new Point(x, y));
                }
            }
        }
        if (twoWayTeleports.isEmpty()) {
            System.out.println("No destination!");
            return;
        }
        Collections.shuffle(twoWayTeleports);
        Point w = twoWayTeleports.get(0);
        System.out.println("Moving from " + rat.getUniversalCoords() + " to " + w);
        BaseBlockNeigbours neighBase = world.getBaseBlockNeigboursByUniversal(w);

        List<Point> freeSidePoints = new ArrayList<>(4);
        //FIXME this is buggy. A coordinates must be returned with uf, otherwise it is highly imprecisse
        BlockField uf = neighBase.getUpField(w.x % BaseConfig.getConfig().getBaseSize(), w.y % BaseConfig.getConfig().getBaseSize());
        if (uf != null && uf.isPassable()) {
            freeSidePoints.add(new Point(w.x, w.y - 1));
        }

        if (freeSidePoints.isEmpty()){
            rat.setUniversalCoords(w);
        } else {
            Collections.shuffle(freeSidePoints);
            rat.setUniversalCoords(freeSidePoints.get(0));
        }
    }
}