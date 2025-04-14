package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseBlockNeigbours {
    private final int middleX;
    private final int middleY;
    private final BaseBlock center;
    private BaseBlock up, right, down, left;

    public BaseBlockNeigbours(int originX, int originY, BaseBlock center) {
        this.middleX = originX;
        this.middleY = originY;
        this.center = center;
    }

    @Override
    public String toString() {
        int w = getBlockWidth();
        int h = getBlockHeight();
        if (w == -1 || h == -1) {
            return "NaN";
        }
        List<String> result = new ArrayList<>(h * 3 + 1);
        topAndBottom(up, result, w, h);
        for (int x = 0; x < h; x++) {
            StringBuilder sb = new StringBuilder();
            sides(left, sb, w, x);
            sides(center, sb, w, x);
            sides(right, sb, w, x);
            result.add(sb.toString());
        }
        topAndBottom(down, result, w, h);
        result.add(0, middleX + " x " + middleY + " :");
        return result.stream().collect(Collectors.joining("\n"));
    }


    private static void sides(BaseBlock unit, StringBuilder sb, int w, int x) {
        if (unit == null) {
            sb.append(".".repeat(w));
        } else {
            sb.append(unit.toStrings().get(x));
        }
    }

    private static void topAndBottom(BaseBlock unit, List<String> result, int w, int h) {
        if (unit == null) {
            for (int x = 0; x < h; x++) {
                result.add(".".repeat(w * 3));
            }
        } else {
            List<String> lines = unit.toStrings();
            for (String line : lines) {
                StringBuilder sb = new StringBuilder(".".repeat(w));
                sb.append(line);
                sb.append(".".repeat(w));
                result.add(sb.toString());
            }
        }
    }

    private BaseBlock[] asArray() {
        return new BaseBlock[]{up, right, down, left, center};
    }

    private int getBlockWidth() {
        for (BaseBlock block : asArray()) {
            if (block != null) {
                return block.getWidth();
            }
        }
        return -1;
    }

    private int getBlockHeight() {
        for (BaseBlock block : asArray()) {
            if (block != null) {
                return block.getHeight();
            }
        }
        return -1;
    }

    public int getMiddleX() {
        return middleX;
    }

    public int getMiddleY() {
        return middleY;
    }

    public BaseBlock getCenter() {
        return center;
    }

    public BaseBlock getUp() {
        return up;
    }

    public BaseBlock getRight() {
        return right;
    }

    public BaseBlock getDown() {
        return down;
    }

    public BaseBlock getLeft() {
        return left;
    }

    public void setUp(BaseBlock up, int x, int y) {
        this.up = up;
    }

    public void setRight(BaseBlock right, int x, int y) {
        this.right = right;
    }

    public void setDown(BaseBlock down, int x, int y) {
        this.down = down;
    }

    public void setLeft(BaseBlock left, int x, int y) {
        this.left = left;
    }

    public BlockField getLeftField(int x, int y) {
        if (y == 0) {
            if (left == null) {
                return null;
            } else {
                return left.get(x, BaseConfig.getConfig().getBaseSize() - 1);
            }
        } else {
            return center.get(x, y - 1);
        }
    }

    public BlockField getRightField(int x, int y) {
        if (y == BaseConfig.getConfig().getBaseSize() - 1) {
            if (right == null) {
                return null;
            } else {
                return right.get(x, 0);
            }
        } else {
            return center.get(x, y + 1);
        }
    }

    public BlockField getDownField(int x, int y) {
        if (x == BaseConfig.getConfig().getBaseSize() - 1) {
            if (down == null) {
                return null;
            } else {
                return down.get(0, y);
            }
        } else {
            return center.get(x + 1, y);
        }
    }

    public BlockField getUpField(int x, int y) {
        if (x == 0) {
            if (up == null) {
                return null;
            } else {
                return up.get(BaseConfig.getConfig().getBaseSize() - 1, y);
            }
        } else {
            return center.get(x - 1, y);
        }
    }
}
