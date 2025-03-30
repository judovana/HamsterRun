package nonsense.hamsterrun.env.traps;

import java.awt.Color;

/**
 * Draws on second level, and if on cross road, the exit from it is random (on strigth, may go even back)
 */
public class Tunnel implements Item {

    public Color getMinimapColor() {
        return new Color(20, 20, 20);
    }

    public int getLevel() {
        return 2; //after rats
    }
}
