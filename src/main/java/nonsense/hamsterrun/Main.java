package nonsense.hamsterrun;

import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.sprites.SpritesProvider;

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
import java.util.stream.Collectors;

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
                        config.delayMs = Integer.valueOf(args[x]);
                        System.out.println("Main loop delay will be " + config.delayMs + " ms");
                        break;
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
                    case "rat":
                        x++;
                        config.addRat(args[x]);
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
        RatsController.RatControl control = null;
        //syntax control:skin:haveDisplay:aiModifier  eg k1:uhlicek:true  or pc:rat:false:10
        for (String ratDef : BaseConfig.getConfig().getRats()) {
            Rat rat = new Rat();
            String[] rataParams = ratDef.split(":");
            for (int i = 0; i < rataParams.length; i++) {
                String param = rataParams[i];
                switch (i) {
                    case 3:
                        control.setChaos(Integer.valueOf(param));
                        break;
                    case 2:
                        control.setDisplay(Boolean.valueOf(param));
                        if (!control.isDisplay()) {
                            //this is not enough. AI can sometimes overflow the sound buffers
                            //however it is unsure to me why and how
                            rat.disableSounds();
                        }
                        break;
                    case 1:
                        if (SpritesProvider.KNOWN_RATS.contains(param)) {
                            rat.setSkin(param);
                        } else {
                            throw new RuntimeException("Unknown sprite " + param +". Available are " + SpritesProvider.KNOWN_RATS.stream().collect(Collectors.joining(",")));
                        }
                        break;
                    case 0:
                        if (param.equalsIgnoreCase("pc")) {
                            control = new RatsController.ComputerControl();
                        } else if (param.equalsIgnoreCase("k1")) {
                            control = new RatsController.KeyboardControl1();
                        } else if (param.equalsIgnoreCase("k2")) {
                            control = new RatsController.KeyboardControl2();
                        } else if (param.equalsIgnoreCase("k3")) {
                            control = new RatsController.KeyboardControl3();
                        } else if (param.equalsIgnoreCase("m")) {
                            control = new RatsController.MouseControl();
                        } else {
                            throw new RuntimeException("unknown param in rat def: " + param + "/" + ratDef);
                        }
                        break;
                }
            }

            ratsController.addRat(new RatsController.RatWithControls(rat, control));
        }
        final Rat currentDemoRat = ratsController.getRats().get(0);
        world.setRatsProvider(ratsController);
        world.allRatsSpread(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel mapPanel = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, 16, currentDemoRat, true);
                    }
                };
                JPanel view = new JPanel() {
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2d = (Graphics2D) g;
                        world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), false, ratsController.getRatControl(currentDemoRat).getZoom(), currentDemoRat, false);
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
                        for (Rat rat : ratsController.getRats()) {
                            System.out.println(e.getKeyCode() + "");
                            RatsController.RatControl ratControl = ratsController.getRatControl(rat);
                            if (ratControl instanceof RatsController.KeyboardControl) {
                                ((RatsController.KeyboardControl) ratControl).act(rat, e, world);
                            }
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
