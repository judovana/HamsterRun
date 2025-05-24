package nonsense.hamsterrun;

import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.KeyboardControl;
import nonsense.hamsterrun.ratcontroll.KeyboardControl0;
import nonsense.hamsterrun.ratcontroll.MouseControl;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.ratcontroll.ScoreListener;
import nonsense.hamsterrun.setup.SetupWindow;
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
import java.io.File;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws Exception {
        for (int x = 0; x < args.length; x++) {
            if (args[x].startsWith("-")) {
                String sanitized = args[x].replaceAll("^-+", "").toLowerCase();
                switch (sanitized) {
                    case "delay":
                        x++;
                        BaseConfig.getConfig().setDelayMs(Integer.valueOf(args[x]));
                        System.out.println("Main loop delay will be " + BaseConfig.getConfig().getDelayMs() + " ms");
                        break;
                    case "base-size":
                        x++;
                        BaseConfig.getConfig().setBaseSize(Integer.valueOf(args[x]));
                        System.out.println("Each basic block will have size " + BaseConfig.getConfig().getBaseSize() + " x " + BaseConfig.getConfig().getBaseSize());
                        break;
                    case "base-density-min":
                        x++;
                        BaseConfig.getConfig().setBaseDensityMin(Integer.valueOf(args[x]));
                        break;
                    case "base-density-max":
                        x++;
                        BaseConfig.getConfig().setBaseDensityMax(Integer.valueOf(args[x]));
                        break;
                    case "grid-size":
                        x++;
                        BaseConfig.getConfig().setGridSize(Integer.valueOf(args[x]));
                        break;
                    case "grid-connectivity-min":
                        x++;
                        BaseConfig.getConfig().setGridConnectivityMin(Integer.valueOf(args[x]));
                        break;
                    case "grid-connectivity-max":
                        x++;
                        BaseConfig.getConfig().setGridConnectivityMax(Integer.valueOf(args[x]));
                        break;
                    case "keep-regenerating":
                        x++;
                        BaseConfig.getConfig().setKeepRegenerating(Boolean.valueOf(args[x]));
                        break;
                    case "regenerating-speed":
                        x++;
                        BaseConfig.getConfig().setRegSpeed(Integer.valueOf(args[x]));
                        break;
                    case "rat":
                        x++;
                        BaseConfig.getConfig().addRat(args[x]);
                        break;
                    case "columns":
                        x++;
                        BaseConfig.getConfig().setColumns(Integer.valueOf(args[x]));
                        break;
                    case "item":
                    case "trap":
                        x++;
                        BaseConfig.getConfig().addTrapModifier(args[x], false);
                        break;
                    case "alien":
                        x++;
                        BaseConfig.getConfig().addTrapModifier(args[x], true);
                        break;
                    case "max-aliens":
                        x++;
                        BaseConfig.getConfig().setMaxAliens(Integer.valueOf(args[x]));
                        break;
                    case "min-keys":
                        x++;
                        BaseConfig.getConfig().setCumulativeMinimalNUmberOfKeys(Integer.valueOf(args[x]));
                        break;
                    case "min-score":
                        x++;
                        BaseConfig.getConfig().setCumulativeMinimalScoreToEnterGoldenGate(Integer.valueOf(args[x]));
                        break;
                    case "min-score-individual":
                        x++;
                        BaseConfig.getConfig().setIndividualMinimalScoreToEnterGoldenGate(Integer.valueOf(args[x]));
                        break;
                    case "tunnel":
                        x++;
                        BaseConfig.getConfig().setTunnelConfusion(Integer.valueOf(args[x]));
                        break;
                    case "mouse-sensitivity":
                        x++;
                        BaseConfig.getConfig().setMouseSensitivity(Integer.valueOf(args[x]));
                        break;
                    case "save":
                        x++;
                        BaseConfig.save(new File(args[x]));
                        break;
                    case "load":
                        x++;
                        BaseConfig.load(new File(args[x]));
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
                gameView.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                            world.pause();
                            new SetupWindow(world).setVisible(true);
                        }
                    }
                });
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
