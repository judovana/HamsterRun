package nonsense.hamsterrun.env;

import nonsense.hamsterrun.env.aliens.Alien;
import nonsense.hamsterrun.env.traps.AllWayTeleport;
import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Item;
import nonsense.hamsterrun.env.traps.Mushroom;
import nonsense.hamsterrun.env.traps.OneWayTeleport;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.TrapDoor;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.TwoWayTeleport;
import nonsense.hamsterrun.env.traps.Vegetable;

import java.awt.Point;
import java.util.Random;

public class BlockField {

    private final Point coords;
    private final BaseBlock parent;
    private boolean passable;
    //to ahve only one item and one alien is simply simplification
    private Item item = new Empty();
    //unlike item, they move. if two aliens meet, they anhilate to one or none (if they are of same strength.. explosion?)
    //wreckingball, bat... to do, implement.. somwhen...
    private Alien alien;

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

    //fixme - made this setup-able, absolutely.
    //eg via percent, including absolute disablement
    //then use the list instead of reame (including sound)
    public void setRandomObstacle(Random seed) {
        int i = seed.nextInt(15);
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
        } else if (i == 14) {
            this.item = new Mushroom();
        }


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
}
