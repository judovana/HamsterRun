package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AllWayTeleport extends Teleport {
    //will drop the mouse to any other gate.
    //it MUST drop it NEXT to it, otherwise it will cause endless loop
    //Not sure what to do if there are no other gates - it will msot likely drop itself nexto itself, which would be nicely confusing


    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getAllWayTeleport(id);
    }

    @Override
    public void relocate(World world, Rat rat) {
        List<Point> twoWayTeleports = new ArrayList<>();
        for (int x = 0; x < BaseConfig.getConfig().getGridSize() * BaseConfig.getConfig().getBaseSize(); x++) {
            for (int y = 0; y < BaseConfig.getConfig().getGridSize() * BaseConfig.getConfig().getBaseSize(); y++) {
                Item item = world.getBlockField(new Point(x, y)).getItem();
                if (item instanceof AllWayTeleport) {
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
        moveRatTo(world, rat, w);
    }

}