package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class TwoWayTeleport implements Item {
    //wil bound to exact coords as gateway, if they exists. if not, will find new one
    //will create back door on landing
    //they will be defunct for some short time
    //will continue walking
    //must lead to output gate

    int anim = seed.nextInt(48);

    public Color getMinimapColor() {
        return new Color(255, 192, 203);
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours) {
        if (level == 2) {
            g2d.drawImage(SpritesProvider.twoWayTeleport[0], coordx, coordy, zoom, zoom, null);
        }
        if (level == 3) {
            float opacity = 0.75f;
            anim++;
            if (anim>49){
                anim = 0;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(SpritesProvider.getTeleport(anim/10+1), coordx, coordy, zoom, zoom, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }
}
