package nonsense.hamsterrun.setup;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;


/**
 * Hello world!
 */
public class SetupWindow {
    public static void main(String[] args) throws Exception {
        BaseConfig config = BaseConfig.getConfig();
        SpritesProvider.load();
        config.summUp();
        config.verify();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame gameView = new JFrame(Localization.get().getMainTitle());
                JTabbedPane tabs = new JTabbedPane();
                JPanel rats = new JPanel(new BorderLayout());
                rats.setName("rats");
                ;
                rats.add(new JButton("Add rat"), BorderLayout.NORTH);
                JPanel items = new ItemsAndAliensPanel();
                tabs.add(rats);
                tabs.add(items);
                tabs.add(new WorldPanel());
                gameView.add(tabs);
                gameView.setSize(800, 800);
                gameView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                gameView.setVisible(true);
            }
        });
    }

}
