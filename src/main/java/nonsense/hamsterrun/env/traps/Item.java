package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public interface Item {
    static final Random seed = new Random();


    Color getMinimapColor();
    //fixme move sounds to here - it may have severa; spunds - eg fire
    //use this in some readme instead of plain doc


    //level: 1 floor, 2 before rats, rats, 3 over rats
    void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y);
}
