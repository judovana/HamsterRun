package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BaseBlock {

    public static final int WALL_WIDTH = 6;
    private static final Random seed = new Random();
    private final BlockField[][] map;
    private final int sizex;
    private final int sizey;
    private final int baseSize;
    private final Point coords;

    public BaseBlock(int baseSize, Point coords) {
        this.sizex = baseSize;
        this.sizey = baseSize; //relict for ancient times when non-square rectangle was allowed
        this.baseSize = baseSize;
        this.coords = coords;
        this.map = new BlockField[sizex][sizey];
        reset();
    }

    public static BaseBlock generateByNeighbours(BaseConfig config, BaseBlock west, BaseBlock east, BaseBlock north, BaseBlock south, Point coord) {
        int vcon = config.getConnectivity();
        int hcon = config.getConnectivity();
        BaseBlock block = new BaseBlock(config.getBaseSize(), coord);
        if (west != null || east != null) {
            while (block.getRows().size() < hcon) {
                setConnectingRow(west, block);
                setConnectingRow(east, block);
            }
        }
        if (north != null || south != null) {
            while (block.getColumns().size() < vcon) {
                setConnectingColumn(north, block);
                setConnectingColumn(south, block);
            }
        }
        int vdens = config.getDensity();
        int hdens = config.getDensity();
        while (block.getColumns().size() < vdens) {
            block.setRandomColumn();
        }
        while (block.getRows().size() < hdens) {
            block.setRandomRow();
        }
        setObstacles(block.map);
        return block;
    }

    public static void setObstacles(BlockField[][] map) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y].isPassable()) {
                    map[x][y].setRandomObstacle(seed);
                }
            }
        }
    }

    private static void setConnectingRow(BaseBlock hNeighbour, BaseBlock block) {
        if (hNeighbour != null) {
            List<Integer> neighbourRows = hNeighbour.getRows();
            int connection = neighbourRows.get(seed.nextInt(neighbourRows.size()));
            block.setRow(connection);
        }
    }

    private static void setConnectingColumn(BaseBlock vNeighbour, BaseBlock block) {
        if (vNeighbour != null) {
            List<Integer> neighbourColumns = vNeighbour.getColumns();
            int connection = neighbourColumns.get(seed.nextInt(neighbourColumns.size()));
            block.setColumn(connection);
        }
    }

    public static BaseBlock generateMiddle(BaseConfig config, Point coord) {
        return generateByNeighbours(config, null, null, null, null, coord);
    }

    private void reset() {
        Utils.clear(map, this, false);
    }

    private List<Integer> getRows() {
        List<Integer> columns = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            if (map[x][0].isPassable()) {
                columns.add(x);
            }
        }
        return columns;
    }

    private List<Integer> getColumns() {
        List<Integer> rows = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            if (map[0][y].isPassable()) {
                rows.add(y);
            }
        }
        return rows;
    }

    //borders must be 0
    private void setRandomColumn() {
        setColumn(seed.nextInt(sizey - 2) + 1);
    }

    private void setColumn(int i) {
        setColumn(i, true);
    }

    private void setColumn(int i, boolean value) {
        Utils.column(map, i, value);
    }

    //borders must be 0
    private void setRandomRow() {
        setRow(seed.nextInt(sizex - 2) + 1);
    }

    private void setRow(int i) {
        setRow(i, true);
    }

    private void setRow(int i, boolean value) {
        Utils.row(map, i, value);
    }

    @Override
    public String toString() {
        return toString(null, null);
    }

    public String toString(Character ch1, Character ch2) {
        List<String> l = toStrings(ch1, ch2);
        return l.stream().collect(Collectors.joining("\n"));
    }

    public List<String> toStrings() {
        return toStrings(null, null);
    }

    /* without rotation:
    00 10 12 ..
    01 11 12 ..
    02 ...
    ...
    with rotation it is even wirder
     */

    public List<String> toStrings(Character ch1, Character ch2) {
        List<String> sb = new ArrayList<>(map.length);
        for (int xop = 0; xop < map.length; xop++) {
            StringBuilder line = new StringBuilder();
            for (int yop = 0; yop < map[xop].length; yop++) {
                int xcoord, ycoord;
                xcoord = xop;
                ycoord = yop;
                if (map[xcoord][ycoord].isImpassable()) {
                    if (ch1 == null) {
                        line.append("" + (map[xcoord][ycoord].isPassable() ? "0" : "X"));
                    } else {
                        line.append("" + ch1);
                    }
                } else {
                    if (ch2 == null) {
                        line.append("" + (map[xcoord][ycoord].isPassable() ? "0" : "X"));
                    } else {
                        line.append("" + ch2);
                    }
                }
            }
            sb.add(line.toString());
        }
        return sb;
    }

    public BufferedImage toImage(int zoom, boolean map) {
        return Utils.toImage(this, zoom, map);
    }

    public void drawMap(int userx, int usery, int zoom, Graphics2D g2d, int level, boolean mapOnly, BaseBlockNeigbours neigbours) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y].isPassable()) {
                    //this is aligning it with console and debugger output of [][]
                    int coordx = y * zoom + userx;
                    int coordy = x * zoom + usery;
                    if (BaseConfig.getConfig().haveViewPort()) {
                        if (coordx < 0 - 2 * zoom || coordy < 0 - 2 * zoom) {
                            continue; //nothing should draw more then one field around
                        }
                        if (coordx > BaseConfig.getConfig().getPartialViewPort().x+2*zoom || coordy > BaseConfig.getConfig().getPartialViewPort().x+2*zoom) {
                            continue; //nothing should draw more then one field around
                        }
                    }
                    if (mapOnly) {
                        g2d.setColor(map[x][y].getItem().getMinimapColor());
                        //on maps, all items are drawn before rats
                        if (level == 2) {
                            g2d.fillRect(coordx, coordy, zoom, zoom);
                        }
                    } else {
                        if (level == 1) {
                            g2d.drawImage(SpritesProvider.getFloor(zoom), coordx, coordy, zoom, zoom, null);
                        }
                        map[x][y].getItem().drawInto(g2d, coordx, coordy, zoom, level, neigbours, x, y);
                    }
                    if (zoom > 2 && level == 2) {
                        if (mapOnly) {
                            g2d.setColor(new Color(255, 0, 0, 255));
                            g2d.drawRect(coordx, coordy, zoom - 1, zoom - 1);
                        }
                    }
                    if (zoom > 2 && level == 1) {
                        if (!mapOnly) {
                            BlockField lb = neigbours.getLeftField(x, y);
                            if (lb == null || lb.isImpassable()) {
                                g2d.drawImage(SpritesProvider.wall, coordx - (zoom / WALL_WIDTH), coordy, zoom / WALL_WIDTH - 1, zoom - 1, null);
                            }
                            BlockField rb = neigbours.getRightField(x, y);
                            if (rb == null || rb.isImpassable()) {
                                g2d.drawImage(SpritesProvider.wall, coordx + zoom, coordy, zoom / WALL_WIDTH - 1, zoom - 1, null);
                            }
                            BlockField ub = neigbours.getUpField(x, y);
                            if (ub == null || ub.isImpassable()) {
                                g2d.drawImage(SpritesProvider.wall, coordx, coordy - (zoom / WALL_WIDTH), zoom - 1, zoom / WALL_WIDTH - 1, null);
                            }
                            BlockField db = neigbours.getDownField(x, y);
                            if (db == null || db.isImpassable()) {
                                g2d.drawImage(SpritesProvider.wall, coordx, coordy + zoom, zoom - 1, zoom / WALL_WIDTH - 1, null);
                            }
                        }
                    }
                    if (zoom > 10 && level == 4) {
                        if (!mapOnly) {
                            g2d.drawString(map[x][y].getCoords() + "", coordx, coordy + 10);
                            g2d.drawString(map[x][y].getParent().getCoords() + "", coordx, coordy + 20);
                            g2d.drawString(Rat.toUniversalCoords(map[x][y].getParent().getCoords(), map[x][y].getCoords()) + "",
                                    coordx, coordy + 30);
                        }
                    }
                }
            }
        }
    }

    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }

    public Point getRandomSafeSpot() {
        int x = -1;
        int y = -1;
        int attempts = 100;
        do {
            attempts--;
            if (attempts == 0) {
                break;
            }
            x = seed.nextInt(map.length);
            y = seed.nextInt(map[x].length);
        } while (map[x][y] != null && !map[x][y].isPassable() && !map[x][y].isFree());
        if (attempts == 0) {
            attempts = 100;
            do {
                attempts--;
                if (attempts == 0) {
                    break;
                }
                x = seed.nextInt(map.length);
                y = seed.nextInt(map[x].length);
            } while (map[x][y] != null && !map[x][y].isPassable());
        }
        return new Point(x, y);
    }

    public BlockField get(int x, int y) {
        if (x < 0 || x >= map[0].length) {
            return null;
        }
        if (y < 0 || y >= map.length) {
            return null;
        }
        return map[x][y];
    }

    public Point getCoords() {
        return coords;
    }
}