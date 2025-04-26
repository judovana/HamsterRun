package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseControl extends HumanControl {

    Point lastPosition = new Point(0, 0);
    private long lastMove = System.currentTimeMillis();

    @Override
    public String id() {
        return "m1";
    }

    @Override
    public String toString() {
        return "mouse move  wheel wheel-click  double-click tripple-click";
    }


    public void actW(Rat rat, MouseWheelEvent ew, World world) {
        int notches = ew.getWheelRotation();
        if (notches > 0) {
            zoomIn();
        } else {
            zoomOut();
        }
    }

    public void actM(Rat rat, MouseEvent em, World world) {
        if (System.currentTimeMillis() - lastMove < BaseConfig.getConfig().getMouseDelay()) {
            return;
        }
        if (Math.abs(lastPosition.x - em.getLocationOnScreen().x) > Math.abs(lastPosition.y - em.getLocationOnScreen().y)) {
            if (lastPosition.x - em.getLocationOnScreen().x < 0) {
                rat.setMouseRight();
            } else if (lastPosition.x - em.getLocationOnScreen().x > 0) {
                rat.setMouseLeft();
            }
        } else if (Math.abs(lastPosition.x - em.getLocationOnScreen().x) < Math.abs(lastPosition.y - em.getLocationOnScreen().y)) {
            if (lastPosition.y - em.getLocationOnScreen().y < 0) {
                rat.setMouseDown();
            } else if (lastPosition.y - em.getLocationOnScreen().y > 0) {
                rat.setMouseUp();
            }
        }
        lastPosition = em.getLocationOnScreen();
        lastMove = System.currentTimeMillis();
    }

    public void actC(Rat rat, MouseEvent em, World world) {
        if ((em.getModifiersEx() & InputEvent.BUTTON2_DOWN_MASK) != 0) {
            //we actually need two maps - anymated and non anymated..
            map = !map;
        }
        if ((em.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0 && em.getClickCount() >= 2) {
            world.regenerateAll();
        }
        //keep those two? Made it configurable?
        //yes! per player? Yes!
        if ((em.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0 && em.getClickCount() == 2) {
            world.allRatsSpread(true);
        }
        if ((em.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0 && em.getClickCount() == 3) {
            world.allRatsSpread(false);
        }
    }
}
