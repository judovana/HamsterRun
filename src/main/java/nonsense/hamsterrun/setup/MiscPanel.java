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



public class MiscPanel extends JPanel implements Localized {
    private final JSpinner mouseSpinner;
    private final JLabel mouseLabel;
    private final JSpinner tunnelConfusion;
    private final JLabel tunnelLabel;
    private final JSpinner keys;
    private final JLabel keysLabel;
    private final JSpinner cumulScore;
    private final JLabel cumulLabel;
    private final JSpinner singleScore;
    private final JLabel singleLabel;


    //TODO
    //minKeysToEnterTheCage - multiplied by rats 0 disables it
    //minCumualtiveScoreToEnterCage - 0 disables it
    //allRatsEnterTime - 0 disables and one is enough

    public MiscPanel() {
        this.setLayout(new GridLayout(0, 2));

        keysLabel = new JLabel("keys to enter cage");
        this.add(keysLabel);
        this.keys = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getCumulativeMinimalNUmberOfKeys(), 0, 100, 1)));
        keys.addChangeListener(a -> {
            BaseConfig.getConfig().setCumulativeMinimalNUmberOfKeys(((Number) keys.getValue()).intValue());
        });
        this.add(keys);

        cumulLabel = new JLabel("cumulative score to enter cage");
        this.add(cumulLabel);
        this.cumulScore = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getCumulativeMinimalScoreToEnterGoldenGate(), 0, 100000, 1)));
        cumulScore.addChangeListener(a -> {
            BaseConfig.getConfig().setCumulativeMinimalScoreToEnterGoldenGate(((Number) cumulScore.getValue()).intValue());
        });
        this.add(cumulScore);

        singleLabel = new JLabel("minimal score per rat to enter the gate");
        this.add(singleLabel);
        this.singleScore= (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getIndividualMinimalScoreToEnterGoldenGate(), 0, 10000, 1)));
        singleScore.addChangeListener(a -> {
            BaseConfig.getConfig().setIndividualMinimalScoreToEnterGoldenGate(((Number) singleScore.getValue()).intValue());
        });
        this.add(singleScore);

        tunnelLabel = new JLabel("tunnel confusion");
        this.add(tunnelLabel);
        this.tunnelConfusion = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getTunnelConfusionFactor(), 0, 1000, 1)));
        tunnelConfusion.addChangeListener(a -> {
            BaseConfig.getConfig().setTunnelConfusion(((Number) tunnelConfusion.getValue()).intValue());
        });
        this.add(tunnelConfusion);

        mouseLabel = new JLabel("mouse sensitivity");
        this.add(mouseLabel);
        this.mouseSpinner = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getMouseDelay(), 0, 1000, 1)));
        mouseSpinner.addChangeListener(a -> {
            BaseConfig.getConfig().setMouseSensitivity(((Number) mouseSpinner.getValue()).intValue());
        });
        this.add(mouseSpinner);

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
