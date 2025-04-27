package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.event.KeyEvent;

public class KeyboardControl3 extends KeyboardControl {

    @Override
    public String id() {
        return "k3";
    }

    @Override
    public String toString() {
        return "jilk 89 0 bckspace -=";
    }

    public void act(Rat rat, KeyEvent e, World world) {
        if (e.getKeyCode() == KeyEvent.VK_J) {
            rat.setMouseLeft(world);
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            rat.setMouseUp(world);
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            rat.setMouseRight(world);
        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            rat.setMouseDown(world);
        } else if (e.getKeyChar() == '8') {
            zoomIn();
        } else if (e.getKeyChar() == '9') {
            zoomOut();
        } else if (e.getKeyChar() == '0') {
            //we actually need two maps - anymated and non anymated..
            map = !map;
        } else if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            world.regenerateAll();
        }
        //keep those two? Made it configurable?
        //yes! per player? Yes!
        else if ((e.getKeyChar() == '-')) {
            world.allRatsSpread(true);
        } else if ((e.getKeyChar() == '=')) {
            world.allRatsSpread(false);
        }
    }
}
