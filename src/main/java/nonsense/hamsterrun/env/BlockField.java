package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.aliens.Alien;
import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Item;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockField {

    private static final List<ItemsWithBoundaries> recalcualted = recalculateToBoundaries(BaseConfig.DEFAULT_ITEMS_PROBABILITIES);

    public static List<ItemsWithBoundaries> recalculateToBoundaries(BaseConfig.ItemsWithProbability[] itemsWithProbabilities) {
        int maxSum = Arrays.stream(itemsWithProbabilities).map(a -> a.ratio).collect(Collectors.summingInt(Integer::intValue));
        List<ItemsWithBoundaries> recalcualted = new ArrayList<>(itemsWithProbabilities.length);
        int usedSum = 0;
        float probabCheck = 0;
        for (BaseConfig.ItemsWithProbability item : itemsWithProbabilities) {
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
