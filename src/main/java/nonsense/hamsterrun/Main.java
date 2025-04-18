package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BaseBlock;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.RatsController;
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

        System.out.println("bye");
    }

    private static void worldDemo() {
        final World world = new World(Maze.generate(BaseConfig.getConfig()));
        final RatsController ratsController = new RatsController();
        Rat demoRat = new Rat();
        ratsController.addRat(new RatsController.RatWithControls(demoRat, new RatsController.KeyboardControl1()));
        ratsController.addRat(new RatsController.RatWithControls(new Rat(), new RatsController.KeyboardControl1()));//intentional experiment
        ratsController.addRat(new RatsController.RatWithControls(new Rat(), new RatsController.ComputerControl()));
        ratsController.addRat(new RatsController.RatWithControls(new Rat(), new RatsController.ComputerControl()));
        world.setRatsProvider(ratsController);
        world.allRatsSpread(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel mapPanel = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, 16, demoRat);
                    }
                };
                JPanel view = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), false, ratsController.getRatControl(demoRat).getZoom(), demoRat);
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
                            demoRat.setMouseLeft();
                        } else if (e.getKeyCode() == 38/*uparrow*/) {
                            demoRat.setMouseUp();
                        } else if (e.getKeyCode() == 39/*rightarrow*/) {
                            demoRat.setMouseRight();
                        } else if (e.getKeyCode() == 40/*downarrow*/) {
                            demoRat.setMouseDown();
                        } else if (e.getKeyChar() == '+') {
                            ratsController.getRatControl(demoRat).zoomIn();
                        } else if (e.getKeyChar() == '-') {
                            ratsController.getRatControl(demoRat).zoomOut();
                        } else if (e.getKeyCode() == 16) {
                            world.allRatsSpread(true);
                        } else if (e.getKeyCode() == 20) {
                            world.allRatsSpread(false);
                        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            world.regenerateAll();
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
