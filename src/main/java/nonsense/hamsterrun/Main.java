package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
                    case "keep-regenerating":
                        x++;
                        config.keepRegenerating = Boolean.valueOf(args[x]);
                        break;
                    default:
                        throw new RuntimeException("Unknown parameter " + args[x]);

                }
            }
        }
        SpritesProvider.load();
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
                JPanel mapPanel = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, 16);
                    }
                };
                JPanel view = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), false);
                    }
                };
                mapPanel.setBackground(Color.BLACK);
                view.setBackground(Color.BLACK);
                world.addRepaintListener(mapPanel);
                world.addRepaintListener(view);
                JFrame mapFrame = new JFrame();
                mapFrame.add(mapPanel);
                JFrame gameView = new JFrame();
                gameView.add(view);
                gameView.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        System.out.println(e.getKeyCode() + "");
                        if (e.getKeyCode() == 37/*leftarrow*/) {
                            world.setMyMouseLeft();
                        } else if (e.getKeyCode() == 38/*uparrow*/) {
                            world.setMyMouseUp();
                        } else if (e.getKeyCode() == 39/*rightarrow*/) {
                            world.setMyMouseRight();
                        } else if (e.getKeyCode() == 40/*downarrow*/) {
                            world.setMyMouseDown();
                        } else if (e.getKeyChar() == '+') {
                            world.zoomIn();
                        } else if (e.getKeyChar() == '-') {
                            world.zoomOut();
                        } else if (e.getKeyCode() >= 97 && e.getKeyCode() <= 105) {
                            int numlock = e.getKeyCode() - 97; //1 is zero for our needs;
                            world.regenerateBlock(numlock / 3, numlock % 3);
                        } else if (e.getKeyCode() == 16) {
                            world.allRatsSpread(true);
                        } else if (e.getKeyCode() == 20) {
                            world.allRatsSpread(false);
                        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            world.regenerateAll();
                        } else if (e.getKeyCode() >= 65 && e.getKeyCode() < 95) {
                            world.setMyMouse(e.getKeyCode() - 65);
                        }

                        gameView.repaint();
                    }
                });
                mapFrame.setSize(800, 800);
                mapFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                mapFrame.setVisible(true);
                gameView.setSize(800, 800);
                gameView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                gameView.setVisible(true);
            }
        });
    }
}
