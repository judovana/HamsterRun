package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Item;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public static List<ItemsWithBoundaries> recalculateToBoundaries(List<BaseConfig.ItemsWithProbability> itemsWithProbabilities) {
        int maxSum = itemsWithProbabilities.stream().map(a -> a.ratio).collect(Collectors.summingInt(Integer::intValue));
        List<ItemsWithBoundaries> recalcualted = new ArrayList<>(itemsWithProbabilities.size());
        int usedSum = 0;
        float probabCheck = 0;
        for (BaseConfig.ItemsWithProbability item : itemsWithProbabilities) {
            if (item.ratio > 0) {
                recalcualted.add(new ItemsWithBoundaries(item.clazz, usedSum, usedSum + item.ratio));
                usedSum = usedSum + item.ratio;
                System.out.println("Adding " + item.clazz.getSimpleName() + " as " + recalcualted.size() + " of (max) " + itemsWithProbabilities.size());
                float probab = ((float) item.ratio / (float) maxSum * (float) 100);
                probabCheck += probab;
                System.out.println(" Probability is " + probab + " %");
            }
        }
        System.out.println("Total: " + probabCheck + " %");
        if (usedSum != maxSum) {
            throw new RuntimeException("Author can not count");
        }
        if (recalcualted.isEmpty()) {
            throw new RuntimeException("At least empty wall must be present");
        }
        return recalcualted;
    }

    public static Item itemClassToItemCatched(Class clazz) {
        try {
            return itemClassToItem(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Item itemClassToItem(Class clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance();
        return (Item) object;
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
        List<ItemsWithBoundaries> recalcualted = recalculateToBoundaries(BaseConfig.getConfig().getItemsProbabilities());
        int i = seed.nextInt(recalcualted.get(recalcualted.size() - 1).upper);
        for (ItemsWithBoundaries item : recalcualted) {
            if (i >= item.lower && i < item.upper) {
                this.item = itemClassToItemCatched(item.clazz);
                break;
            }
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
}
