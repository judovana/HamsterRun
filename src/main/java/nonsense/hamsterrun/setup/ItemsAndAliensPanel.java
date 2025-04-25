package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.traps.Item;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.stream.Collectors;


public class ItemsAndAliensPanel extends JPanel implements Localized, ChangeListener {

    private static final SoundsBuffer examples = new SoundsBuffer();
    private static final int origSum = Arrays.stream(BaseConfig.DEFAULT_ITEMS_PROBABILITIES).map(a -> a.ratio).collect(Collectors.summingInt(Integer::intValue));

    //TODO extract shared min/max here and in config validate
    public ItemsAndAliensPanel() {
        this.setLayout(new GridLayout(2, 1));
        JPanel controlls = new JPanel(new GridLayout(0, 1));
        int i = 0;
        for (BaseConfig.ItemsWithProbability iwp : BaseConfig.DEFAULT_ITEMS_PROBABILITIES) {
            i++;
            if (i == 20) {
                break;
            }
            PreviewItemLine item = new PreviewItemLine(iwp);
            controlls.add(item);
        }
        JScrollPane controlsScroll = new JScrollPane(controlls);
        add(controlsScroll);
//        add(items);
//        add(aliens);
        setTitles();
        stateChanged(null);
        ;
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getItemsTitle());

    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {

    }

    private static class PreviewItemLine extends JPanel {
        private final Item item;
        private final BaseConfig.ItemsWithProbability source;

        public PreviewItemLine(BaseConfig.ItemsWithProbability iwp) {
            this.setLayout(new GridLayout(1, 3));
            this.item = BlockField.itemClassToItemCatched(iwp.clazz);
            this.source = iwp;
            JPanel b1 = new JPanel(new GridLayout(2, 1));
            JLabel ln = new JLabel(source.clazz.getSimpleName());
            ln.setToolTipText("Description");
            b1.add(ln);
            JButton sound = new JButton("<))");
            sound.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    item.playMainSoundFor(examples);
                }
            });
            b1.add(sound);
            this.add(b1);
            this.add(new ThumbanilPanel());
            JPanel b2 = new JPanel(new GridLayout(2, 1));
            JSpinner js = new JSpinner(new SpinnerNumberModel(iwp.ratio, 0, 10000, 1));
            js.setToolTipText("If set to 0, then it is disabled");
            b2.add(js);
            JLabel is = new JLabel("is: ");
            float probab = ((float) iwp.ratio / (float) origSum * (float) 100);
            is.setToolTipText("was: " + iwp.ratio + "/" + origSum + "->" + probab + "%");
            b2.add(is);
            this.add(b2);

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

    private static class PreviewAlienLine extends JPanel {
        private final MovingOne alien;

        public PreviewAlienLine(MovingOne alien) {
            this.alien = alien;
        }
    }
}
