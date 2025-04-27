package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

public class ComputerControl implements RatsController.RatControl {
    private int chaos = RatsController.DEFAULT_CHAOS;
    private boolean display = false;

    @Override
    public String id() {
        return "pc";
    }

    @Override
    public String toString() {
        return "chaos around 5 is really chaotic, above 50 is really apaptic";
    }

    public void selfAct(Rat rat, World world) {
        switch (RatsController.seed.nextInt(chaos)) {
            case 0:
                rat.setMouseLeft(world);
                break;
            case 1:
                rat.setMouseRight(world);
                break;
            case 2:
                rat.setMouseUp(world);
                break;
            case 3:
                rat.setMouseDown(world);
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
    public boolean isDisplay() {
        return display;
    }

    @Override
    public void setDisplay(Boolean aBoolean) {
        this.display = aBoolean;
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
