package nonsense.hamsterrun.env.traps;

import java.awt.Color;
import java.util.Random;

public class Vegetable implements Item {

    int energy = seed.nextInt(8)+2;

    public Color getMinimapColor() {
        return Color.green;
    }

    public boolean eat() {
        energy--;
        return (energy <= 0);
    }
}
