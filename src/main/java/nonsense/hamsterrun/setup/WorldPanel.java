package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.World;

import javax.swing.JLabel;
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


public class WorldPanel extends JPanel implements Localized, ChangeListener {

    private World world;
    private final JLabel baseSizeLabel;
    private final JSpinner baseSizeSpinner;
    private final JLabel gridSizeLabel;
    private final JSpinner gridSizeSpinner;
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
        controls.add(baseSizeSpinner);

        gridSizeLabel = new JLabel("grid size");
        controls.add(gridSizeLabel);
        gridSizeSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getGridSize(), 3, 10000, 1));
        gridSizeSpinner.addChangeListener(this);
        controls.add(gridSizeSpinner);

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
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        BaseConfig.getConfig().setBaseSize(((Number) baseSizeSpinner.getValue()).intValue());
        BaseConfig.getConfig().setGridSize(((Number) gridSizeSpinner.getValue()).intValue());
        if (world != null) {
            world.kill();
        }
        world = new World(Maze.generate(BaseConfig.getConfig()));
        world.addRepaintListener(preview);
    }
}
