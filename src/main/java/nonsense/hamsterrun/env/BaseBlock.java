package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseBlock {
    //TODO replace int by object Field
    private final int[][] map;
    private static final Random seed = new Random();
    private final int sizex;
    private final int sizey;
    private final int baseSize;

    public BaseBlock(int baseSize) {
        this.sizex = baseSize;
        this.sizey = baseSize; //relict for ancient times when non-square rectangle was allowed
        this.baseSize = baseSize;
        this.map = new int[sizex][sizey];
        reset();
    }

    public static BaseBlock generateByNeighours(BaseConfig config, BaseBlock west, BaseBlock east, BaseBlock north, BaseBlock south) {
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
        return block;
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
        Utils.clear(map, 0);
    }

    public static BaseBlock generateMiddle(BaseConfig config) {
        return generateByNeighours(config, null, null, null, null);
    }

    private List<Integer> getRows() {
        List<Integer> columns = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            if (map[x][0] > 0) {
                columns.add(x);
            }
        }
        return columns;
    }

    private List<Integer> getColumns() {
        List<Integer> rows = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            if (map[0][y] > 0) {
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
        setColumn(i, 1);
    }

    private void setColumn(int i, int value) {
        Utils.column(map, i, value);
    }

    //borders must be 0
    private void setRandomRow() {
        setRow(seed.nextInt(sizex - 2) + 1);
    }

    private void setRow(int i) {
        setRow(i, 1);
    }

    private void setRow(int i, int value) {
        Utils.row(map, i, value);
    }

    @Override
    public String toString() {
        return Utils.toString(map, null, null);
    }

    public BufferedImage toImage(int zoom) {
        return Utils.toImage(map, zoom);
    }
}