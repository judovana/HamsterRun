package nonsense.hamsterrun.env.traps;

import java.awt.Color;

public class InvisibleTrapDoor implements Item{

    public Color getMinimapColor() {
        return new Color(200, 200, 200);
    }
}
