package nonsense.hamsterrun.setup;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.Main;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class SetupWindow extends JFrame implements Localized {

    private final JButton startButton;
    private final World pausedWorld;

    public SetupWindow(World world) {
        JTabbedPane tabs = new JTabbedPane();
        if (world == null) {
            JPanel rats = new RatsPanel();
            tabs.add(rats);
        }
        JPanel items = new ItemsAndAliensPanel();
        tabs.add(items);
        tabs.add(new WorldPanel(world));
        JPanel allowedControls = new MiscPanel(world, this);
        tabs.add(allowedControls);
        JPanel presetConfigs = new PresetConfigs(world, this);
        tabs.add(presetConfigs);
        JPanel networkConfigs = new NetworkPane(world, this);
        tabs.add(networkConfigs);
        add(tabs);
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (tabs.getSelectedComponent() instanceof  NetworkPane) {
                    startButton.setEnabled(false);
                } else {
                    startButton.setEnabled(true);
                }
            }
        });
        if (world == null) {
            startButton = new JButton("start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    start();
                }
            });
            add(startButton, BorderLayout.SOUTH);
        } else {
            SetupWindow.this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    world.resume();
                }
            });
            startButton = new JButton("resume");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    world.resume();
                    SetupWindow.this.dispose();
                }
            });
            add(startButton, BorderLayout.SOUTH);
        }
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pausedWorld = world;
        setTitles();
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            BaseConfig config = BaseConfig.getConfig();
            SpritesProvider.load();
            config.summUp();
            config.verify();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SetupWindow(null);
                }
            });
        } else {
            Main.main(args);
        }
    }

    public void start() {
        this.dispose();
        new Main().worldDemo();
    }

    public void setTitles() {
        setTitle(Localization.get().getMainTitle());
        if (pausedWorld == null) {
            startButton.setText(Localization.get().getStartGame());
        } else {
            startButton.setText(Localization.get().getResumeGame());
        }
    }

}
