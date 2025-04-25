package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Draws on second level, and if on cross road, the exit from it is random (on strigth, may go even back)
 */
public class Tunnel implements Item {

    public Color getMinimapColor() {
        return new Color(20, 20, 20);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToMoveQueue(SoundsBuffer.brbliTunel);
    }


    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 3) {
            g2d.drawImage(SpritesProvider.tunnelOpened, coordx, coordy, zoom, zoom, null);
            if (neigbours!=null) {
                BlockField lb = neigbours.getLeftField(x, y);
                if (lb == null || lb.isImpassable() || lb.getItem() instanceof Tunnel) {
                    g2d.drawImage(SpritesProvider.tunnelClosed[3], coordx, coordy, zoom, zoom, null);
                }
                BlockField rb = neigbours.getRightField(x, y);
                if (rb == null || rb.isImpassable() || rb.getItem() instanceof Tunnel) {
                    g2d.drawImage(SpritesProvider.tunnelClosed[1], coordx, coordy, zoom, zoom, null);
                }
                BlockField ub = neigbours.getUpField(x, y);
                if (ub == null || ub.isImpassable() || ub.getItem() instanceof Tunnel) {
                    g2d.drawImage(SpritesProvider.tunnelClosed[0], coordx, coordy, zoom, zoom, null);
                }
                BlockField db = neigbours.getDownField(x, y);
                if (db == null || db.isImpassable() || db.getItem() instanceof Tunnel) {
                    g2d.drawImage(SpritesProvider.tunnelClosed[2], coordx, coordy, zoom, zoom, null);
                }
            }
        }
    }

    @Override
    public void drawThumbnail(Graphics2D g2d, int size) {
        drawInto(g2d, 0,0, size, 3,null, 0, 0);
    }

}
