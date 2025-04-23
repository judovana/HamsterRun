package nonsense.hamsterrun.env;

import nonsense.hamsterrun.env.aliens.Alien;
import nonsense.hamsterrun.env.traps.AllWayTeleport;
import nonsense.hamsterrun.env.traps.Carrot;
import nonsense.hamsterrun.env.traps.ColorfullFlask;
import nonsense.hamsterrun.env.traps.Cucumber;
import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Item;
import nonsense.hamsterrun.env.traps.Mushroom;
import nonsense.hamsterrun.env.traps.OneWayTeleport;
import nonsense.hamsterrun.env.traps.Pepper;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.TrapDoor;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.TwoWayTeleport;
import nonsense.hamsterrun.env.traps.Vegetable;
import nonsense.hamsterrun.env.traps.Water;

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
        int i = seed.nextInt(101) + 1;
        if (i > 10 && i <= 20) {
            this.item = new Water();
        } else if (i > 20 && i <= 34) {
            this.item = new Cucumber();
        } else if (i > 34 && i <= 47) {
            this.item = new Pepper();
        } else if (i > 47 && i <= 60) {
            this.item = new Carrot();
        } else if (i > 60 && i <= 62) {
            this.item = new OneWayTeleport();
        } else if (i > 62 && i <= 66) {
            this.item = new TwoWayTeleport();
        } else if (i > 66 && i <= 68) {
            this.item = new AllWayTeleport();
        } else if (i > 68 && i <= 71) {
            this.item = new TrapDoor();
        } else if (i > 71 && i <= 74) {
            this.item = new InvisibleTrapDoor();
        } else if (i > 74 && i <= 90) {
            this.item = new Tunnel();
        } else if (i > 90 && i <= 94) {
            this.item = new Fire();
        } else if (i > 94 && i <= 98) {
            this.item = new Torturer();
        } else if (i > 98 &&  i <= 99) {
            this.item = new Mushroom();
        } else if (i > 99 &&  i <= 100) {
            this.item = new ColorfullFlask();
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
