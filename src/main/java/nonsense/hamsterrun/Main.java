package nonsense.hamsterrun;

import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.KeyboardControl;
import nonsense.hamsterrun.ratcontroll.KeyboardControl0;
import nonsense.hamsterrun.ratcontroll.MouseControl;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.ratcontroll.ScoreListener;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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
                    case "delay":
                        x++;
                        config.setDelayMs(Integer.valueOf(args[x]));
                        System.out.println("Main loop delay will be " + config.getDelayMs() + " ms");
                        break;
                    case "base-size":
                        x++;
                        config.setBaseSize(Integer.valueOf(args[x]));
                        System.out.println("Each basic block will have size " + config.getBaseSize() + " x " + config.getBaseSize());
                        break;
                    case "base-density-min":
                        x++;
                        config.setBaseDensityMin(Integer.valueOf(args[x]));
                        break;
                    case "base-density-max":
                        x++;
                        config.setBaseDensityMax(Integer.valueOf(args[x]));
                        break;
                    case "grid-size":
                        x++;
                        config.setGridSize(Integer.valueOf(args[x]));
                        break;
                    case "grid-connectivity-min":
                        x++;
                        config.setGridConnectivityMin(Integer.valueOf(args[x]));
                        break;
                    case "grid-connectivity-max":
                        x++;
                        config.setGridConnectivityMax(Integer.valueOf(args[x]));
                        break;
                    case "keep-regenerating":
                        x++;
                        config.setKeepRegenerating(Boolean.valueOf(args[x]));
                        break;
                    case "regenerating-speed":
                        x++;
                        config.setRegSpeed(Integer.valueOf(args[x]));
                        break;
                    case "rat":
                        x++;
                        config.addRat(args[x]);
                        break;
                    case "columns":
                        x++;
                        config.setColumns(Integer.valueOf(args[x]));
                        break;
                    case "item":
                    case "trap":
                        x++;
                        config.addTrapModifier(args[x], false);
                        break;
                    case "alien":
                        x++;
                        config.addTrapModifier(args[x], true);
                        break;
                    case "max-aliens":
                        x++;
                        config.setMaxAliens(Integer.valueOf(args[x]));
                        break;
                    case "tunnel":
                        x++;
                        config.setTunnelConfusion(Integer.valueOf(args[x]));
                        break;
                    case "mouse-sensitivity":
                        x++;
                        config.setMouseSensitivity(Integer.valueOf(args[x]));
                        break;
                    default:
                        throw new RuntimeException("Unknown parameter " + args[x]);

                }
            }
        }
        SpritesProvider.load();
        worldDemo();
    }

    private static WorldAndRats generateGame() {
        final World world = new World(Maze.generate(BaseConfig.getConfig()));
        final RatsController ratsController = new RatsController();
        RatsController.RatControl control = null;

        for (RatSetup param : BaseConfig.getConfig().getRats()) {
            Rat rat = new Rat();
            control = param.control;
            control.setChaos(param.aiChaos);
            control.setDisplay(param.display);
            if (!control.isDisplay()) {
                //this is not enough. AI can sometimes overflow the sound buffers
                //however it is unsure to me why and how
                rat.disableSounds();
            }
            rat.setSkin(param.skin);
            ratsController.addRat(new RatsController.RatWithControls(rat, control));
        }
        world.setRatsProvider(ratsController);
        world.allRatsSpread(true);
        return new WorldAndRats(world, ratsController);
    }

    public static void worldDemo() {
        BaseConfig.getConfig().summUp();
        BaseConfig.getConfig().verify();
        WorldAndRats result = generateGame();
        final World world = result.world;
        final RatsController ratsController = result.ratsController;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame gameView = new JFrame(Localization.get().getMainTitle());
                gameView.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        BaseConfig.getConfig().setWholeViewPort(gameView.getWidth(), gameView.getHeight());
                    }
                });
                gameView.setLayout(new GridLayout(0, BaseConfig.getConfig().getColumns(), 2, 2));
                if (BaseConfig.getConfig().getViews() == 0) {
                    RatsController.RatControl exControl = new KeyboardControl0();
                    ratsController.addRat(new RatsController.RatWithControls(null, exControl));
                    JPanel view = new JPanel() {
                        public void paint(Graphics g) {
                            super.paint(g);
                            Graphics2D g2d = (Graphics2D) g;
                            if (exControl.getMap() == 0) {
                                world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), false, exControl.getZoom(), null, false);
                            } else {
                                world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, exControl.getZoom(), null, true);
                            }
                        }
                    };
                    view.setBackground(Color.BLACK);
                    world.addRepaintListener(view);
                    gameView.add(view);
                }
                for (Rat rat : ratsController.getRats()) {
                    if (!ratsController.getRatControl(rat).isDisplay()) {
                        continue;
                    }
                    JPanel view = new JPanel() {
                        public void paint(Graphics g) {
                            super.paint(g);
                            Graphics2D g2d = (Graphics2D) g;
                            if (ratsController.getRatControl(rat) != null) {
                                if (ratsController.getRatControl(rat).getMap() == 0) {
                                    world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), false,
                                            ratsController.getRatControl(rat).getZoom(), rat, false);
                                } else {
                                    world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true,
                                            ratsController.getRatControl(rat).getZoom(), rat, true);
                                }
                            }
                        }
                    };
                    view.setBackground(Color.BLACK);
                    world.addRepaintListener(view);
                    JPanel wrapper = new JPanel(new BorderLayout());
                    JLabel scoreShow = new JLabel(rat.getSkin() + ": " + rat.getScore());
                    rat.addScoreListener(new ScoreListener(scoreShow, rat));
                    wrapper.add(scoreShow, BorderLayout.NORTH);
                    wrapper.add(view);
                    gameView.add(wrapper);
                }
                gameView.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        for (Rat rat : ratsController.getRats()) {
                            RatsController.RatControl ratControl = ratsController.getRatControl(rat);
                            if (ratControl instanceof MouseControl) {
                                ((MouseControl) ratControl).actC(rat, e, world);
                            }
                        }
                        gameView.repaint();
                    }
                });
                gameView.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        for (Rat rat : ratsController.getRats()) {
                            RatsController.RatControl ratControl = ratsController.getRatControl(rat);
                            if (ratControl instanceof MouseControl) {
                                ((MouseControl) ratControl).actM(rat, e, world);
                            }
                        }
                        gameView.repaint();
                    }
                });
                gameView.addMouseWheelListener(new MouseWheelListener() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        for (Rat rat : ratsController.getRats()) {
                            RatsController.RatControl ratControl = ratsController.getRatControl(rat);
                            if (ratControl instanceof MouseControl) {
                                ((MouseControl) ratControl).actW(rat, e, world);
                            }
                        }
                        gameView.repaint();
                    }
                });

                gameView.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        boolean found = false;
                        for (Rat rat : ratsController.getRats()) {
                            System.out.println(e.getKeyCode() + "");
                            RatsController.RatControl ratControl = ratsController.getRatControl(rat);
                            if (ratControl instanceof KeyboardControl) {
                                found = true;
                                ((KeyboardControl) ratControl).act(rat, e, world);
                            }
                        }
                        if (!found) {
                            if (ratsController.getNulLControl() != null) {
                                ratsController.getNulLControl().act(null, e, world);
                            }
                        }
                        gameView.repaint();
                    }
                });
                gameView.setSize(800, 800);
                //gameView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                gameView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                gameView.setVisible(true);
            }
        });
    }

    private static class WorldAndRats {
        final World world;
        final RatsController ratsController;

        public WorldAndRats(World world, RatsController ratsController) {
            this.world = world;
            this.ratsController = ratsController;
        }
    }

}
