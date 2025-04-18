package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

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
    }

    public static abstract class HumanControl implements RatControl {
        private int worldZoom = 64;

        @Override
        public int getZoom() {
            return worldZoom;
        }

        @Override
        public void zoomIn() {
            worldZoom = worldZoom + Math.max(worldZoom / 2, 1);
        }

        @Override
        public void zoomOut() {
            worldZoom = worldZoom - Math.max(1, worldZoom / 2);
            if (worldZoom <= 0) {
                worldZoom = 1;
            }
        }

        @Override
        public void selfAct(Rat rat) {

        }
    }

    public static abstract class KeyboardControl extends HumanControl {

    }

    public static class KeyboardControl1 extends KeyboardControl {

    }

    public static class KeyboardControl2 extends KeyboardControl {

    }

    public static class KeyboardControl3 extends KeyboardControl {

    }

    public static class MouseControl extends HumanControl {

    }

    public static class ComputerControl implements RatControl {
        public void selfAct(Rat rat) {
            switch (seed.nextInt(20)) {
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
            return 0;
        }

        @Override
        public void zoomIn() {

        }

        @Override
        public void zoomOut() {

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
