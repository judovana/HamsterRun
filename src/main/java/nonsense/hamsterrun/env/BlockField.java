package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Item;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class BlockField {

    private final Point coords;
    private final BaseBlock parent;
    private boolean passable;
    //to ahve only one item and one alien is simply simplification
    private Item item = new Empty();

    public BlockField(boolean passable, Point coords, BaseBlock parent) {
        this.passable = passable;
        this.coords = coords;
        this.parent = parent;
    }


    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public boolean isImpassable() {
        return !passable;
    }

    public Item getItem() {
        return item;
    }

    public boolean isFree() {
        return item == null;
    }

    public void clear() {
        this.item = new Empty();
    }

    public Point getCoords() {
        return coords;
    }

    public BaseBlock getParent() {
        return parent;
    }

    public Point getUniversalCoords() {
        return Rat.toUniversalCoords(getParent().getCoords(), getCoords());
    }

    public void setRandomObstacle(Random seed) {
        List<ItemsWithBoundaries> recalcualted = ItemsWithBoundaries.recalculateToBoundaries(BaseConfig.getConfig().getItemsProbabilities());
        int i = seed.nextInt(recalcualted.get(recalcualted.size() - 1).upper);
        for (ItemsWithBoundaries item : recalcualted) {
            if (i >= item.lower && i < item.upper) {
                this.item = ItemsWithBoundaries.itemClassToItemCatched(item.clazz);
                break;
            }
        }
    }

}
