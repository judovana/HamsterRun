package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RatsController implements RatsProvider {

    private List<RatWithControls> rats = new ArrayList<>(4);

    public void addRat(RatWithControls ratWithControls) {
        this.rats.add(ratWithControls);
    }

    @Override
    public List<Rat> getRats() {
        return rats.stream().map(a -> a.rat).collect(Collectors.toUnmodifiableList());
    }

    public interface RatControl {

    }

    public static abstract class KeyboardControl implements RatControl {

    }

    public static class KeyboardControl1 extends KeyboardControl {

    }

    public static class KeyboardControl2 extends KeyboardControl {

    }

    public static class KeyboardControl3 extends KeyboardControl {

    }

    public static class MouseControl implements RatControl {

    }

    public static class ComputerControl implements RatControl {

    }

    public static class RatWithControls {
        private final Rat rat;
        private final RatControl ratControl;
        private final World world;

        public RatWithControls(Rat rat, RatControl ratControl, World world) {
            this.rat = rat;
            this.ratControl = ratControl;
            if (ratControl instanceof ComputerControl) {
                rat.setAi(true);
            }
            this.world = world;
        }
    }
}
