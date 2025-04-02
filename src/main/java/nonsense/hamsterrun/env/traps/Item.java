package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public interface Item {
    static final Random seed = new Random();


    Color getMinimapColor();

    default int getLevel() {
        return 2;//1 floor, 2 before rats, rats, 3 over rats
    }

    void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, BaseBlockNeigbours neigbours);
}
