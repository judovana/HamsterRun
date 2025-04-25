package nonsense.hamsterrun.setup;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.Main;
import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.sprites.SpritesProvider;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Hello world!
 */
public class SetupWindow extends JFrame implements  Localized {

    private final JButton startButton;

    public SetupWindow() throws HeadlessException {
        JTabbedPane tabs = new JTabbedPane();
        JPanel rats = new RatsPanel();
        tabs.add(rats);
        JPanel items = new ItemsAndAliensPanel();
        tabs.add(items);
        tabs.add(new WorldPanel());
        add(tabs);
        startButton = new JButton("start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        add(startButton, BorderLayout.SOUTH);
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitles();
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        BaseConfig config = BaseConfig.getConfig();
        SpritesProvider.load();
        config.summUp();
        config.verify();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               new SetupWindow();
            }
        });
    }

    public void start() {
        this.dispose();
        new Main().worldDemo();
    }

    public void setTitles() {
        setTitle(Localization.get().getMainTitle());
        startButton.setText(Localization.get().getStartGame());
    }

}
