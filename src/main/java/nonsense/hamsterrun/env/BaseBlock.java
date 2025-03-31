package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;
import nonsense.hamsterrun.sprites.Rats;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseBlock {
    public static final int WALL_WIDTH = 6;
    private final BlockField[][] map;
    private static final Random seed = new Random();
    private final int sizex;
    private final int sizey;
    private final int baseSize;

    public BaseBlock(int baseSize) {
        this.sizex = baseSize;
        this.sizey = baseSize; //relict for ancient times when non-square rectangle was allowed
        this.baseSize = baseSize;
        this.map = new BlockField[sizex][sizey];
        reset();
    }

    public static BaseBlock generateByNeighbours(BaseConfig config, BaseBlock west, BaseBlock east, BaseBlock north, BaseBlock south) {
        int vcon = config.getConnectivity();
        int hcon = config.getConnectivity();
        BaseBlock block = new BaseBlock(config.getBaseSize());
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
                if (map[x][y].isPassable() && seed.nextBoolean()) {
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

    private void reset() {
        Utils.clear(map, false);
    }

    public static BaseBlock generateMiddle(BaseConfig config) {
        return generateByNeighbours(config, null, null, null, null);
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
        return Utils.toString(map, null, null);
    }

    public BufferedImage toImage(int zoom, boolean map) {
        return Utils.toImage(this, zoom, map);
    }

    public void drawMap(int userx, int usery, int zoom, Graphics2D g2d, int level, boolean mapOnly) {
        mapOnly = false;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y].isPassable()) {
                    //this is aligning it with console and debugger output of [][]
                    int coordx = y * zoom + userx;
                    int coordy = x * zoom + usery;
                    if (mapOnly) {
                        g2d.setColor(map[x][y].getItem().getMinimapColor());
                        if (level == (map[x][y].getItem().getLevel())) {
                            g2d.fillRect(coordx, coordy, zoom, zoom);
                        }
                    } else {
                        //g2d.setColor(map[x][y].getItem().drawInto(g2d, coordx, coordy);
                    }
                    if (zoom > 2 && level == 1) {
                        if (mapOnly) {
                            g2d.setColor(new Color(255, 0, 0, 255));
                            g2d.drawRect(coordx, coordy, zoom - 1, zoom - 1);
                        } else {
                            //FIXME is ignoring surrounding BaseBlocks!
                            //left wall

                            if (y == 0 || map[x][y - 1].isImpassable()) {
                                g2d.drawImage(Rats.wall, coordx - (zoom / WALL_WIDTH), coordy, zoom / WALL_WIDTH - 1, zoom - 1, null);
                            }
                            //right wall
                            if (y == map[0].length - 1 || map[x][y + 1].isImpassable()) {
                                g2d.drawImage(Rats.wall, coordx + zoom, coordy, zoom / WALL_WIDTH - 1, zoom - 1, null);
                            }
                            //up wall
                            if (x == 0 || map[x - 1][y].isImpassable()) {
                                g2d.drawImage(Rats.wall, coordx, coordy - (zoom / WALL_WIDTH), zoom - 1, zoom / WALL_WIDTH - 1, null);
                            }
                            //down wall
                            if (x == map.length - 1 || map[x + 1][y].isImpassable()) {
                                g2d.drawImage(Rats.wall, coordx, coordy + zoom, zoom - 1, zoom / WALL_WIDTH - 1, null);
                            }
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
        do {
            x = seed.nextInt(map.length);
            y = seed.nextInt(map[x].length);
        } while (map[x][y] != null && !map[x][y].isPassable() && !map[x][y].isFree());
        System.out.println("p" + map[x][y].isPassable());
        System.out.println("f" + map[x][y].isPassable());
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
}