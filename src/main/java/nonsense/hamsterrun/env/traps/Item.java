package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.BaseBlockNeigbours;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.SoundsBuffer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

//use this in some readme instead of plain doc
public interface Item {
    static final Random seed = new Random();


    Color getMinimapColor();
    void playMainSoundFor(SoundsBuffer rat);
    default void playSecondarySoundFor(SoundsBuffer rat){};
    default void playTercialSoundFor(SoundsBuffer rat){};

    //level: 1 floor, 2 before rats, rats, 3 over rats
    void drawInto(Graphics2D g2d, int coordx, int coordy, int zoom, int level, BaseBlockNeigbours neigbours, int x, int y);
}
