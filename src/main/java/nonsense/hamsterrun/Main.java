package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BaseConfig config = BaseConfig.baseConfig;
        for(int x = 0 ; x< args.length; x++){
            if (args[x].startsWith("-")) {
                String sanitized = args[x].replaceAll("^-+","").toLowerCase();
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
        BaseBlock middle = BaseBlock.generateMiddle(config);
        System.out.println(middle.toString());
        File f = new File("/tmp/block.png");
        ImageIO.write(middle.toImage(20), "png", f);
        Runtime.getRuntime().exec(new String[]{"eog", f.getAbsolutePath()});
        System.out.println("bye");
    }
}
