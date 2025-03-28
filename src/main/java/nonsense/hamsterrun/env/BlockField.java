package nonsense.hamsterrun.env;

import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Item;
import nonsense.hamsterrun.env.traps.Teleport;
import nonsense.hamsterrun.env.traps.TrapDoor;
import nonsense.hamsterrun.env.traps.Vegetable;

import java.awt.Point;
import java.util.Random;

public class BlockField {

    private boolean passable;
    private Item item = new Empty();

    public BlockField(boolean passable) {
        this.passable = passable;
    }

    public boolean isPassable() {
        return passable;
    }

    public boolean isImpassable() {
        return !passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public Item getItem() {
        return item;
    }

    public void setRandomObstacle(Random seed) {
        int i = seed.nextInt(6);
        if (i < 3) {
            this.item = new Vegetable();
        } else if (i == 3) {
            this.item = new Teleport();
        } else if (i == 4) {
            this.item = new TrapDoor();
        } else if (i == 5) {
            this.item = new InvisibleTrapDoor();
        }
    }


    public boolean isFree() {
        return item == null;
    }

    public void clear() {
        this.item = new Empty();
    }
}
