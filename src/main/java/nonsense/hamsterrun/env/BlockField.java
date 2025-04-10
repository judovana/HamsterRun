package nonsense.hamsterrun.env;

import nonsense.hamsterrun.env.traps.AllWayTeleport;
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

import java.awt.Point;
import java.util.Random;

public class BlockField {

    private boolean passable;
    private Item item = new Empty();
    //this is nothing to relay on it is usually set only  if the block is part of neigbrho.
    //still each call to set it, should be identical
    private Point coordsInNeigbrhood;

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
        int i = seed.nextInt(14);
        if (i < 5) {
            this.item = new Vegetable();
        } else if (i == 6) {
            int subseed = seed.nextInt(6);
            switch (subseed) {
                case 0:
                    this.item = new OneWayTeleport();
                    break;
                case 1:
                case 2:
                    this.item = new TwoWayTeleport();
                    break;
                case 3:
                case 4:
                    this.item = new AllWayTeleport();
                    break;
                default:
                    if (seed.nextBoolean()) {
                        item = new Vegetable();
                    } else {
                        item = new Tunnel();
                    }
            }
        } else if (i == 7) {
            this.item = new TrapDoor();
        } else if (i == 8) {
            this.item = new InvisibleTrapDoor();
        } else if (i == 9 || i == 10 || i == 11) {
            this.item = new Tunnel();
        } else if (i == 12) {
            this.item = new Fire();
        } else if (i == 13) {
            this.item = new Torturer();
        }

    }


    public boolean isFree() {
        return item == null;
    }

    public void clear() {
        this.item = new Empty();
    }

    public void setLastNeighborhoodCords(int x, int y) {
        if (coordsInNeigbrhood == null) {
            coordsInNeigbrhood = new Point(x, y);
        } else {
            if (!coordsInNeigbrhood.equals(new Point(x, y))) {
                throw new RuntimeException("Field with set coords of " + coordsInNeigbrhood + " is being chnaged to " + new Point(x, y));
            }
        }
    }

    public Point getCoordsInNeigbrhood() {
        return coordsInNeigbrhood;
    }
}
