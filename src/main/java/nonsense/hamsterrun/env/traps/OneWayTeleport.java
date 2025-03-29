package nonsense.hamsterrun.env.traps;

import java.awt.Color;

public class OneWayTeleport implements Item {
    //wil bound to exact coords as gateway, if they exists. if not, will find new one
    //will continue walking

    public Color getMinimapColor() {
        return new Color(230, 255, 0);
    }
}
