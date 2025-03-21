package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;
import nonsense.hamsterrun.env.BlockField;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static void clear(BlockField[][] map, boolean passable) {
        for(int x = 0; x< map.length; x++){
            for(int y=0; y<map[x].length; y++) {
                if (map[x][y] == null) {
                    map[x][y] = new BlockField(passable);
                } else {
                    map[x][y].setPassable(passable);
                }
            }
        }
    }

    public static void column(BlockField[][] map, int y, boolean i) {
        for(int x = 0; x< map.length; x++){
                map[x][y].setPassable(i);
        }
    }

    public static void row(BlockField[][] map, int x, boolean i) {
            for(int y=0; y<map[x].length; y++) {
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
        for(int x = 0; x< map.length; x++){
            StringBuilder line = new StringBuilder();
            for(int y=0; y<map[x].length; y++) {
                if (map[x][y].isImpassable()) {
                    if (ch1 == null) {
                        line.append("" + map[x][y]);
                    } else {
                        line.append("" + ch1);
                    }
                } else {
                    if (ch2 == null) {
                        line.append("" + map[x][y]);
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
    public static BufferedImage toImage(BlockField[][] map, int zoom) {
        BufferedImage bi = new BufferedImage(map[0].length * zoom, map.length * zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        draw(map, zoom, g2d);
        return bi;
    }

    public static void draw(BlockField[][] map, int zoom, Graphics2D g2d) {
        drawTo(0, 0, map, zoom, g2d);
    }

    public static void drawTo(int userx, int usery, BlockField[][] map, int zoom, Graphics2D g2d) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y].isImpassable()) {
                    g2d.setColor(new Color(0,0,0,255));
                } else {
                    g2d.setColor(new Color(255,255,255,255));
                }
                //this is aligning it with console and debugger output of [][]
                int coordx = y*zoom + userx;
                int coordy = x*zoom + usery;
                g2d.fillRect(coordx, coordy, zoom, zoom);
                if (zoom>2) {
                    g2d.setColor(new Color(255,0,0,255));
                    g2d.drawRect(coordx, coordy, zoom-1, zoom-1);
                }
            }
        }
    }

    public static BufferedImage toImage(BaseBlock[][] map, int zoom, BaseConfig config) {
        BufferedImage bi = new BufferedImage(map[0].length * config.getBaseSize() * zoom, map.length * config.getBaseSize() * zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        draw(map, zoom, config, g2d);
        return bi;
    }

    public static void draw(BaseBlock[][] map, int zoom, BaseConfig config, Graphics2D g2d) {
        drawTo(0,0, map, zoom, config, g2d);
    }

    public static void drawTo(int userx, int usery, BaseBlock[][] map, int zoom, BaseConfig config, Graphics2D g2d) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] != null) {
                    int coordx = y * config.getBaseSize() * zoom + userx;
                    int coordy = x * config.getBaseSize() * zoom + usery;
                    //g2d.drawImage(map[x][y].toImage(zoom), coordx, coordy, null);
                    map[x][y].drawTo(g2d, zoom, coordx, coordy);
                }

            }
        }
    }
}
