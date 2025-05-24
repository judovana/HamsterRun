package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.Utils;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.ratcontroll.RatsProvider;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class WorldPanel extends JPanel implements Localized, ChangeListener, FocusListener {

    private final JLabel baseSizeLabel;
    private final JSpinner baseSizeSpinner;
    private final JLabel gridSizeLabel;
    private final JSpinner gridSizeSpinner;
    private final JLabel regSpeedLabel;
    private final JSpinner regSpeedSpinner;
    private final JLabel baseDensityMinLabel;
    private final JSpinner baseDensityMinSpinner;
    private final JLabel baseDensityMaxLabel;
    private final JSpinner baseDensityMaxSpinner;
    private final JLabel gridConnectivityMinLabel;
    private final JSpinner gridConnectivityMinSpinner;
    private final JLabel gridConnectivityMaxLabel;
    private final JSpinner gridConnectivityMaxSpinner;
    private final JLabel delayMsLabel;
    private final JSpinner delayMsSpinner;
    private final JCheckBox keepRegenerating;
    //    private int columns =  - moved to rats
    private final JComboBox<String> previewType;
    private final JComboBox<String> floor;

    private World world;
    private BufferedImage staticWorld;
    private final JPanel preview = new JPanel() {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            if (previewType.getSelectedIndex() == 2) {
                if (world != null) {
                    int zoom = getZoom(this);
                    world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, zoom, null, true);
                }
            } else if (previewType.getSelectedIndex() == 1) {
                if (staticWorld != null) {
                    int wh = Math.min(this.getWidth(), this.getHeight());
                    g2d.drawImage(staticWorld, (this.getWidth() - wh) / 2, (this.getHeight() - wh) / 2, wh, wh, null);
                }
            } else {
                //will be cleaned?
            }
        }

    };
    private Object lastIntValue;


    //TODO extract shared min/max here and in config validate
    public WorldPanel(World world) {
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
        if (world != null) {
            baseSizeSpinner.setEnabled(false);
        }


        gridSizeLabel = new JLabel("grid size");
        controls.add(gridSizeLabel);
        gridSizeSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getGridSize(), 3, 10001, 2));
        gridSizeSpinner.addChangeListener(this);
        gridSizeSpinner.addFocusListener(this);
        controls.add(gridSizeSpinner);
        if (world != null) {
            gridSizeSpinner.setEnabled(false);
        }

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

        gridConnectivityMinLabel = new JLabel("grid connectivity min");
        controls.add(gridConnectivityMinLabel);
        gridConnectivityMinSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getGridConnectivityMin(), 1, 999, 1));
        gridConnectivityMinSpinner.addChangeListener(this);
        gridConnectivityMinSpinner.addFocusListener(this);
        controls.add(gridConnectivityMinSpinner);

        gridConnectivityMaxLabel = new JLabel("base connectivity max");
        controls.add(gridConnectivityMaxLabel);
        gridConnectivityMaxSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getGridConnectivityMax(), 1, 999, 1));
        gridConnectivityMaxSpinner.addChangeListener(this);
        gridConnectivityMaxSpinner.addFocusListener(this);
        controls.add(gridConnectivityMaxSpinner);


        delayMsLabel = new JLabel("delay ms");
        controls.add(delayMsLabel);
        delayMsSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getDelayMs(), 1, 100000, 10));
        delayMsSpinner.addChangeListener(this);
        delayMsSpinner.addFocusListener(this);
        controls.add(delayMsSpinner);

        regSpeedLabel = new JLabel("reg speed");
        controls.add(regSpeedLabel);
        regSpeedSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getRegSpeed(), 4, 10000, 10));
        regSpeedSpinner.addChangeListener(this);
        regSpeedSpinner.addFocusListener(this);
        controls.add(regSpeedSpinner);

        keepRegenerating = new JCheckBox("keepRegenerating", BaseConfig.getConfig().isKeepRegenerating());
        keepRegenerating.addChangeListener(this);
        controls.add(new JLabel(""));
        controls.add(keepRegenerating);

        ///controlsScroll.add(controls);
        add(controls);

        preview.setBackground(Color.BLACK);
        JPanel previewHolder = new JPanel(new BorderLayout());
        previewHolder.add(preview, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new GridLayout(2,1));
        previewType = new JComboBox();
        previewType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorldPanel.this.stateChanged(null);
            }
        });
        southPanel.add(previewType);
        floor = new JComboBox(new Vector(SpritesProvider.KNOWN_FLOORS));
        floor.setSelectedItem(BaseConfig.getConfig().getFloor());
        floor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseConfig.getConfig().setFloor((String)(floor.getSelectedItem()));
                try {
                    SpritesProvider.recreateFloor();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        southPanel.add(floor);
        previewHolder.add(southPanel, BorderLayout.SOUTH);
        add(previewHolder);
        setTitles();
        stateChanged(null);
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getWorldTitle());
        baseSizeLabel.setText(Localization.get().getBaseConfigLabel());
        gridSizeLabel.setText(Localization.get().getGridConfigLabel());
        regSpeedLabel.setText(Localization.get().getRegSpeedLabel());
        baseDensityMaxLabel.setText(Localization.get().baseDensityMaxLabel());
        baseDensityMinLabel.setText(Localization.get().baseDensityMinLabel());
        gridConnectivityMinLabel.setText(Localization.get().gridConnectivityMinLabel());
        gridConnectivityMaxLabel.setText(Localization.get().gridConnectivityMaxLabel());
        delayMsLabel.setText(Localization.get().delayMs());
        keepRegenerating.setText(Localization.get().getKeepRegenerating());
        int index = previewType.getSelectedIndex();
        if (index < 0) {
            index = 0;
        }
        previewType.setModel(getLocalisedPreviewTypes());
        previewType.setSelectedIndex(index);
    }

    private ComboBoxModel getLocalisedPreviewTypes() {
        return new DefaultComboBoxModel(new String[]{
                Localization.get().getNoPreview(),
                Localization.get().getImagePreview(),
                Localization.get().getFullPreview()
        });
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        BaseConfig.getConfig().setBaseSize(((Number) baseSizeSpinner.getValue()).intValue());
        BaseConfig.getConfig().setGridSize(((Number) gridSizeSpinner.getValue()).intValue());
        BaseConfig.getConfig().setBaseDensityMin(((Number) baseDensityMinSpinner.getValue()).intValue());
        BaseConfig.getConfig().setBaseDensityMax(((Number) baseDensityMaxSpinner.getValue()).intValue());
        BaseConfig.getConfig().setGridConnectivityMin(((Number) gridConnectivityMinSpinner.getValue()).intValue());
        BaseConfig.getConfig().setGridConnectivityMax(((Number) gridConnectivityMaxSpinner.getValue()).intValue());
        BaseConfig.getConfig().setRegSpeed(((Number) regSpeedSpinner.getValue()).intValue());
        BaseConfig.getConfig().setDelayMs(((Number) delayMsSpinner.getValue()).intValue());
        BaseConfig.getConfig().setKeepRegenerating(keepRegenerating.isSelected());
        try {
            BaseConfig.getConfig().verify();
        } catch (Exception ex) {
            if (changeEvent != null && changeEvent.getSource() instanceof JSpinner) {
                ((JSpinner) changeEvent.getSource()).setValue(lastIntValue);
            }
            JOptionPane.showMessageDialog(null, ex);
            throw ex;
        }
        if (changeEvent != null && changeEvent.getSource() instanceof JSpinner) {
            lastIntValue = ((JSpinner) changeEvent.getSource()).getValue();
        }
        if (world != null) {
            world.kill();
        }
        if (previewType.getSelectedIndex() == 2) {
            world = createFullnewWorld();
            staticWorld = null;
        } else if (previewType.getSelectedIndex() == 1) {
            world = null;
            Maze maze = Maze.generate(BaseConfig.getConfig());
            staticWorld = Utils.toImage(maze, getZoom(preview), BaseConfig.getConfig(), true);
        } else {
            world = null;
            staticWorld = null;
        }
        preview.repaint();
    }

    private static int getZoom(JPanel that) {
        int worldWidth = BaseConfig.getConfig().getBaseSize() * BaseConfig.getConfig().getGridSize();
        int worldHeight = BaseConfig.getConfig().getBaseSize() * BaseConfig.getConfig().getGridSize();
        int wZoopm = that.getWidth() / worldWidth;
        int hZoopm = that.getHeight() / worldHeight;
        int zoom = Math.min(hZoopm, wZoopm);
        return zoom;
    }

    private World createFullnewWorld() {
        World lworld = new World(Maze.generate(BaseConfig.getConfig()));
        lworld.setRatsProvider(new RatsProvider() {
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

            @Override
            public void kill() {
                for (Rat rat : this.getRats()) {
                    rat.getSounds().kill();
                }
            }

            @Override
            public void remove(Rat rat) {
                //should be no ope here
            }
        });
        lworld.allRatsSpread(false);
        lworld.addRepaintListener(preview);
        return lworld;
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
