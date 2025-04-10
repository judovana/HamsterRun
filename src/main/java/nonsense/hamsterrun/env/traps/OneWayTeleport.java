package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class OneWayTeleport extends Teleport {
    //wil drop the  mouse to one bound space. If the space no longer exists (is impassable)
    //will move pick up new random spot

    private Point target;

    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }

    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getOneWayTeleport(id);
    }

    @Override
    public void relocate(World world, Rat rat) {
        if (target == null) {
            InvisibleTrapDoor.randomAloneSpot(world, rat);
            target = rat.getUniversalCoords();
        } else {
            if (world.getBlockField(target).isPassable()) {
                rat.setUniversalCoords(target);
            } else {
                InvisibleTrapDoor.randomAloneSpot(world, rat);
                target = rat.getUniversalCoords();
            }
        }
    }


}
