package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;
import nonsense.hamsterrun.env.Maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BaseConfig config = BaseConfig.baseConfig;
        for (int x = 0; x < args.length; x++) {
            if (args[x].startsWith("-")) {
                String sanitized = args[x].replaceAll("^-+", "").toLowerCase();
                switch (sanitized) {
                    case "base-size":
                        x++;
                        config.baseSize = Integer.valueOf(args[x]);
                        System.out.println("Each basic block will have size " + config.baseSize + " x " + config.baseSize);
                        break;
                    case "base-density-min":
                        x++;
                        config.baseDensityMin = Integer.valueOf(args[x]);
                        break;
                    case "base-density-max":
                        x++;
                        config.baseDensityMax = Integer.valueOf(args[x]);
                        break;
                    case "grid-size":
                        x++;
                        config.gridSize = Integer.valueOf(args[x]);
                        break;
                    case "grid-connectivity-min":
                        x++;
                        config.gridConnectivityMin = Integer.valueOf(args[x]);
                        break;
                    case "grid-connectivity-max":
                        x++;
                        config.gridConnectivityMax = Integer.valueOf(args[x]);
                        break;
                    default:
                        throw new RuntimeException("Unknown parameter " + args[x]);

                }
            }
        }
        config.summUp();
        config.verify();

        mazeDemo(config);
        //baseBlockDemo(config);

        System.out.println("bye");
    }

    private static void mazeDemo(BaseConfig config) throws IOException {
        Maze maze = Maze.generate(config);
        BufferedImage bi = maze.toImage(10, config);
        File f = new File("/tmp/mazr.png");
        ImageIO.write(bi, "png", f);
        ProcessBuilder pb = new ProcessBuilder("eog", f.getAbsolutePath());
        pb.start();
    }

    private static void baseBlockDemo(BaseConfig config) throws IOException {
        BaseBlock middle = BaseBlock.generateMiddle(config);
        BaseBlock right = BaseBlock.generateByNeighours(config, middle, null, null, null);
        BaseBlock left = BaseBlock.generateByNeighours(config, null, middle, null, null);
        BaseBlock up = BaseBlock.generateByNeighours(config, null, null, null, middle);
        BaseBlock down = BaseBlock.generateByNeighours(config, null, null, middle, null);
        show(middle, "mid");
        show(up, "up");
        show(down, "down");
        show(left, "left");
        show(right, "right");
        BaseBlock newMiddle = BaseBlock.generateByNeighours(config, left, right, up, down);
        show(newMiddle, "nwm");
        BaseBlock leftup = BaseBlock.generateByNeighours(config, up, null, null, left);
        show(leftup, "leftup");
        BaseBlock righttup = BaseBlock.generateByNeighours(config, null, up, null, right);
        show(righttup, "righttup");
        BaseBlock leftdown = BaseBlock.generateByNeighours(config, down, null, left, null);
        show(leftdown, "leftdown");
        BaseBlock rightdown = BaseBlock.generateByNeighours(config, null, down, right, null);
        show(rightdown, "rightdown");
    }

    private static void show(BaseBlock middle, String id) throws IOException {
        System.out.println("------- start " + id + " start -------");
        System.out.println(middle.toString());
        System.out.println("------- end " + id + " end -------");
        File f = new File("/tmp/" + id + "block.png");
        ImageIO.write(middle.toImage(20), "png", f);
        ProcessBuilder pb = new ProcessBuilder("eog", f.getAbsolutePath());
        pb.start();
    }
}
