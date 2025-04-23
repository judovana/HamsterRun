package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.event.KeyEvent;

public abstract class KeyboardControl extends HumanControl {
    public abstract void act(Rat rat, KeyEvent e, World world);
}
