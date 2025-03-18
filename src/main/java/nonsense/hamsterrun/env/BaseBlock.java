package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BaseBlock {
    private final int[][] map;
    private static final Random seed = new Random();
    private final int sizex;
    private final int sizey;
    private final int baseSize;

    public BaseBlock(int baseSize) {
        this.sizex = baseSize;
        this.sizey = baseSize; //relict for ancient times when non-square rectangle was allowed
         this.baseSize=baseSize;
        this.map = new int[sizex][sizey];
        reset();
    }

    private void reset() {
        Utils.clear(map, 0);
    }

    public static BaseBlock generateMiddle(BaseConfig config) {
        int vdens = config.getDensity();
        int hdens = config.getDensity();
        BaseBlock block = new BaseBlock(config.getBaseSize());
        while (block.getColumns().size() < vdens) {
            block.setRandomColumn();
        }
        while (block.getRows().size() < hdens) {
            block.setRandomRow();
        }
        return block;
    }

    private Collection<Integer> getRows() {
        List<Integer> columns = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            if (map[x][0] > 0) {
                columns.add(x);
            }
        }
        return columns;
    }

    private Collection<Integer> getColumns() {
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