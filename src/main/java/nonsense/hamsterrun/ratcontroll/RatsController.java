package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RatsController implements RatsProvider {

    private static final Random seed = new Random();

    private List<RatWithControls> rats = new ArrayList<>(4);

    public void addRat(RatWithControls ratWithControls) {
        this.rats.add(ratWithControls);
    }

    @Override
    public List<Rat> getRats() {
        return rats.stream().map(a -> a.rat).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public RatControl getRatControl(Rat rat) {
        return rats.stream().filter(a -> a.rat == rat).map(a -> a.ratControl).findFirst().get();
    }

    public interface RatControl {

        void selfAct(Rat rat);

        int getZoom();

        void zoomIn();

        void zoomOut();

        void setDisplay(Boolean aBoolean);

        boolean isDisplay();

        void setChaos(int i);

        int getMap();
    }

    public static abstract class HumanControl implements RatControl {
        private int worldZoom = 64;
        private int mapZoom = 16;
        private boolean display = false;
        protected boolean map = false;

        @Override
        public int getZoom() {
            if (getMap() == 0) {
                return worldZoom;
            } else {
                return mapZoom;
            }
        }

        @Override
        public void zoomIn() {
            if (getMap() == 0) {
                worldZoom = calcZoomIn(worldZoom);
            } else {
                mapZoom = calcZoomIn(mapZoom);
            }
        }

        private static int calcZoomIn(int i) {
            return i + Math.max(i / 2, 1);
        }

        @Override
        public void zoomOut() {
            if (getMap() == 0) {
                worldZoom = calcZoomOut(worldZoom);
            } else {
                mapZoom = calcZoomOut(mapZoom);
            }
        }

        private static int calcZoomOut(int i) {
            i = i - Math.max(1, i / 2);
            if (i <= 0) {
                i = 1;
            }
            return i;
        }

        @Override
        public void selfAct(Rat rat) {

        }

        @Override
        public void setDisplay(Boolean aBoolean) {
            this.display = aBoolean;
        }

        @Override
        public boolean isDisplay() {
            return display;
        }

        @Override
        public void setChaos(int i) {

        }

        @Override
        public int getMap() {
            if (map) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static abstract class KeyboardControl extends HumanControl {

        public abstract void act(Rat rat, KeyEvent e, World world);
    }

    public static class KeyboardControl1 extends KeyboardControl {
        public void act(Rat rat, KeyEvent e, World world) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                rat.setMouseLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                rat.setMouseUp();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rat.setMouseRight();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                rat.setMouseDown();
            } else if (e.getKeyChar() == '+') {
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
            //keep those two? Made it controllable?
            else if (e.getKeyCode() == KeyEvent.VK_HOME) {
                world.allRatsSpread(true);
            } else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                world.allRatsSpread(false);
            }
        }

    }

    public static class KeyboardControl2 extends KeyboardControl {
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
                //we actually need two maps - anymated and non anymated.. what to do with zoom?
                //base second zoom here internally?
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

    public static class KeyboardControl3 extends KeyboardControl {
        public void act(Rat rat, KeyEvent e, World world) {
            if (e.getKeyCode() == KeyEvent.VK_J) {
                rat.setMouseLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_I) {
                rat.setMouseUp();
            } else if (e.getKeyCode() == KeyEvent.VK_L) {
                rat.setMouseRight();
            } else if (e.getKeyCode() == KeyEvent.VK_K) {
                rat.setMouseDown();
            } else if (e.getKeyChar() == '8') {
                zoomIn();
            } else if (e.getKeyChar() == '9') {
                zoomOut();
            } else if (e.getKeyChar() == '0') {
                //we actually need two maps - anymated and non anymated.. what to do with zoom?
                //base second zoom here internally?
                map = !map;
            } else if ((e.getKeyChar() == '4')) {
                world.regenerateAll();
            }
            //keep those two? Made it controllable?
            else if ((e.getKeyChar() == '-')) {
                world.allRatsSpread(true);
            } else if ((e.getKeyChar() == '=')) {
                world.allRatsSpread(false);
            }
        }
    }

    public static class MouseControl extends HumanControl {

    }

    public static class ComputerControl implements RatControl {
        private int chaos = 20;
        private boolean display = false;

        public void selfAct(Rat rat) {
            switch (seed.nextInt(chaos)) {
                case 0:
                    rat.setMouseLeft();
                    break;
                case 1:
                    rat.setMouseRight();
                    break;
                case 2:
                    rat.setMouseUp();
                    break;
                case 3:
                    rat.setMouseDown();
                    break;
                default: //ok
            }
        }

        @Override
        public int getZoom() {
            return 64;
        }

        @Override
        public void zoomIn() {

        }

        @Override
        public void zoomOut() {

        }

        @Override
        public void setDisplay(Boolean aBoolean) {
            this.display = aBoolean;
        }

        @Override
        public boolean isDisplay() {
            return display;
        }

        @Override
        public void setChaos(int i) {
            this.chaos = i;
        }

        @Override
        public int getMap() {
            return 0;
        }
    }

    public static class RatWithControls {
        private final Rat rat;
        private final RatControl ratControl;

        public RatWithControls(Rat rat, RatControl ratControl) {
            this.rat = rat;
            this.ratControl = ratControl;
        }

    }
}
