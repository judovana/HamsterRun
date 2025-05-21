package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.ThumbnailAble;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

//use this in some readme instead of plain doc
public interface Item extends ThumbnailAble {
    static final Random seed = new Random();


    Color getMinimapColor();


    //level: 1 floor, 2 before rats, rats, 3 over rats
    //missing somethign between floor and walls.. but nvm...
    void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y);

}
