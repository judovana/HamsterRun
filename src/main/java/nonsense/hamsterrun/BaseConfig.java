package nonsense.hamsterrun;

import nonsense.hamsterrun.env.aliens.BigBats;
import nonsense.hamsterrun.env.aliens.BigFlies;
import nonsense.hamsterrun.env.aliens.Boulder;
import nonsense.hamsterrun.env.aliens.Cat;
import nonsense.hamsterrun.env.aliens.Ghost;
import nonsense.hamsterrun.env.aliens.Hawk;
import nonsense.hamsterrun.env.aliens.Key;
import nonsense.hamsterrun.env.aliens.SmallBats;
import nonsense.hamsterrun.env.aliens.SmallFlies;
import nonsense.hamsterrun.env.traps.AllWayTeleport;
import nonsense.hamsterrun.env.traps.Cage;
import nonsense.hamsterrun.env.traps.Carrot;
import nonsense.hamsterrun.env.traps.ColorfullFlask;
import nonsense.hamsterrun.env.traps.Cucumber;
import nonsense.hamsterrun.env.traps.Empty;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.Grass;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Mushroom;
import nonsense.hamsterrun.env.traps.OneWayTeleport;
import nonsense.hamsterrun.env.traps.Pepper;
import nonsense.hamsterrun.env.traps.Repa;
import nonsense.hamsterrun.env.traps.Salat;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.TrapDoor;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.TwoWayTeleport;
import nonsense.hamsterrun.env.traps.Water;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class BaseConfig {

    private static final ItemsWithProbability[] DEFAULT_ITEMS_PROBABILITIES = new ItemsWithProbability[]{
            new ItemsWithProbability(Empty.class, 50),
            new ItemsWithProbability(Cucumber.class, 30),
            new ItemsWithProbability(Carrot.class, 15),
            new ItemsWithProbability(Pepper.class, 15),
            new ItemsWithProbability(Grass.class, 20),
            new ItemsWithProbability(Salat.class, 15),
            new ItemsWithProbability(Repa.class, 15),
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
            new ItemsWithProbability(Cage.class, 1),
    };

    private static final ItemsWithProbability[] DEFAULT_ALIENS_PROBABILITIES = new ItemsWithProbability[]{
            new ItemsWithProbability(SmallFlies.class, 50),
            new ItemsWithProbability(BigFlies.class, 50),
            new ItemsWithProbability(SmallBats.class, 20),
            new ItemsWithProbability(BigBats.class, 20),
            new ItemsWithProbability(Boulder.class, 35),
            new ItemsWithProbability(Hawk.class, 10),
            new ItemsWithProbability(Ghost.class, 5),
            new ItemsWithProbability(Cat.class, 15),
            new ItemsWithProbability(Key.class, 5),
    };

    private static final Random seed = new Random();
    private static BaseConfig baseConfig = BaseConfig.small();
    private final Map<Class, Integer> itemsWithProbabilityOverride = new HashMap<>();
    private final Map<Class, Integer> aliensWithProbabilityOverride = new HashMap<>();
    private int baseSize = 10;
    private int baseDensityMin = 4;
    private int baseDensityMax = 7;
    private int gridSize = 5;
    private int gridConnectivityMin = 1;
    private int gridConnectivityMax = 4;
    private int delayMs = 50;
    private boolean keepRegenerating = true;
    private int regSpeed = 200;
    private List<RatSetup> rats = new ArrayList<>(10);
    private int columns = 2;
    private int maxAliens = 10;
    //FIXME cmdline/gui setup
    private int tunnelConfusion = 20;
    private int mouseSensitivity = 200;
    //goal here should be, that the most score owning player, should wait in game, until weaker players enters, as the game ends when all rats are in cage
    private int cumulativeMinimalScoreToEnterGoldenGate= 50000;
    private int cumulativeMinimalNUmberOfKeys= 10; //if conditions are not met, then punish? Set by multiplying by rats count!
    private int individualMinimalScoreToEnterGoldenGate= 5000;
    //w and h ow space to draw to,
    // if the drawn object is out,
    // no need to draw it
    //the view port is get as whole window
    //to return the parts for each rat
    //it have to be divided by columns and rats
    private Point viewPort;

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

    static Class getTrapClassByName(String name) throws ClassNotFoundException {
        Class clazz = Class.forName(Empty.class.getPackageName() + "." + name);
        return clazz;
    }

    public void summUp() {
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
        System.out.println("rats in world is " + rats.size() + "/" + rats.stream().map(a -> a.toString()).collect(Collectors.joining(",")));
        System.out.println("  note, syntax is as --rat control:skin:haveDisplay:aiModifier  eg  --rat k1:uhlicek:true  -rat pc:rat:false:10");
        System.out.println("  note, available skins are: " + SpritesProvider.KNOWN_RATS.stream().collect(Collectors.joining(", ")));
        System.out.println("  note, available controls  are: k1, k2, m1, pc");
        System.out.println("  note, display is true/false, and calcs if this rtat have its own window");
        System.out.println("  note, last field is for ai only, how weird it will be");
        System.out.println("main loop delay is " + delayMs + " bigger the number, slower the animations will be");
        System.out.println("regenerations speed is  " + regSpeed + " (sm,aller the number, more often it changes)");
        System.out.println("gui will have columns: " + columns);
    }

    public void verify() {
        if (delayMs <= 1 || delayMs > 1000) {
            throw new RuntimeException(" main thead delay should be between 1 and 1000: " + delayMs);
        }
        if (regSpeed <= 1 || regSpeed > 1000) {
            throw new RuntimeException("regeneration should be between 1 and 1000: " + delayMs);
        }
        if (baseSize <= 4) {
            throw new RuntimeException(" minimal size of base is  5. You set" + baseSize);
        }
        if (baseDensityMin > baseDensityMax) {
            throw new RuntimeException("base density min must be lower or equal to max, is not: " + baseDensityMin + "!<=" + baseDensityMax);
        }
        if (baseDensityMin >= baseSize - 2) {
            throw new RuntimeException("base density min must be lower base size:  " + baseDensityMin + "!<" + (baseSize - 2));
        }
        if (baseDensityMax >= baseSize - 2) {
            throw new RuntimeException("base density max must be lower base size:  " + baseDensityMax + "!<" + (baseSize - 2));
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
        if (columns < 1 || columns > 10) {
            throw new RuntimeException("To few, to much columns");
        }
        if (rats.isEmpty()) {
            System.out.println("Warning, no rats, view only centered mode");
        }
    }

    public int getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(int baseSize) {
        this.baseSize = baseSize;
    }

    public int getBaseDensityMin() {
        return baseDensityMin;
    }

    public void setBaseDensityMin(int baseDensityMin) {
        this.baseDensityMin = baseDensityMin;
    }

    public int getBaseDensityMax() {
        return baseDensityMax;
    }

    public void setBaseDensityMax(int baseDensityMax) {
        this.baseDensityMax = baseDensityMax;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridConnectivityMin() {
        return gridConnectivityMin;
    }

    public void setGridConnectivityMin(int gridConnectivityMin) {
        this.gridConnectivityMin = gridConnectivityMin;
    }

    public int getGridConnectivityMax() {
        return gridConnectivityMax;
    }

    public void setGridConnectivityMax(int gridConnectivityMax) {
        this.gridConnectivityMax = gridConnectivityMax;
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

    public void setKeepRegenerating(boolean keepRegenerating) {
        this.keepRegenerating = keepRegenerating;
    }

    public void addRat(String def) {
        rats.add(RatSetup.parse(def));
    }

    public List<RatSetup> getRats() {
        return rats;
    }

    public void setRats(List<RatSetup> rats) {
        this.rats = rats;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(int delayMs) {
        this.delayMs = delayMs;
    }

    public int getColumnsDirect() {
        return columns;
    }

    public int getColumns() {
        int views = getViews();
        if (views <= 1) {
            return 1;
        }
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getViews() {
        int views = 0;
        for (RatSetup def : getRats()) {
            if (def.display) {
                views++;
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

    public int getTunnelConfusionFactor() {
        return tunnelConfusion;
    }

    public void setTunnelConfusion(int tunnelConfusion) {
        this.tunnelConfusion = tunnelConfusion;
    }

    public long getMouseDelay() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(int mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    public Integer getDefaultItemProbability(Class key) {
        return getDefaultItemProbabilityImpl(DEFAULT_ITEMS_PROBABILITIES, key);
    }
    public Integer getDefaultAlienProbability(Class key) {
        return getDefaultItemProbabilityImpl(DEFAULT_ALIENS_PROBABILITIES, key);
    }

    private static Integer getDefaultItemProbabilityImpl(ItemsWithProbability[] param, Class key) {
        List<ItemsWithProbability> items = new ArrayList<>(param.length);
        for (ItemsWithProbability origItem : param) {
            if (origItem.clazz.equals(key)) {
                return origItem.ratio;
            }
        }
        return null;
    }

    public void resetItemsProbabilities() {
        itemsWithProbabilityOverride.clear();
    }

    public void resetAliensProbabilities() {
        aliensWithProbabilityOverride.clear();
    }

    public List<ItemsWithProbability> getItemsProbabilities() {
        return getItemsProbabilitiesImpl(DEFAULT_ITEMS_PROBABILITIES, itemsWithProbabilityOverride);
    }

    public List<ItemsWithProbability> getAliensProbabilities() {
        return getItemsProbabilitiesImpl(DEFAULT_ALIENS_PROBABILITIES, aliensWithProbabilityOverride);
    }

    private static List<ItemsWithProbability> getItemsProbabilitiesImpl(ItemsWithProbability[] array, Map<Class, Integer> map) {
        List<ItemsWithProbability> items = new ArrayList<>(array.length);
        for (ItemsWithProbability origItem : array) {
            Integer i = map.get(origItem.clazz);
            if (i == null) {
                items.add(new ItemsWithProbability(origItem.clazz, origItem.ratio));
            } else {
                items.add(new ItemsWithProbability(origItem.clazz, i));
            }
        }
        return items;
    }

    public void addTrapModifier(String arg, boolean alien) {
        try {
            addTrapModifierThrows(arg, alien);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addTrapModifierThrows(String arg, boolean alien) throws ClassNotFoundException {
        String[] nameAndProbability = arg.split(":");
        Class clazz = getTrapClassByName(nameAndProbability[0]);
        if (nameAndProbability.length == 1) {
            addTrapModifierSafe(clazz, 0, alien);
        } else {
            addTrapModifierSafe(clazz, Integer.valueOf(nameAndProbability[1]), alien);
        }
    }

    public void addTrapModifierSafe(Class clazz, Integer prob, boolean alien) {
        if (alien) {
            aliensWithProbabilityOverride.put(clazz, Integer.valueOf(prob));
        } else {
            itemsWithProbabilityOverride.put(clazz, Integer.valueOf(prob));
        }

    }

    public void setWholeViewPort(int width, int height) {
        this.viewPort = new Point(width, height);
    }

    public Point getPartialViewPort() {
        int wPart = viewPort.x / getColumns();
        int hPart = viewPort.y / (getViews() / getColumns() + 1);
        return new Point(wPart, hPart);
    }

    public boolean haveViewPort() {
        return viewPort != null;
    }

    public void disbaleAllItems() {
        disbaleAllItemsImpl(DEFAULT_ITEMS_PROBABILITIES, itemsWithProbabilityOverride);
    }

    public void disbaleAllAliens() {
        disbaleAllItemsImpl(DEFAULT_ALIENS_PROBABILITIES, aliensWithProbabilityOverride);
    }

    private static void disbaleAllItemsImpl(ItemsWithProbability[] array, Map<Class, Integer> map) {
        map.clear();
        for (ItemsWithProbability origItem : array) {
            if (!origItem.clazz.equals(Empty.class) ) {
                map.put(origItem.clazz, 0);
            }
        }
    }

    public static class ItemsWithProbability {
        //0 == disabled
        public final Class clazz;
        public final int ratio;

        public ItemsWithProbability(Class clazz, int ratio) {
            this.clazz = clazz;
            this.ratio = ratio;
        }
    }

    public int getMaxAliens() {
        return maxAliens;
    }

    public void setMaxAliens(int maxAliens) {
        this.maxAliens = maxAliens;
    }
}
