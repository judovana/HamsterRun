package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TwoWayTeleport implements Item {
    //will drop the mouse to unassigned  gate. the gate willbeocme assigned
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
            if (anim>49){
                anim = 0;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(getSprite(anim/10+1), coordx-zoom/2, coordy-zoom/2, zoom+zoom, zoom+zoom, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }

    protected BufferedImage getSprite(int id) {
        return SpritesProvider.getTwoWayTeleport(id);
    }
}
