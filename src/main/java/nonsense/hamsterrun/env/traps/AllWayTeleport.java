package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.image.BufferedImage;

public class AllWayTeleport extends  Teleport {
    //will drop the mouse to any other gate.
    //it MUST drop it NEXT to it, otherwise it will cause endless loop
    //Not sure what to do if there are no other gates


    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getAllWayTeleport(id);
    }

}