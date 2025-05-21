package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.traps.Item;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ItemsAndAliensPanel extends JPanel implements Localized {

    private static final SoundsBuffer examples = new SoundsBuffer();
    private JPanel itemsPanel;
    private JButton resetFields;
    private JButton disableFields;


    //TODO extract shared min/max here and in config validate
    public ItemsAndAliensPanel() {
        this.setLayout(new GridLayout(2, 1));
        JPanel itemsWrapper = new JPanel(new BorderLayout());
        itemsPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane controlsScroll = new JScrollPane(itemsPanel);
        itemsWrapper.add(controlsScroll);
        JPanel itemsControls = new JPanel(new GridLayout(1,2));
        resetFields = new JButton("reset");
        resetFields.addActionListener( a -> resetAllitems());
        disableFields = new JButton("disable all");
        disableFields.addActionListener( a -> disbaleAllitems());
        itemsControls.add(resetFields);
        itemsControls.add(disableFields);
        itemsWrapper.add(itemsControls, BorderLayout.SOUTH);
        add(itemsWrapper);
//        add(aliens);
        setTitles();
    }

    private static String getPercentText(int ratio, int summ) {
        float probab = ((float) ratio / (float) summ * (float) 100);
        return ratio + "/" + summ + "->" + probab + "%";
    }

    private void regenerateItems(boolean recreate, boolean setSpinners) {
        if (recreate) {
            itemsPanel.removeAll();
        }
        int sum = 0;
        int origSum = 0;
        for (BaseConfig.ItemsWithProbability item : BaseConfig.getConfig().getItemsProbabilities()) {
            int origRatio = BaseConfig.getConfig().getDefaultItemProbability(item.clazz);
            origSum += origRatio;
            sum += item.ratio;
        }
        int counter = 0;
        for (BaseConfig.ItemsWithProbability iwp : BaseConfig.getConfig().getItemsProbabilities()) {
            if (recreate) {
                PreviewItemLine item = new PreviewItemLine(iwp, origSum, sum);
                itemsPanel.add(item);
            } else {
                ((PreviewItemLine) (itemsPanel.getComponent(counter))).refreshCountes(iwp, origSum, sum);
                if (setSpinners) {
                    ((PreviewItemLine) (itemsPanel.getComponent(counter))).setSpinner();
                }
            }
            counter++;
        }
    }

    private void resetAllitems() {
       BaseConfig.getConfig().resetItemsProbabilities();
       regenerateItems(true, false);
       itemsPanel.revalidate();
       this.revalidate();
    }

    private void disbaleAllitems() {
        BaseConfig.getConfig().disbaleAllItems();
        regenerateItems(true, false);
        itemsPanel.revalidate();
        this.revalidate();
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getItemsTitle());
        disableFields.setText(Localization.get().getDisableAll());
        resetFields.setText(Localization.get().getResetFields());
        regenerateItems(true, false);
    }

    private static class PreviewAlienLine extends JPanel {
        private final MovingOne alien;

        public PreviewAlienLine(MovingOne alien) {
            this.alien = alien;
        }
    }

    private class PreviewItemLine extends JPanel {
        private final Item item;
        private final BaseConfig.ItemsWithProbability source;
        private final JLabel is;
        private final JSpinner js;

        public PreviewItemLine(BaseConfig.ItemsWithProbability iwp, int origSum, int sum) {
            this.setLayout(new GridLayout(1, 3));
            this.item = BlockField.itemClassToItemCatched(iwp.clazz);
            this.source = iwp;
            JPanel b1 = new JPanel(new GridLayout(2, 1));
            JLabel ln = new JLabel(source.clazz.getSimpleName());
            ln.setToolTipText("<html>" + Localization.get().getOr(source.clazz.getName()));
            ln.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(ln, ln.getToolTipText());
                }
            });
            b1.add(ln);
            JButton sound = new JButton("<))");
            sound.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    item.playMainSoundFor(examples);
                    item.playSecondarySoundFor(examples);
                    item.playTercialSoundFor(examples);
                }
            });
            b1.add(sound);
            this.add(b1);
            this.add(new ThumbanilPanel());
            JPanel b2 = new JPanel(new GridLayout(2, 1));
            js = new JSpinner(new SpinnerNumberModel(iwp.ratio, 0, 10000, 1));
            js.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    BaseConfig.getConfig().addTrapModifierSafe(iwp.clazz, ((Number) js.getValue()).intValue());
                    regenerateItems(false, false);
                }
            });
            js.setToolTipText(Localization.get().getDisabledOnZero());
            b2.add(js);
            is = new JLabel();
            refreshCountes(iwp, origSum, sum);
            b2.add(is);
            this.add(b2);

        }

        public void refreshCountes(BaseConfig.ItemsWithProbability iwp, int origSum, int sum) {
            is.setText("is: " + getPercentText(iwp.ratio, sum));
            is.setToolTipText("was: " + getPercentText(BaseConfig.getConfig().getDefaultItemProbability(iwp.clazz), origSum));
        }

        public void setSpinner() {
            js.setValue(source.ratio);
        }

        private class ThumbanilPanel extends JPanel {

            public ThumbanilPanel() {
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
                Graphics2D g2d = (Graphics2D) g;
                item.drawThumbnail(g2d, this.getHeight());
            }
        }
    }
}
