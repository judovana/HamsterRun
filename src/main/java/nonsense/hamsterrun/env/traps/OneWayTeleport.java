package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OneWayTeleport  extends  TwoWayTeleport {
    //wil drop the  mouse to one bound space. If the space no longer exists (is impassable)
    //will move pick up new random spot

    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }

    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getOneWayTeleport(id);
    }



}
