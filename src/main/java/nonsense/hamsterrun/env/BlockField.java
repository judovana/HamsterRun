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
import nonsense.hamsterrun.env.traps.Water;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockField {

    private static ItemsWithProbability[] itemsWithProbabilities = new ItemsWithProbability[]{
            new ItemsWithProbability(Empty.class, 50),
            new ItemsWithProbability(Cucumber.class, 30),
            new ItemsWithProbability(Carrot.class, 15),
            new ItemsWithProbability(Pepper.class, 15),
            new ItemsWithProbability(Cucumber.class, 30),
            new ItemsWithProbability(OneWayTeleport.class, 4),
            new ItemsWithProbability(TwoWayTeleport.class, 8),
            new ItemsWithProbability(AllWayTeleport.class, 4),
            new ItemsWithProbability(TrapDoor.class, 5),
            new ItemsWithProbability(InvisibleTrapDoor.class, 4),
            new ItemsWithProbability(Water.class, 15),
            new ItemsWithProbability(Tunnel.class, 20),
            new ItemsWithProbability(Fire.class, 10),
            new ItemsWithProbability(Torturer.class, 10),
            new ItemsWithProbability(Mushroom.class, 2),
            new ItemsWithProbability(ColorfullFlask.class, 2),

    };
    private static final List<ItemsWithBoundaries> recalcualted = recalculateToBoundaries(itemsWithProbabilities);

    private static List<ItemsWithBoundaries> recalculateToBoundaries(ItemsWithProbability[] itemsWithProbabilities) {
        int maxSum = Arrays.stream(itemsWithProbabilities).map(a -> a.ratio).collect(Collectors.summingInt(Integer::intValue));
        List<ItemsWithBoundaries> recalcualted = new ArrayList<>(itemsWithProbabilities.length);
        int usedSum = 0;
        float probabCheck = 0;
        for (ItemsWithProbability item : itemsWithProbabilities) {
            if (item.ratio > 0) {
                recalcualted.add(new ItemsWithBoundaries(item.clazz, usedSum, usedSum + item.ratio));
                usedSum = usedSum + item.ratio;
                System.out.println("Adding " + item.clazz.getSimpleName() + " as " + recalcualted.size() + " of (max) " + itemsWithProbabilities.length);
                float probab = ((float) item.ratio / (float) maxSum * (float) 100);
                probabCheck += probab;
                System.out.println(" Probability is " + probab + " %");
            }
        }
        System.out.println("Total: " + probabCheck + " %");
        if (usedSum != maxSum) {
            throw new RuntimeException("Author can not count");
        }
        if (recalcualted.isEmpty()){
            throw new RuntimeException("At least empty wall must be present");
        }
        return recalcualted;
    }

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


    private static class ItemsWithProbability {
        //0 == disabled
        private final Class clazz;
        private final int ratio;

        public ItemsWithProbability(Class clazz, int ratio) {
            this.clazz = clazz;
            this.ratio = ratio;
        }
    }

    private static class ItemsWithBoundaries {
        //0 == disabled
        private final Class clazz;
        private final int lower;
        private final int upper;

        public ItemsWithBoundaries(Class clazz, int lower, int upper) {
            this.clazz = clazz;
            this.lower = lower;
            this.upper = upper;
        }
    }

    //fixme - made this setup-able, absolutely.
    public void setRandomObstacle(Random seed) {
        int i = seed.nextInt(recalcualted.get(recalcualted.size()-1).upper);
        for (ItemsWithBoundaries item : recalcualted) {
            if (i >= item.lower && i < item.upper) {
                try {
                    Constructor<?> ctor = item.clazz.getConstructor();
                    Object object = ctor.newInstance();
                    this.item = (Item) object;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }
}
