package nonsense.hamsterrun.env.traps;

import java.awt.Color;

public class TrapDoor extends InvisibleTrapDoor implements Item {

    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }
}
