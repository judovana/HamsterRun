package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.VirtualRatSetup;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RatsPanel extends JPanel implements Localized {

    private final List<VirtualRatSetup> ratsWithView = new ArrayList<>();
    private final List<VirtualRatSetup> ratsWithoutView = new ArrayList<>();

    private final JButton addButton1;
    private final JButton addButton2;
    private final JSpinner columnsSpinner;
    private final JLabel columnsSpinnerLabel;
    JPanel mousesWithView;
    JPanel mousesWithoutView;

    public RatsPanel() {
        this.setLayout(new BorderLayout());
        addButton1 = new JButton("Add rat with view");
        addButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ratsWithView.add(new VirtualRatSetup("none", RatsController.DEFAULT_CHAOS, true, getRandomSkin(), "k1"));
                repopulateWith();
            }
        });
        addButton2 = new JButton("Add rat without view");
        addButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ratsWithoutView.add(new VirtualRatSetup("none", RatsController.DEFAULT_CHAOS, false, getRandomSkin(), "pc"));
                repopulateWithout();
            }
        });
        JPanel upButtns = new JPanel(new GridLayout(1, 2));
        upButtns.add(addButton1);
        upButtns.add(addButton2);
        this.add(upButtns, BorderLayout.NORTH);
        //  private int columns = 2; //number of collumns on split screen
        JPanel bottomSpinner = new JPanel(new BorderLayout());
        columnsSpinnerLabel = new JLabel("set columns");
        columnsSpinner = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getColumnsDirect(), 1, 10, 1));
        columnsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                BaseConfig.getConfig().setColumns((int) (columnsSpinner.getValue()));
                mousesWithView.setLayout(new GridLayout(0, BaseConfig.getConfig().getColumnsDirect()));
                RatsPanel.super.revalidate();
                RatsPanel.super.repaint();
            }
        });
        bottomSpinner.add(columnsSpinnerLabel);
        bottomSpinner.add(columnsSpinner, BorderLayout.EAST);
        this.add(bottomSpinner, BorderLayout.SOUTH);
        JPanel central = new JPanel(new GridLayout(2, 1));
        this.add(central);
        mousesWithView = new JPanel(new GridLayout(0, BaseConfig.getConfig().getColumnsDirect()));
        mousesWithoutView = new JPanel(new GridLayout(1, 0));
        central.add(mousesWithView);
        central.add(mousesWithoutView);
        this.add(central);
        setTitles();
    }

    private void repopulateWith(){
        populateWith(mousesWithView, ratsWithView);
    }
    private void repopulateWithout(){
        populateWith(mousesWithoutView, ratsWithoutView);
    }

    private void populateWith(JPanel view, List<VirtualRatSetup> orig) {
        setMouses();
        view.removeAll();
        for (VirtualRatSetup rat : orig) {
            view.add(new RatConfig(rat));
        }
        view.revalidate();
        view.repaint();
    }

    private void setMouses() {
        BaseConfig.getConfig().getRats().clear();
        for(VirtualRatSetup rat: ratsWithView){
            BaseConfig.getConfig().addRat(rat.toString());
        }
        for(VirtualRatSetup rat: ratsWithoutView){
            BaseConfig.getConfig().addRat(rat.toString());
        }
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getRatsTitle());
        addButton1.setText(Localization.get().getAddMousesButton1());
        addButton2.setText(Localization.get().getAddMousesButton2());
        columnsSpinnerLabel.setText(Localization.get().getColumnsLabel());
    }

    private class RatConfig extends JPanel {
        private final JComboBox<String> skin;
        private final JComboBox<String> controls;
        private final JCheckBox view;
        private final JSpinner chaos;
        private final JButton remove;
        private final VirtualRatSetup rat;

        public RatConfig(VirtualRatSetup rat) {
            this.rat = rat;
            this.setLayout(new GridLayout(3, 3));
            this.skin=(new JComboBox<String>(SpritesProvider.KNOWN_RATS.toArray(new String[0])));
            this.skin.setSelectedItem(rat.getSkin());
            this.add(skin);
            if (rat.isDisplay()) {
                this.controls=(new JComboBox<String>(new String[]{"k1", "k2", "k3", "m1", "pc"}));
            } else {
                this.controls=(new JComboBox<String>(new String[]{"pc"}));
            }
            this.controls.setToolTipText(VirtualRatSetup.stringToRatControl("none", rat.getControlDef()).toString());
            this.add(controls);
            this.view = new JCheckBox("view", rat.isDisplay());
            this.view.setEnabled(false);
            this.add(this.view);
            this.add(new JLabel("ai chaos"));
            this.chaos=new JSpinner(new SpinnerNumberModel(rat.getAiChaos(), 1, 10000, 1));
            this.add(this.chaos);
            //set skin to thumbnail
            this.remove=new JButton(Localization.get().getRemoveMe());
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rat.isDisplay()) {
                        ratsWithView.remove(rat);
                        repopulateWith();
                    }else {
                        ratsWithoutView.remove(rat);
                        repopulateWithout();
                    }
                }
            });
            this.add(this.remove);
            this.add(new JLabel(""));
            this.add(new ThumbanilPanel(rat.getSkin()));
//            Border raisedbevel = BorderFactory.createRaisedBevelBorder();
//            Border loweredbevel = BorderFactory.createLoweredBevelBorder();
//            Border compound = BorderFactory.createCompoundBorder(
//                    raisedbevel, loweredbevel);
//            this.setBorder(compound);
            this.setBorder(new LineBorder(Color.black, 10));
        }
    }

    private static class ThumbanilPanel extends JPanel {
        int i = 0;
        String skin;

        public ThumbanilPanel(String skin) {
            this.skin = skin;
            this.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    repaint();
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            i++;
            if (i > 100) {
                i = 0;
            }
            Graphics2D g2d = (Graphics2D) g;
            BufferedImage bi = SpritesProvider.ratSprites.get(skin).getRun(RatActions.Direction.UP.getSprite(), i % (SpritesProvider.ratSprites.get(skin).getRuns()));
            g2d.drawImage(bi, 0, 0 , getWidth(), getHeight(), null);
        }
    }

    private static String getRandomSkin() {
        return SpritesProvider.KNOWN_RATS.get(new Random().nextInt(SpritesProvider.KNOWN_RATS.size()));
    }

}