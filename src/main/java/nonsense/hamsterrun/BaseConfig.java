package nonsense.hamsterrun;

import nonsense.hamsterrun.sprites.SpritesProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BaseConfig {
    private static final Random seed = new Random();
    private static BaseConfig baseConfig = BaseConfig.small();
    int baseSize = 10;
    int baseDensityMin = 4;
    int baseDensityMax = 7;
    int gridSize = 5;
    int gridConnectivityMin = 1;
    int gridConnectivityMax = 4;
    int delayMs = 50;
    boolean keepRegenerating = true;
    int regSpeed = 200;
    private List<String> rats = new ArrayList<>(10);
    private int columns = 2;

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

    private static int getBaseConfigRandom(int min, int max) {
        return seed.nextInt(max - min + 1) + min;
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
        System.out.println("  note, syntax is as --rat control:skin:haveDisplay:aiModifier  eg  --rat k1:uhlicek:true  -rat pc:rat:false:10");
        System.out.println("  note, available skins are: " + SpritesProvider.KNOWN_RATS.stream().collect(Collectors.joining(", ")));
        System.out.println("  note, available controls  are: k1, k2, m1, pc");
        System.out.println("  note, display is true/false, and calcs if this rtat have its own window");
        System.out.println("  note, last field is for ai only, how weird it will be");
        System.out.println("main loop delay is " + delayMs + " bigger the number, slower the animations will be");
        System.out.println("regenerations speed is  " + regSpeed + " (sm,aller the number, more often it changes)");
        System.out.println("gui will have columns: " + columns);
    }

    void verify() {
        if (delayMs <= 1 || delayMs > 1000) {
            throw new RuntimeException(" main thead delay should be between 1 and 1000: " + delayMs);
        }
        if (regSpeed <= 1 || regSpeed > 1000) {
            throw new RuntimeException("regeneration should be between 1 and 1000: " + delayMs);
        }
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
        if (columns < 1 || columns>10) {
            throw new RuntimeException("To few, to much columns");
        }
        if (rats.isEmpty()) {
            //throw new RuntimeException("No mouses in world, add some!");
        }
        for (String ratDef : rats) {
            String[] rataParams = ratDef.split(":");
            for (int i = 0; i < rataParams.length; i++) {
                String param = rataParams[i];
                switch (i) {
                    case 3:
                        Integer.valueOf(param);
                        break;
                    case 2:
                        Boolean.valueOf(param);
                        break;
                    case 1:
                        if (SpritesProvider.KNOWN_RATS.contains(param)) {
                            //ok
                        } else {
                            throw new RuntimeException("Unknown sprite " + param + ". Available are " + SpritesProvider.KNOWN_RATS.stream().collect(Collectors.joining(",")));
                        }
                        break;
                    case 0:
                        if (!(param.equalsIgnoreCase("pc") ||
                                param.equalsIgnoreCase("k1") ||
                                param.equalsIgnoreCase("k2") ||
                                param.equalsIgnoreCase("k3") ||
                                param.equalsIgnoreCase("m"))) {
                            throw new RuntimeException("Unknown rat def: " + param + ". Use pc, k1, k2, k3 or m. Each can repeat unlimited times... but");
                        }
                        break;
                }
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

    public long getDelayMs() {
        return delayMs;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getColumns() {
        int views = getViews();
        if (views <= 1) {
            return 1;
        }
        return columns;
    }

    public int getViews() {
        int views = 0;
        for (String def : getRats()) {
            //fixme move to  object, its getting complicated
            String[] params = def.split(":");
            if (params.length > 2) {
                if (Boolean.valueOf(params[2])) {
                    views++;
                }
            }
        }
        return views;
    }

    public int getRegSpeed() {
        return regSpeed;
    }

    public void setRegSpeed(int regSpeed) {
        this.regSpeed = regSpeed;
    }
}
