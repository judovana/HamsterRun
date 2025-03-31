package nonsense.hamsterrun.env;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseBlockNeigbours {
    private final int middleX;
    private final int middleY;
    private final BaseBlock center;
    BaseBlock up, right, down, left;

    public BaseBlockNeigbours(int originX, int originY, BaseBlock center) {
        this.middleX=originX;
        this.middleY=originY;
        this.center = center;
    }

    @Override
    public String toString() {
        int w = getBlockWidth();
        int h = getBlockHeight();
        if (w == -1 || h == -1) {
            return "NaN";
        }
        List<String> result = new ArrayList<>(h*3+1);
        result.add( middleX + " x " + middleY+" :");
        topAndBottom(up, result, w, h);
        for(int x = 0; x< h; x++){
            StringBuilder sb = new StringBuilder();
            sides(left, sb, w, x);
            sides(center, sb, w, x);
            sides(right, sb, w, x);
            result.add(sb.toString());
        }
        topAndBottom(down, result, w, h);
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
            for(int x = 0; x< h; x++){
                result.add(".".repeat(w *3));
            }
        } else {
            List<String> lines = unit.toStrings();
            for (String line: lines) {
                StringBuilder sb = new StringBuilder(".".repeat(w));
                sb.append(line);
                sb.append(".".repeat(w));
                result.add(sb.toString());
            }
        }
    }

    private BaseBlock[] asArray() {
        return new BaseBlock[]{up, right, down, left};
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
}
