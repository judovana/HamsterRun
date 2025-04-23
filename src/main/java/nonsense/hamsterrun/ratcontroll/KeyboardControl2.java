package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.event.KeyEvent;

public class KeyboardControl2 extends KeyboardControl {

    @Override
    public String id() {
        return "k2";
    }

    @Override
    public String toString() {
        return "wasd 13 2456";
    }

    public void act(Rat rat, KeyEvent e, World world) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            rat.setMouseLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            rat.setMouseUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            rat.setMouseRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            rat.setMouseDown();
        } else if (e.getKeyChar() == '1') {
            zoomIn();
        } else if (e.getKeyChar() == '3') {
            zoomOut();
        } else if (e.getKeyChar() == '2') {
            //we actually need two maps - anymated and non anymated..
            map = !map;
        } else if ((e.getKeyChar() == '4')) {
            world.regenerateAll();
        }
        //keep those two? Made it controllable?
        else if ((e.getKeyChar() == '5')) {
            world.allRatsSpread(true);
        } else if ((e.getKeyChar() == '6')) {
            world.allRatsSpread(false);
        }
    }
}
