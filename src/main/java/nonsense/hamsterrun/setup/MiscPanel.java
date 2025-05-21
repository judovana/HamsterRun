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

import javax.swing.JButton;
import javax.swing.JCheckBox;
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


public class MiscPanel extends JPanel implements Localized {
    private final JSpinner mouseSpinner;
    private final JSpinner tunnelConfusion;


    //TODO
    //minKeysToEnterTheCage - multiplied by rats 0 disables it
    //minCumualtiveScoreToEnterCage - 0 disables it
    //allRatsEnterTime - 0 disables and one is enough

    public MiscPanel() {
        this.setLayout(new GridLayout(0, 2));
        this.add(new JLabel("mouse sensitivity"));
        this.mouseSpinner = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getMouseDelay(), 0, 1000, 1)));
        mouseSpinner.addChangeListener(a -> {
            BaseConfig.getConfig().setMouseSensitivity(((Number) mouseSpinner.getValue()).intValue());
        });
        this.add(mouseSpinner);
        this.add(new JLabel("tunnel confusion"));
        this.tunnelConfusion = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getTunnelConfusionFactor(), 0, 1000, 1)));
        tunnelConfusion.addChangeListener(a -> {
            BaseConfig.getConfig().setTunnelConfusion(((Number) tunnelConfusion.getValue()).intValue());
        });
        this.add(tunnelConfusion);
        this.add(new JLabel("----"));
        this.add(new JLabel("----"));
        //minKeysToEnterTheCage
        //minCumualtiveScoreToEnterCage
        //allRatsEnterTime
        this.add(new JLabel("----"));
        this.add(new JLabel("Allowed keys:"));
        this.add(new JCheckBox("left", true));
        this.add(new JCheckBox("right", true));
        this.add(new JCheckBox("up", true));
        this.add(new JCheckBox("down", true));
        this.add(new JCheckBox("zoom", true));
        this.add(new JCheckBox("map", true));
        this.add(new JCheckBox("call", true));
        this.add(new JCheckBox("spread", true));
        this.add(new JButton("save config"));
        this.add(new JButton("load config"));
        setTitles();
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getMiscTitle());

    }


}
