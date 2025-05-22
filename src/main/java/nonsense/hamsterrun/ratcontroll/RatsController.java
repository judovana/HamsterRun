package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RatsController implements RatsProvider {

    //TODO make this configurable?
    public static final int DEFAULT_CHAOS = 20;


    static final Random seed = new Random();

    private List<RatWithControls> rats = new ArrayList<>(4);

    public void addRat(RatWithControls ratWithControls) {
        this.rats.add(ratWithControls);
    }

    @Override
    public List<Rat> getRats() {
        return rats.stream().filter(a -> a != null && a.rat != null).map(a -> a.rat).collect(Collectors.toUnmodifiableList());
    }

    private List<RatWithControls> getRatsWithControlls() {
        return rats.stream().filter(a -> a != null && a.rat != null).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public RatControl getRatControl(Rat rat) {
        for (RatWithControls rats: rats){
            if (rats.rat == rat) {
                return rats.ratControl;
            }
        }
        return null;
    }

    @Override
    public void swap(Rat rat) {
        RatWithControls current = rats.get(seed.nextInt(rats.size()));
        for (int x = 0; x < 5; x++) {
            RatWithControls future = rats.get(seed.nextInt(rats.size()));
            if (future == current) {
                continue;
            } else {
                RatControl currentControl = current.getRatControl();
                RatControl futureControl = future.getRatControl();
                current.setRatControl(futureControl);
                future.setRatControl(currentControl);
                break;
            }
        }


    }

    @Override
    public void kill() {
        for (Rat rat : this.getRats()) {
            rat.getSounds().kill();
        }
        rats.clear();
    }

    @Override
    public void remove(Rat rat) {
        //FIXME introduce some time and score results.
        //if such resutls will not be empty, show...
        //eg on the empty screen after palyer left
        //eg on the exit
        for (int x = 0; x < rats.size(); x++) {
            if (rats.get(x).rat == rat) {
                rats.remove(x);
                x--;// or break? can there be duplicates?
            }
        }
    }

    public KeyboardControl getNulLControl() {
        for (RatWithControls ratsconts : rats) {
            if (ratsconts.rat == null) {
                return (KeyboardControl) (ratsconts.ratControl);
            }
        }
        return null;
    }

    public interface RatControl {

        void selfAct(Rat rat, World world);

        int getZoom();

        void zoomIn();

        void zoomOut();

        boolean isDisplay();

        void setDisplay(Boolean aBoolean);

        void setChaos(int i);

        int getMap();

        String id();
    }

    public static class RatWithControls {
        private final Rat rat;
        private RatControl ratControl;

        public RatWithControls(Rat rat, RatControl ratControl) {
            this.rat = rat;
            this.ratControl = ratControl;
        }

        public RatControl getRatControl() {
            return ratControl;
        }

        public void setRatControl(RatControl ratControl) {
            this.ratControl = ratControl;
        }
    }
}
