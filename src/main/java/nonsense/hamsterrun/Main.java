package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.World;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BaseConfig config = BaseConfig.getConfig();
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
        Rats.load();
        config.summUp();
        config.verify();

        worldDemo();
        //frameDemo(config);
        //mazeDemo(config);
        //baseBlockDemo(config);

        System.out.println("bye");
    }

    private static void worldDemo() {
        final World world = new World(Maze.generate(BaseConfig.getConfig()));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel view = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.draw(g2d);
                    }
                };
                view.setBackground(Color.BLACK);
                JFrame f = new JFrame();
                f.add(view);
                f.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        System.out.println(e.getKeyCode() + "");
                        if (e.getKeyCode() == 37/*leftarrow*/) {
                            world.moveMyMouseLeft();
                        }
                        if (e.getKeyCode() == 38/*uparrow*/) {
                            world.moveMyMouseUp();
                        }
                        if (e.getKeyCode() == 39/*rightarrow*/) {
                            world.moveMyMouseRight();
                        }
                        if (e.getKeyCode() == 40/*downarrow*/) {
                            world.moveMyMouseDown();
                        }
                        if (e.getKeyChar() == '+') {
                            world.zoomIn();
                        }
                        if (e.getKeyChar() == '-') {
                            world.zoomOut();
                        }
                        if (e.getKeyCode() >= 97 && e.getKeyCode() <= 105) {
                            int numlock = e.getKeyCode() - 97; //1 is zero for our needs;
                            world.regenerateBlock(numlock / 3, numlock % 3);
                        }
                        f.repaint();
                    }
                });
                f.setSize(800, 800);
                f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                f.setVisible(true);
            }
        });

    }

    private static void frameDemo(BaseConfig config) {
        final Maze maze = Maze.generate(config);
        final int[] xyz = new int[]{1, 5, 20};
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel view = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        maze.drawMapLevel1(xyz[0] * xyz[2], xyz[1] * xyz[2], xyz[2], config, g2d);
                    }
                };
                view.setBackground(Color.BLACK);
                JFrame f = new JFrame();
                f.add(view);
                f.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        System.out.println(e.getKeyCode() + "");
                        if (e.getKeyCode() == 37/*leftarrow*/) {
                            xyz[0]++;
                        }
                        if (e.getKeyCode() == 38/*uparrow*/) {
                            xyz[1]++;
                        }
                        if (e.getKeyCode() == 39/*rightarrow*/) {
                            xyz[0]--;
                        }
                        if (e.getKeyCode() == 40/*downarrow*/) {
                            xyz[1]--;
                        }
                        if (e.getKeyChar() == '+') {
                            xyz[2]++;
                        }
                        if (e.getKeyChar() == '-') {
                            xyz[2]--;
                        }
                        if (e.getKeyCode() >= 97 && e.getKeyCode() <= 105) {
                            int numlock = e.getKeyCode() - 97; //1 is zero for our needs;
                            maze.regenerate(numlock / 3, numlock % 3, config);
                        }
                        f.repaint();
                    }
                });
                f.setSize(700, 700);
                f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                f.setVisible(true);
            }
        });

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
        BaseBlock right = BaseBlock.generateByNeighbours(config, middle, null, null, null);
        BaseBlock left = BaseBlock.generateByNeighbours(config, null, middle, null, null);
        BaseBlock up = BaseBlock.generateByNeighbours(config, null, null, null, middle);
        BaseBlock down = BaseBlock.generateByNeighbours(config, null, null, middle, null);
        show(middle, "mid");
        show(up, "up");
        show(down, "down");
        show(left, "left");
        show(right, "right");
        BaseBlock newMiddle = BaseBlock.generateByNeighbours(config, left, right, up, down);
        show(newMiddle, "nwm");
        BaseBlock leftup = BaseBlock.generateByNeighbours(config, up, null, null, left);
        show(leftup, "leftup");
        BaseBlock righttup = BaseBlock.generateByNeighbours(config, null, up, null, right);
        show(righttup, "righttup");
        BaseBlock leftdown = BaseBlock.generateByNeighbours(config, down, null, left, null);
        show(leftdown, "leftdown");
        BaseBlock rightdown = BaseBlock.generateByNeighbours(config, null, down, right, null);
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
