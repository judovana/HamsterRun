package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.ratcontroll.RatsProvider;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;


public class WorldPanel extends JPanel implements Localized, ChangeListener, FocusListener {

    private World world;
    private Object lastIntValue;
    private final JLabel baseSizeLabel;
    private final JSpinner baseSizeSpinner;
    private final JLabel gridSizeLabel;
    private final JSpinner gridSizeSpinner;
    private final JLabel regSpeedLabel;
    private final JSpinner regSpeedSpinner;
    private final JLabel baseDensityMaxLabel;
    private final JSpinner baseDensityMaxSpinner;
    private final JLabel baseDensityMinLabel;
    private final JSpinner baseDensityMinSpinner;
    private final JPanel preview = new JPanel() {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            if (world != null) {
                int worldWidth = BaseConfig.getConfig().getBaseSize() * BaseConfig.getConfig().getGridSize();
                int worldHeight = BaseConfig.getConfig().getBaseSize() * BaseConfig.getConfig().getGridSize();
                int wZoopm = this.getWidth() / worldWidth;
                int hZoopm = this.getHeight() / worldHeight;
                world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, Math.min(hZoopm, wZoopm), null, true);
            }
        }
    };


    //TODO extract shared min/max here and in config validate
    public WorldPanel() {
        this.setLayout(new GridLayout(2, 1));
        //JScrollPane controlsScroll = new JScrollPane();
        //add(controlsScroll);
        JPanel controls = new JPanel(new GridLayout(0, 2));
        baseSizeLabel = new JLabel("base size");
        controls.add(baseSizeLabel);
        baseSizeSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getBaseSize(), 4, 10000, 1));
        baseSizeSpinner.addChangeListener(this);
        baseSizeSpinner.addFocusListener(this);
        controls.add(baseSizeSpinner);


        gridSizeLabel = new JLabel("grid size");
        controls.add(gridSizeLabel);
        gridSizeSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getGridSize(), 3, 10001, 2));
        gridSizeSpinner.addChangeListener(this);
        gridSizeSpinner.addFocusListener(this);
        controls.add(gridSizeSpinner);

        baseDensityMinLabel = new JLabel("base Density Min");
        controls.add(baseDensityMinLabel);
        baseDensityMinSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getBaseDensityMin(), 1, 999, 1));
        baseDensityMinSpinner.addChangeListener(this);
        baseDensityMinSpinner.addFocusListener(this);
        controls.add(baseDensityMinSpinner);

        baseDensityMaxLabel = new JLabel("base Density Max");
        controls.add(baseDensityMaxLabel);
        baseDensityMaxSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getBaseDensityMax(), 1, 999, 1));
        baseDensityMaxSpinner.addChangeListener(this);
        baseDensityMaxSpinner.addFocusListener(this);
        controls.add(baseDensityMaxSpinner);

        regSpeedLabel = new JLabel("reg speed");
        controls.add(regSpeedLabel);
        regSpeedSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getRegSpeed(), 4, 10000, 1));
        regSpeedSpinner.addChangeListener(this);
        regSpeedSpinner.addFocusListener(this);
        controls.add(regSpeedSpinner);

        ///controlsScroll.add(controls);
        add(controls);

        preview.setBackground(Color.BLACK);
        add(preview);
        setTitles();
        stateChanged(null);
        ;
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getWorldTitle());
        baseSizeLabel.setText(Localization.get().getBaseConfigLabel());
        gridSizeLabel.setText(Localization.get().getGridConfigLabel());
        regSpeedLabel.setText(Localization.get().getRegSpeedLabel());
        baseDensityMaxLabel.setText(Localization.get().baseDensityMaxLabel());
        baseDensityMinLabel.setText(Localization.get().baseDensityMinLabel());
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        BaseConfig.getConfig().setBaseSize(((Number) baseSizeSpinner.getValue()).intValue());
        BaseConfig.getConfig().setGridSize(((Number) gridSizeSpinner.getValue()).intValue());
        BaseConfig.getConfig().setRegSpeed(((Number) regSpeedSpinner.getValue()).intValue());
        BaseConfig.getConfig().setBaseDensityMin(((Number) baseDensityMinSpinner.getValue()).intValue());
        BaseConfig.getConfig().setBaseDensityMax(((Number) baseDensityMaxSpinner.getValue()).intValue());
        try {
            BaseConfig.getConfig().verify();
        } catch (Exception ex) {
            if (changeEvent != null) {
                ((JSpinner) changeEvent.getSource()).setValue(lastIntValue);
            }
            JOptionPane.showMessageDialog(null, ex);
            throw ex;
        }
        if (changeEvent != null) {
            lastIntValue = ((JSpinner) changeEvent.getSource()).getValue();
        }
        if (world != null) {
            world.kill();
        }
        world = new World(Maze.generate(BaseConfig.getConfig()));
        world.setRatsProvider(new RatsProvider() {
            private final Rat r = new Rat(new SoundsBuffer.NoSound());

            @Override
            public List<Rat> getRats() {
                return Arrays.asList(r);
            }

            @Override
            public RatsController.RatControl getRatControl(Rat rat) {
                return new ComputerControl();
            }

            @Override
            public void swap(Rat rat) {

            }
        });
        world.allRatsSpread(false);
        world.addRepaintListener(preview);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JSpinner) {
            lastIntValue = ((JSpinner) e.getSource()).getValue();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {

    }
}
