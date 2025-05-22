package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.ItemsWithBoundaries;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.ThumbnailAble;
import nonsense.hamsterrun.env.aliens.MovingOne;

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
    private JButton resetItems;
    private JButton disableItems;

    private JPanel aliensPanel;
    private JButton resetAliens;
    private JButton disableAliens;
    private JLabel maxAliensLabel;
    private JSpinner maxAliens;


    //TODO extract shared min/max here and in config validate
    public ItemsAndAliensPanel() {
        this.setLayout(new GridLayout(2, 1));
        JPanel itemsWrapper = createItems();
        add(itemsWrapper);
        JPanel aliensWrapper = createAliens();
        add(aliensWrapper);
        setTitles();
    }

    private JPanel createItems() {
        JPanel itemsWrapper = new JPanel(new BorderLayout());
        itemsPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane controlsScroll = new JScrollPane(itemsPanel);
        itemsWrapper.add(controlsScroll);
        JPanel itemsControls = new JPanel(new GridLayout(1, 2));
        resetItems = new JButton("reset");
        resetItems.addActionListener(a -> resetAllItems());
        disableItems = new JButton("disable all");
        disableItems.addActionListener(a -> disbaleAllItems());
        itemsControls.add(resetItems);
        itemsControls.add(disableItems);
        itemsWrapper.add(itemsControls, BorderLayout.SOUTH);
        return itemsWrapper;
    }

    private JPanel createAliens() {
        JPanel aliensWrapper = new JPanel(new BorderLayout());
        aliensPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane controlsScroll = new JScrollPane(aliensPanel);
        aliensWrapper.add(controlsScroll);
        JPanel aliensControls = new JPanel(new GridLayout(1, 4));
        resetAliens = new JButton("reset");
        resetAliens.addActionListener(a -> resetAllAliens());
        disableAliens = new JButton("disable all");
        disableAliens.addActionListener(a -> disbaleAllAliens());
        aliensControls.add(resetAliens);
        aliensControls.add(disableAliens);
        maxAliensLabel = new JLabel("max aliens - 0 is none!");
        maxAliens  = new JSpinner(new SpinnerNumberModel(BaseConfig.getConfig().getMaxAliens(), 0, 10000, 1));
        maxAliens.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                BaseConfig.getConfig().setMaxAliens(((Number) maxAliens.getValue()).intValue());
            }
        });
        aliensControls.add(maxAliensLabel, BorderLayout.WEST);
        aliensControls.add(maxAliens);
        aliensWrapper.add(aliensControls, BorderLayout.SOUTH);
        return aliensWrapper;
    }

    private static String getPercentText(int ratio, int summ) {
        float probab = ((float) ratio / (float) summ * (float) 100);
        return ratio + "/" + summ + "->" + probab + "%";
    }

    private void regenerateItems(boolean recreate, boolean setSpinners) {
        regenerate(new ItemValuesProvider(), itemsPanel, recreate, setSpinners);
    }

    private void regenerateAliens(boolean recreate, boolean setSpinners) {
        regenerate(new AliensValuesProvider(), aliensPanel, recreate, setSpinners);
    }

    private static void regenerate(ValuesProvider provider, JPanel panel, boolean recreate, boolean setSpinners) {
        if (recreate) {
            panel.removeAll();
        }
        int sum = 0;
        int origSum = 0;
        for (BaseConfig.ItemsWithProbability item : provider.getValues()) {
            int origRatio = provider.getDefaultprobability(item.clazz);
            origSum += origRatio;
            sum += item.ratio;
        }
        int counter = 0;
        for (BaseConfig.ItemsWithProbability iwp : provider.getValues()) {
            if (recreate) {
                PreviewItemLine item = new PreviewItemLine(iwp, origSum, sum, panel, provider);
                panel.add(item);
            } else {
                ((PreviewItemLine) (panel.getComponent(counter))).refreshCountes(iwp, origSum, sum, provider);
                if (setSpinners) {
                    ((PreviewItemLine) (panel.getComponent(counter))).setSpinner();
                }
            }
            counter++;
        }
    }

    private void resetAllItems() {
        BaseConfig.getConfig().resetItemsProbabilities();
        regenerateItems(true, false);
        itemsPanel.revalidate();
        this.revalidate();
    }

    private void disbaleAllItems() {
        BaseConfig.getConfig().disbaleAllItems();
        regenerateItems(true, false);
        itemsPanel.revalidate();
        this.revalidate();
    }

    private void resetAllAliens() {
        BaseConfig.getConfig().resetAliensProbabilities();
        regenerateAliens(true, false);
        aliensPanel.revalidate();
        this.revalidate();
        if (((Number)maxAliens.getValue()).intValue() == 0) {
            maxAliens.setValue(10);
        }
    }

    private void disbaleAllAliens() {
        BaseConfig.getConfig().disbaleAllAliens();
        regenerateAliens(true, false);
        aliensPanel.revalidate();
        this.revalidate();
        maxAliens.setValue(0);
        JOptionPane.showMessageDialog(this, Localization.get().getAllAliensDisabled());
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getItemsTitle());
        disableItems.setText(Localization.get().getDisableAll());
        resetItems.setText(Localization.get().getResetFields());
        disableAliens.setText(Localization.get().getDisableAll());
        resetAliens.setText(Localization.get().getResetFields());
        maxAliensLabel.setText(Localization.get().maxAliensLabel());
        regenerateItems(true, false);
        regenerateAliens(true, false);
    }

    private static class PreviewAlienLine extends JPanel {
        private final MovingOne alien;

        public PreviewAlienLine(MovingOne alien) {
            this.alien = alien;
        }
    }

    private static class PreviewItemLine extends JPanel {
        private final ThumbnailAble item;
        private final BaseConfig.ItemsWithProbability source;
        private final JLabel is;
        private final JSpinner js;

        public PreviewItemLine(BaseConfig.ItemsWithProbability iwp, int origSum, int sum, JPanel parent, ValuesProvider provider) {
            this.setLayout(new GridLayout(1, 3));
            this.item = provider.convert(iwp.clazz);
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
                    provider.set(iwp.clazz, ((Number) js.getValue()).intValue());
                    regenerate(provider, parent, false, false);
                }
            });
            js.setToolTipText(Localization.get().getDisabledOnZero());
            b2.add(js);
            is = new JLabel();
            refreshCountes(iwp, origSum, sum,provider);
            b2.add(is);
            this.add(b2);

        }

        public void refreshCountes(BaseConfig.ItemsWithProbability iwp, int origSum, int sum, ValuesProvider provider) {
            is.setText("is: " + getPercentText(iwp.ratio, sum));
            is.setToolTipText("was: " + getPercentText(provider.getDefaultprobability(iwp.clazz), origSum));
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

    private interface ValuesProvider {
        public int getDefaultprobability(Class clazz);

        public Iterable<? extends BaseConfig.ItemsWithProbability> getValues();

        ThumbnailAble convert(Class clazz);

        void set(Class clazz, int i);
    }

    private static class ItemValuesProvider implements ValuesProvider {

        @Override
        public int getDefaultprobability(Class clazz) {
            return BaseConfig.getConfig().getDefaultItemProbability(clazz);
        }

        @Override
        public Iterable<? extends BaseConfig.ItemsWithProbability> getValues() {
            return BaseConfig.getConfig().getItemsProbabilities();
        }

        @Override
        public ThumbnailAble convert(Class clazz) {
            return ItemsWithBoundaries.itemClassToItemCatched(clazz);
        }

        @Override
        public void set(Class clazz, int prob) {
            BaseConfig.getConfig().addTrapModifierSafe(clazz, prob, false);
        }
    }

    private static class AliensValuesProvider implements ValuesProvider {

        @Override
        public int getDefaultprobability(Class clazz) {
            return BaseConfig.getConfig().getDefaultAlienProbability(clazz);
        }

        @Override
        public Iterable<? extends BaseConfig.ItemsWithProbability> getValues() {
            return BaseConfig.getConfig().getAliensProbabilities();
        }

        @Override
        public ThumbnailAble convert(Class clazz) {
            return ItemsWithBoundaries.alienClassToItemCatched(clazz);
        }

        @Override
        public void set(Class clazz, int prob) {
            BaseConfig.getConfig().addTrapModifierSafe(clazz, prob, true);
        }
    }
}
