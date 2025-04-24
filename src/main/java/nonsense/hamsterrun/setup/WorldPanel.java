package nonsense.hamsterrun.setup;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.Maze;
import nonsense.hamsterrun.env.World;


public class WorldPanel extends JPanel implements Localized, ChangeListener {

    private World world;
    private final JLabel baseSizeLabel;
    private final JSpinner baseSizeSpinner;
    private final JPanel preview = new JPanel() {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            if (world!=null) {
                world.drawMap(g2d, new Point(this.getWidth() / 2, this.getHeight() / 2), true, 2, null, true);
            }
        }
    };


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
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        BaseConfig.getConfig().setBaseSize(((Number) baseSizeSpinner.getValue()).intValue());
        //FIXME the old world must terminate te thread.. or something..its blocking the shutdwon now
        world = new World(Maze.generate(BaseConfig.getConfig()));
        world.addRepaintListener(preview);
    }
}
