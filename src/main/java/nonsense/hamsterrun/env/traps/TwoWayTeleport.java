package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoWayTeleport extends Teleport {

    private static final boolean disableSelfDirection = true;

    //will drop the mouse to unassigned  gate. the gate will become assigned
    //it MUST drop it NEXT to it, otherwise it will cause endless loop
    //if all gates are assigned, then to random one?

    private Point targetGate;

    private static List<Point> findTeleportWithoutGate(List<Point> twoWayTeleports, World world) throws IOException {
        Collections.shuffle(twoWayTeleports);
        List<Point> realTwoWayTeleports = new ArrayList<>(twoWayTeleports.size());
        for (Point point : twoWayTeleports) {
            Item item = world.getBlockField(point).getItem();
            if (item instanceof TwoWayTeleport) {
                realTwoWayTeleports.add(point);
            }
        }
        if (realTwoWayTeleports.isEmpty()) {
            throw new IOException("No two way tleports in world!");
        }
        List<Point> unallocatedTwoWayTeleports = new ArrayList<>(twoWayTeleports.size());
        for (Point point : realTwoWayTeleports) {
            TwoWayTeleport item = (TwoWayTeleport) world.getBlockField(point).getItem();
            if (item.targetGate == null) {
                unallocatedTwoWayTeleports.add(point);
            }
        }
        if (unallocatedTwoWayTeleports.isEmpty()) {
            ((TwoWayTeleport) world.getBlockField(realTwoWayTeleports.get(0)).getItem()).targetGate = null;
            unallocatedTwoWayTeleports.add(realTwoWayTeleports.get(0));
        }
        return unallocatedTwoWayTeleports;
    }

    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getTwoWayTeleport(id);
    }

    @Override
    public void relocate(World world, Rat rat) {
        List<Point> twoWayTeleports = new ArrayList<>();
        for (int x = 0; x < BaseConfig.getConfig().getGridSize() * BaseConfig.getConfig().getBaseSize(); x++) {
            for (int y = 0; y < BaseConfig.getConfig().getGridSize() * BaseConfig.getConfig().getBaseSize(); y++) {
                Item item = world.getBlockField(new Point(x, y)).getItem();
                if (item instanceof TwoWayTeleport) {
                    System.out.println("Found teleport at " + x + ":" + y + "; rat is at " + rat.getUniversalCoords());
                    twoWayTeleports.add(new Point(x, y));
                }
            }
        }
        if (twoWayTeleports.isEmpty()) {
            System.out.println("No destination!");
            return;
        }
        if (disableSelfDirection) {
            if (twoWayTeleports.size() > 1) {
                twoWayTeleports.remove(rat.getUniversalCoords());
            }
        }
        try {
            if (targetGate == null) {
                twoWayTeleports = findTeleportWithoutGate(twoWayTeleports, world);
                targetGate = twoWayTeleports.get(0);
                ((TwoWayTeleport) world.getBlockField(targetGate).getItem()).targetGate = rat.getUniversalCoords();
            } else {
                Item item = world.getBlockField(targetGate).getItem();
                if (item instanceof TwoWayTeleport) {
                    System.out.println("Moving to set place");
                } else {
                    twoWayTeleports = findTeleportWithoutGate(twoWayTeleports, world);
                    targetGate = twoWayTeleports.get(0);
                    ((TwoWayTeleport) world.getBlockField(targetGate).getItem()).targetGate = rat.getUniversalCoords();
                }

            }
            moveRatTo(world, rat, targetGate);
        } catch (IOException ex) {
            ex.printStackTrace();
            moveRatTo(world, rat, rat.getUniversalCoords());
        }
    }

}