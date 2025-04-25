package nonsense.hamsterrun.setup;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RatsPanel extends JPanel implements Localized {

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
                mousesWithView.add(new RatConfig());
                RatsPanel.super.revalidate();
            }
        });
        addButton2 = new JButton("Add rat without view");
        addButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mousesWithoutView.add(new RatConfig());
                RatsPanel.super.revalidate();
            }
        });
        JPanel upButtns = new JPanel(new GridLayout(1,2));
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
                BaseConfig.getConfig().setColumns((int)(columnsSpinner.getValue()));
                mousesWithView.setLayout(new GridLayout(0,BaseConfig.getConfig().getColumnsDirect()));
                RatsPanel.super.revalidate();
                RatsPanel.super.repaint();
            }
        });
        bottomSpinner.add(columnsSpinnerLabel);
        bottomSpinner.add(columnsSpinner, BorderLayout.EAST);
        this.add(bottomSpinner, BorderLayout.SOUTH);
        JPanel central = new JPanel(new GridLayout(2,1));
        this.add(central);
        mousesWithView = new JPanel(new GridLayout(0,BaseConfig.getConfig().getColumnsDirect()));
        mousesWithoutView = new JPanel(new GridLayout(1,0));
        central.add(mousesWithView);
        central.add(mousesWithoutView);
        this.add(central);
        setTitles();
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getRatsTitle());
        addButton1.setText(Localization.get().getAddMousesButton1());
        addButton2.setText(Localization.get().getAddMousesButton2());
        columnsSpinnerLabel.setText(Localization.get().getColumnsLabel());
    }

    private static class RatConfig extends JPanel {
        public RatConfig() {
            this.add(new JButton("remove me"));
        }
    }

        private class ThumbanilPanel extends JPanel {
            int i=0;
            final String skin;

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
                if (i>100) {
                    i=0;
                }
                Graphics2D g2d = (Graphics2D) g;
                SpritesProvider.ratSprites.get(skin).getRun(RatActions.Direction.UP.getSprite(), i%(SpritesProvider.ratSprites.get(skin).getRuns()));
            }
        }
    }

