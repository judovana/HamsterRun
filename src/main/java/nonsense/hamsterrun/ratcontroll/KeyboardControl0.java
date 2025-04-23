package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.event.KeyEvent;

public class KeyboardControl0 extends KeyboardControl {
    @Override
    public String id() {
        return "k0";
    }

    @Override
    public String toString() {
        return "+ - end, del, home, ins";
    }

    public void act(Rat rat, KeyEvent e, World world) {
        if (e.getKeyChar() == '+') {
            zoomIn();
        } else if (e.getKeyChar() == '-') {
            zoomOut();
        } else if (e.getKeyCode() == KeyEvent.VK_END) {
            //we actually need two maps - anymated and non anymated.. what to do with zoom?
            //base second zoom here internally?
            map = !map;
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            world.regenerateAll();
        }
        //keep those two? Made it configurable?
        //yes! per player? Yes!
        else if (e.getKeyCode() == KeyEvent.VK_HOME) {
            world.allRatsSpread(true);
        } else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            world.allRatsSpread(false);
        }
    }
}
