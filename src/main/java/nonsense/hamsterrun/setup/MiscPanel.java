package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


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
    private final JButton save;
    private final JButton load;
    JFileChooser chooser = new JFileChooser();
    private static File lastPath;


    public MiscPanel(World world, SetupWindow parent) {
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
        this.singleScore = (new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getIndividualMinimalScoreToEnterGoldenGate(), 0, 10000, 1)));
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
        save = new JButton("save config");
        load = new JButton("load config");
        this.add(save);
        this.add(load);
        try {
            Class.forName("com.google.gson.GsonBuilder");
        }catch (ClassNotFoundException ex){
            save.setEnabled(false);
            load.setEnabled(false);
            String s = "To enable  save/load put gson library to class-path";
            save.setToolTipText(s);
            load.setToolTipText(s);
        }
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (lastPath != null) {
                        chooser.setCurrentDirectory(lastPath.getParentFile());
                    }
                    int returnVal = chooser.showSaveDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        lastPath = chooser.getSelectedFile();
                        BaseConfig.save(chooser.getSelectedFile());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(save, ex);
                    ex.printStackTrace();
                }
            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (lastPath != null) {
                        chooser.setCurrentDirectory(lastPath.getParentFile());
                    }
                    int returnVal = chooser.showOpenDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        lastPath = chooser.getSelectedFile();
                        BaseConfig lastConfig = BaseConfig.getConfig();
                        BaseConfig.load(chooser.getSelectedFile());
                        BaseConfig.getConfig().setBaseSize(lastConfig.getBaseSize());
                        BaseConfig.getConfig().setGridSize(lastConfig.getGridSize());
                        SpritesProvider.recreateFloor();
                        new SetupWindow(world).setVisible(true);
                        parent.dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(load, ex);
                    ex.printStackTrace();
                }
            }
        });
        setTitles();
    }

    @Override
    public void setTitles() {
        mouseLabel.setText(Localization.get().get("mouseLabel"));
        tunnelLabel.setText(Localization.get().get("tunnelLabel"));
        keysLabel.setText(Localization.get().get("keysLabel"));
        cumulLabel.setText(Localization.get().get("cumuLabel"));
        singleLabel.setText(Localization.get().get("singleLabel"));
        save.setText(Localization.get().get("save"));
        load.setText(Localization.get().get("load"));
        setName(Localization.get().getMiscTitle());

    }


}
