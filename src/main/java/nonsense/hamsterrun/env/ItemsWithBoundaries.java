package nonsense.hamsterrun.env;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.traps.Item;

public class ItemsWithBoundaries {

    private static final boolean logItemsSpreading = false;

    //0 == disabled
    public final Class clazz;
    public final int lower;
    public final int upper;

    public ItemsWithBoundaries(Class clazz, int lower, int upper) {
        this.clazz = clazz;
        this.lower = lower;
        this.upper = upper;
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
                logItemSpreading("Adding " + item.clazz.getSimpleName() + " as " + recalcualted.size() + " of (max) " + itemsWithProbabilities.size());
                float probab = ((float) item.ratio / (float) maxSum * (float) 100);
                probabCheck += probab;
                logItemSpreading(" Probability is " + probab + " %");
            }
        }
        logItemSpreading("Total: " + probabCheck + " %");
        if (usedSum != maxSum) {
            throw new RuntimeException("Author can not count");
        }
        if (recalcualted.isEmpty()) {
            throw new RuntimeException("At least empty wall must be present");
        }
        return recalcualted;
    }

    private static void logItemSpreading(String s) {
        if (logItemsSpreading) {
            System.out.println(s);
        }
    }

    public static Item itemClassToItemCatched(Class clazz) {
        try {
            return itemClassToItem(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Item itemClassToItem(Class clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance();
        return (Item) object;
    }

    public static MovingOne alienClassToItemCatched(Class clazz) {
        try {
            return alienClassToItem(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MovingOne alienClassToItem(Class clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance();
        return (MovingOne) object;
    }
}
