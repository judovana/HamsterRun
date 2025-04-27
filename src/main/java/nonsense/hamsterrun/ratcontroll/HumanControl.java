package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;

public abstract class HumanControl implements RatsController.RatControl {
    protected boolean map = false;
    private int worldZoom = 64;
    private int mapZoom = 16;
    private boolean display = false;

    private static int calcZoomIn(int i) {
        return i + Math.max(i / 2, 1);
    }

    private static int calcZoomOut(int i) {
        i = i - Math.max(1, i / 2);
        if (i <= 0) {
            i = 1;
        }
        return i;
    }

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

    @Override
    public void zoomOut() {
        if (getMap() == 0) {
            worldZoom = calcZoomOut(worldZoom);
        } else {
            mapZoom = calcZoomOut(mapZoom);
        }
    }

    @Override
    public void selfAct(Rat rat, World world) {

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
