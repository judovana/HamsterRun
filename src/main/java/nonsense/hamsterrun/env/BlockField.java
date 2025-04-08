package nonsense.hamsterrun.env;

import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Item;
import nonsense.hamsterrun.env.traps.OneWayTeleport;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.TwoWayTeleport;
import nonsense.hamsterrun.env.traps.TrapDoor;
import nonsense.hamsterrun.env.traps.Vegetable;

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
        int i = seed.nextInt(15);
        if (i < 5) {
            this.item = new Vegetable();
        } else if (i == 6) {
            if (seed.nextBoolean()) {
                this.item = new TwoWayTeleport();
            } else {
                if (seed.nextBoolean()) {
                    item = new Vegetable();
                }
            }
        } else if (i == 7) {
            this.item = new TrapDoor();
        } else if (i == 8) {
            this.item = new InvisibleTrapDoor();
        } else if (i == 9 || i == 10 || i == 11) {
            this.item = new Tunnel();
        } else if (i == 12) {
            if (seed.nextInt(3) == 0) {
                this.item = new OneWayTeleport();
            } else {
                if (seed.nextBoolean()) {
                    this.item = new Vegetable();
                }
            }
        } else if (i == 13) {
            this.item = new Fire();
        } else if (i == 14) {
            this.item = new Torturer();
        }

    }


    public boolean isFree() {
        return item == null;
    }

    public void clear() {
        this.item = new Empty();
    }
}
