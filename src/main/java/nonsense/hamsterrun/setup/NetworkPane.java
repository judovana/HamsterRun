package nonsense.hamsterrun.setup;


import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;


public class NetworkPane extends JPanel implements Localized {

    private static String hostname = getHostName();
    private static String id = randomStringSimple(10, +1);
    private int serverPort = findRandomPort();

    private static class ObserverThread extends Thread {
        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {

                } catch (Exception ex) {

                }
            }

        }
    }

    ;

    private static class TmpServerThread extends Thread {
        private boolean running = true;
        private final int port;
        private ServerSocket server;

        public TmpServerThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                server = serverSocket;
                while (running) {
                    try (Socket client = serverSocket.accept(); BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                        String msg = in.readLine();
                        System.out.println("server got:" + msg);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private static class SayHelloThread extends Thread {
        private boolean running = true;
        private final int port;
        private ServerSocket server;

        public SayHelloThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            while (running) {
                try (Socket clientSocket = new Socket("localhost", port); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {
                    out.println("hello " + id);
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

    }


    private int findRandomPort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int freePort = serverSocket.getLocalPort();
            return freePort;
        } catch (Exception e) {
            return 8456;
        }
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            return "localhost";
        }
    }

    public static String randomStringSimple(int length, int caseUpNothingDown) {
        byte[] byteArray = new byte[length];
        Random random = new Random();
        random.nextBytes(byteArray);
        String s = Base64.getEncoder().encodeToString(byteArray).replaceAll("=", "");
        if (caseUpNothingDown == 0) {
            return s;
        } else if (caseUpNothingDown < 0) {
            return s.toLowerCase();
        } else {
            return s.toUpperCase();
        }
    }

    public NetworkPane(World world, SetupWindow parent) {
        this.setLayout(new GridLayout(0, 1));
        this.add(new JLabel("<html><div style='text-align: center; font-weight: bold; text-decoration: underline; '>" + "Server" + "</div></html>"));
        this.add(new JLabel("All guinea pigs created in '" + Localization.get().getRatsTitle() + "' will be used"));
        this.add(new JLabel("Once you are happy with your setup, click create game. then you will wait, until everybody connects, and THEN start the game."));
        this.add(new JLabel("this machine: " + hostname));
        ;
        //fixme, move to two on one line
        this.add(new JLabel("this port: "));
        ;
        this.add(new JSpinner(new SpinnerNumberModel(serverPort, 1001, 65000, 1)));
        this.add(new JButton("Create game"));
        this.add(new JLabel("Connected players:"));
        JPanel connected = new JPanel(new GridLayout(0, 1));
        this.add(connected);
        //todo move to jmessagepane ? Jsut disable everything?
        this.add(new JLabel("Once all expected guinea pigs are in game, you can press start"));
        this.add(new JButton("Start"));
        this.add(new JLabel("<html><div style='text-align: center; '>" + "------------------ xor ------------------" + "</div></html>"));
        this.add(new JLabel("<html><div style='text-align: center; font-weight: bold; text-decoration: underline; '>" + "Client" + "</div></html>"));
        this.add(new JLabel(" guinea pigs created in '" + Localization.get().getRatsTitle() + "' will NOT be used"));
        this.add(new JLabel("Select your desired mouse, control, and set name:"));
        //fixme, move to two on one line
        this.add(new JComboBox<>(SpritesProvider.KNOWN_RATS.toArray(new String[0])));
        this.add(new JComboBox<String>(RatsPanel.getDispalyfullControls()));
        this.add(new JLabel("id: " + id));
        //fixme, moe to two on one line
        this.add(new JLabel("name: "));
        ;
        this.add(new JTextField(randomStringSimple(5, -1)));
        //fixme, moe to two on one line
        this.add(new JLabel("target machine: "));
        ;
        this.add(new JTextField("localhost"));
        //fixme, moe to two on one line
        this.add(new JLabel("target port: "));
        ;
        this.add(new JSpinner(new SpinnerNumberModel(serverPort, 1001, 65000, 1)));
        this.add(new JButton("Join game"));
        new TmpServerThread(serverPort).start();
        new SayHelloThread(serverPort).start();
        setTitles();
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getNetTitle());

    }

}
