package nonsense.hamsterrun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BaseConfig {
    int baseSize = 10;
    int baseDensityMin = 4;
    int baseDensityMax = 7;
    int gridSize = 5;
    int gridConnectivityMin = 1;
    int gridConnectivityMax = 4;
    boolean keepRegenerating = false;
    private static final Random seed = new Random();
    private List<String> rats = new ArrayList<>(10);

    BaseConfig() {
    }

    public BaseConfig(int baseSize, int baseDensityMin, int baseDensityMax, int gridSize, int gridConnectivityMin, int gridConnectivityMax) {
        this.baseSize = baseSize;
        this.baseDensityMin = baseDensityMin;
        this.baseDensityMax = baseDensityMax;
        this.gridSize = gridSize;
        this.gridConnectivityMin = gridConnectivityMin;
        this.gridConnectivityMax = gridConnectivityMax;
    }

    private static BaseConfig baseConfig = BaseConfig.small();

    public static BaseConfig getConfig() {
        return baseConfig;
    }

    private static BaseConfig dense() {
        return new BaseConfig();

    }

    private static BaseConfig small() {
        return new BaseConfig(10, 1, 2, 3, 1, 1);
    }

    private static BaseConfig sparse() {
        return new BaseConfig(10, 1, 2, 5, 1, 1);
    }

    private static BaseConfig normal() {
        return new BaseConfig(10, 2, 4, 5, 1, 2);
    }

    void summUp() {
        System.out.println("Each basic block will have size " + baseSize + " x " + baseSize);
        System.out.println("Each basic block will minimum of vertical bars " + baseDensityMin);
        System.out.println("Each basic block will minimum of horizontal bars " + baseDensityMin);
        System.out.println("Each basic block will maximum of vertical bars " + baseDensityMax);
        System.out.println("Each basic block will maximum of horizontal bars " + baseDensityMax);
        System.out.println("There will be grid of  " + gridSize + " x " + gridSize + " of base blocks");
        System.out.println("Each base element will be connected to each neighbour at least by  " + gridConnectivityMin + " lines");
        System.out.println("Each base element will be connected to each neighbour no more then  " + gridConnectivityMax + " lines");
        System.out.println("The max connectivity can no always be honoured, because the opposite nighbours may have no intersection. but engine is doing its best.");
        System.out.println("Constant regeneration of world is " + keepRegenerating);
        System.out.println("rats in world is " + rats.size() + "/" + rats.stream().collect(Collectors.joining(",")));
    }

    void verify() {
        if (baseSize <= 4) {
            throw new RuntimeException(" minimal size of base is  5. You set" + baseSize);
        }
        if (baseDensityMin >= baseSize) {
            throw new RuntimeException("base density min must be lower base size:  " + baseDensityMin + "!<" + baseSize);
        }
        if (baseDensityMax >= baseSize) {
            throw new RuntimeException("base density max must be lower base size:  " + baseDensityMax + "!<" + baseSize);
        }
        if (baseDensityMin > baseDensityMax) {
            throw new RuntimeException("base density min must be lower or equal to max, is not: " + baseDensityMin + "!<=" + baseDensityMax);
        }
        if (gridConnectivityMin > baseDensityMin) {
            throw new RuntimeException("connectivity min must be lower or equals  base density min " + gridConnectivityMin + "!<=" + baseDensityMin);
        }
        if (gridConnectivityMax > baseDensityMin) {
            throw new RuntimeException("connectivity max must be lower or equals  base density min " + gridConnectivityMax + "!<=" + baseDensityMin);
        }
        if (gridSize <= 2) {
            throw new RuntimeException(" minimal number of bases in grid is  3. You set" + gridSize);
        }
        if (gridConnectivityMin > baseDensityMax) {
            throw new RuntimeException("grid connectivity min must be lower or equal to max, is not: " + gridConnectivityMin + "!<=" + baseDensityMax);
        }
        if (gridSize % 2 == 0) {
            throw new RuntimeException("grid size must be odd. is not: " + gridSize);
        }
        if (rats.isEmpty()) {
            throw new RuntimeException("No mouses in world, add some!");
        }
        for (String rat : rats) {
            if (!(rat.equalsIgnoreCase("pc") ||
                    rat.equalsIgnoreCase("k1") ||
                    rat.equalsIgnoreCase("k2") ||
                    rat.equalsIgnoreCase("k3") ||
                    rat.equalsIgnoreCase("m"))) {
                throw new RuntimeException("Unknown rat def: " + rat + ". Use pc, k1, k2, k3 or m. Each can repeat unlimited times... but");
            }
        }
    }

    public int getBaseSize() {
        return baseSize;
    }

    public int getBaseDensityMin() {
        return baseDensityMin;
    }

    public int getBaseDensityMax() {
        return baseDensityMax;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getGridConnectivityMin() {
        return gridConnectivityMin;
    }

    public int getGridConnectivityMax() {
        return gridConnectivityMax;
    }

    private static int getBaseConfigRandom(int min, int max) {
        return seed.nextInt(max - min + 1) + min;
    }

    public int getDensity() {
        return getBaseConfigRandom(baseDensityMin, baseDensityMax);
    }

    public int getConnectivity() {
        return getBaseConfigRandom(gridConnectivityMin, gridConnectivityMax);
    }

    public boolean isKeepRegenerating() {
        return keepRegenerating;
    }

    public void addRat(String def) {
        rats.add(def);
    }

    public List<String> getRats() {
        return Collections.unmodifiableList(rats);
    }
}
