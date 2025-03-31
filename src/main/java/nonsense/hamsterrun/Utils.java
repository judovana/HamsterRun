package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Maze;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static void clear(BlockField[][] map, boolean passable) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] == null) {
                    map[x][y] = new BlockField(passable);
                } else {
                    map[x][y].setPassable(passable);
                }
            }
        }
    }

    public static void column(BlockField[][] map, int y, boolean i) {
        for (int x = 0; x < map.length; x++) {
            map[x][y].setPassable(i);
        }
    }

    public static void row(BlockField[][] map, int x, boolean i) {
        for (int y = 0; y < map[x].length; y++) {
            map[x][y].setPassable(i);
        }
    }


    public static String toString(BlockField[][] map, Character ch1, Character ch2) {
        List<String> l = toStrings(map, ch1, ch2);
        return l.stream().collect(Collectors.joining("\n"));
    }

    /*
    00 10 12 ..
    01 11 12 ..
    02 ...
    ...
     */
    public static List<String> toStrings(BlockField[][] map, Character ch1, Character ch2) {
        List<String> sb = new ArrayList<>(map.length);
        for (int x = 0; x < map.length; x++) {
            StringBuilder line = new StringBuilder();
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y].isImpassable()) {
                    if (ch1 == null) {
                        line.append("" + (map[x][y].isPassable()?"0":"X"));
                    } else {
                        line.append("" + ch1);
                    }
                } else {
                    if (ch2 == null) {
                        line.append("" + (map[x][y].isPassable()?"0":"X"));
                    } else {
                        line.append("" + ch2);
                    }
                }
            }
            sb.add(line.toString());
        }
        return sb;
    }


    /*
    00 10 12 ..
    01 11 12 ..
    02 ...
    ...
     */
    public static BufferedImage toImage(BaseBlock map, int zoom, boolean mapOnly) {
        BufferedImage bi = new BufferedImage(map.getWidth() * zoom, map.getHeight() * zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        map.drawMap(0, 0, zoom, g2d, 1, mapOnly, null);
        map.drawMap(0, 0, zoom, g2d, 2, mapOnly, null);
        return bi;
    }


    public static BufferedImage toImage(Maze maze, int zoom, BaseConfig config, boolean map) {
        BufferedImage bi = new BufferedImage(maze.getWidth() * config.getBaseSize() * zoom, maze.getHeight() * config.getBaseSize() * zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        maze.drawMap(0, 0, zoom, config, g2d, 1, map);
        maze.drawMap(0, 0, zoom, config, g2d, 2, map);
        return bi;
    }


}
