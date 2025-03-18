package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static void clear(int[][] map, int i) {
        for(int x = 0; x< map.length; x++){
            for(int y=0; y<map[x].length; y++) {
                map[x][y] = i;
            }
        }
    }

    public static void column(int[][] map, int y,  int i) {
        for(int x = 0; x< map.length; x++){
                map[x][y] = i;
        }
    }

    public static void row(int[][] map, int x, int i) {
            for(int y=0; y<map[x].length; y++) {
                map[x][y] = i;
        }
    }


    public static String toString(int[][] map, Character ch1, Character ch2) {
        List<String> l = toStrings(map, ch1, ch2);
        return l.stream().collect(Collectors.joining("\n"));
    }

    /*
    00 10 12 ..
    01 11 12 ..
    02 ...
    ...
     */
    public static List<String> toStrings(int[][] map, Character ch1, Character ch2) {
        List<String> sb = new ArrayList<>(map.length);
        for(int x = 0; x< map.length; x++){
            StringBuilder line = new StringBuilder();
            for(int y=0; y<map[x].length; y++) {
                if (map[x][y]==0) {
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
    public static BufferedImage toImage(int[][] map, int zoom) {
        BufferedImage bi = new BufferedImage(map[0].length * zoom, map.length * zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] == 0) {
                    g2d.setColor(new Color(0,0,0,255));
                } else {
                    g2d.setColor(new Color(255,255,255,255));
                }
                //this is aligning it with console and debugger output of [][]
                int coordx = y*zoom;
                int coordy = x*zoom;
                g2d.fillRect(coordx, coordy, zoom, zoom);
                if (zoom>2) {
                    g2d.setColor(new Color(255,0,0,255));
                    g2d.drawRect(coordx, coordy, zoom-1, zoom-1);
                }
            }
        }
        return bi;
    }

    public static BufferedImage toImage(BaseBlock[][] map, int zoom, BaseConfig config) {
        BufferedImage bi = new BufferedImage(map[0].length * config.getBaseSize() * zoom, map.length * config.getBaseSize() * zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] != null) {
                    int coordx = y * config.getBaseSize() * zoom;
                    int coordy = x * config.getBaseSize() * zoom;
                    g2d.drawImage(map[x][y].toImage(zoom), coordx, coordy, null);
                }

            }
        }
        return bi;
    }
}
