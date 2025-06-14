package nonsense.hamsterrun.setup;

import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.World;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class PresetConfigs extends JPanel implements Localized {

    private static class LocalisedListItem {
        private final String id;
        private static final String namesapce1 = "nonsense/hamsterrun/preconf";
        private static final String namesapce2 = namesapce1.replace("/", ".");

        public LocalisedListItem(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return Localization.get().get(namesapce2 + ".name." + id);
        }

        public URL getResource() {
            return this.getClass().getClassLoader().getResource(namesapce1 + "/" + id);
        }

        public String getDescription() {
            return Localization.get().get(namesapce2 + ".desc." + id);
        }

    }

    private static List<LocalisedListItem> knownConfigs = Arrays.asList(new LocalisedListItem("paradise"));

    private final JButton load;
    private final JList<LocalisedListItem> configs;
    private final JScrollPane scrollConfigs;
    private final JLabel info;


    public PresetConfigs(World world, SetupWindow parent) {
        this.setLayout(new GridLayout(0, 2));
        JPanel upperHalf = new JPanel(new BorderLayout());
        JPanel bottomHalf = new JPanel(new BorderLayout());
        this.add(upperHalf);
        this.add(bottomHalf);
        configs = new JList(knownConfigs.toArray());
        configs.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (configs.getSelectedValue() != null) {
                    info.setText(configs.getSelectedValue().getDescription());
                    load.setEnabled(true);
                } else {
                    info.setText("");
                    load.setEnabled(false);
                }
            }
        });
        scrollConfigs = new JScrollPane(configs);
        upperHalf.add(scrollConfigs);
        info = new JLabel();
        bottomHalf.add(info);
        load = new JButton("load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MiscPanel.loadConfig(configs.getSelectedValue().getResource().openStream(), world, parent);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PresetConfigs.this, ex);
                }
            }
        });
        load.setEnabled(false);
        bottomHalf.add(load, BorderLayout.SOUTH);
        setTitles();
    }

    @Override
    public void setTitles() {
        load.setText(Localization.get().get("load"));
        setName(Localization.get().getPresetConfigsTitle());
        configs.setModel(new DefaultComboBoxModel<LocalisedListItem>(knownConfigs.toArray(new LocalisedListItem[0])));

    }


}
