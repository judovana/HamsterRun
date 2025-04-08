package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OneWayTeleport  extends  TwoWayTeleport {
    //wil bound to exact coords as gateway, if they exists. if not, will find new one
    //will continue walking
    //should lead to random spot?
    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }

    @Override
    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getOneWayTeleport(id);
    }



}
