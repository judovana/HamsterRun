package nonsense.hamsterrun.env.traps;

import java.awt.Color;

public class InvisibleTrapDoor implements Item{
    //will NOT bound to exact coords. will always spit to random place
    //will regenerate the block if possible
    //if it is impossible to regenerate, maybe not fall through?
    //will remain stay
    public Color getMinimapColor() {
        return new Color(200, 200, 200);
    }
}
