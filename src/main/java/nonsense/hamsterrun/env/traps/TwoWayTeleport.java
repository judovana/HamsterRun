package nonsense.hamsterrun.env.traps;

import java.awt.Color;

public class TwoWayTeleport implements Item{
    //wil bound to exact coords as gateway, if they exists. if not, will find new one
    //will create back door on landing
    //they will be defunct for some short time
    //will continue walking
    public Color getMinimapColor() {
        return new Color(255, 230, 0);
    }
}
