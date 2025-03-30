package nonsense.hamsterrun.env.traps;

import java.awt.Color;
import java.util.Random;

public interface Item {
    static final Random seed = new Random();


    Color getMinimapColor();
    default int getLevel() {
        return 1; //before rats
    }

}
