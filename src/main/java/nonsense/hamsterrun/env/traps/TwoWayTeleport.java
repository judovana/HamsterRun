package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class TwoWayTeleport extends  Teleport {
    //will drop the mouse to unassigned  gate. the gate will beocme assigned
    //it MUST drop it NEXT to it, otherwise it will cause endless loop
    //if all gates are assigned, then to random one


    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getTwoWayTeleport(id);
    }

}