package nonsense.hamsterrun.env.traps;

import java.awt.Color;
import java.awt.Graphics2D;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.sprites.SpritesProvider;


//will sloww down rapidly. blue transaprent ove rat
//wave effect - would be nice if it is slihtly over field, and there willbe /\/\/\/\  which will match form l->r and from down->up
public class Water implements Item {

    public static int anim = 0; //indeed all watters must share anim counter!
    private static Thread waterCounter = new Thread("water waiver") {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                    anim++;
                    if (anim > 1) {
                        anim = 0;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    static {
        waterCounter.setDaemon(true);
        waterCounter.start();
    }


    public Color getMinimapColor() {
        return Color.blue;
    }

    @Override
    public void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y) {
        if (level == 3) {
            g2d.drawImage(SpritesProvider.water[anim % 2], coordx - zoom / 2, coordy - zoom / 2, 2 * zoom, 2 * zoom, null);
        }
    }

    @Override
    public void drawThumbnail(Graphics2D g2d, int size) {
        drawInto(g2d, 0,0, size, 3,null, 0, 0);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {

    }
}
