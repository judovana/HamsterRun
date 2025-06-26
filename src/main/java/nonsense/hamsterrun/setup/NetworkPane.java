package nonsense.hamsterrun.setup;


import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nonsense.hamsterrun.Localization;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;


public class NetworkPane extends JPanel implements Localized {

    private static final int MAX_TTL = 5;
    private static String hostname = getHostName();
    private static String id = randomStringSimple(10, +1);
    private int serverPort = findRandomPort();
    private int clientPort = serverPort;
    private TmpServerThread tmpServer;
    private SayHelloThread tmpClient;


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
                        //filter out itself by id
                        System.out.println("server got:" + msg);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void cancel() {
            try {
                running = false;
                server.close();
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
                    out.println(id + " hello");
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        Thread.sleep(500);
                    } catch (Exception eex) {
                        eex.printStackTrace();
                    }
                }

            }
        }

        public void cancel() {
            running = false;
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
        JSpinner serverSpinner = new JSpinner(new SpinnerNumberModel(serverPort, 1001, 65000, 1));
        serverSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                serverPort = ((Number) serverSpinner.getValue()).intValue();
                resetPorts();
            }
        });
        this.add(serverSpinner);
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
        JSpinner clientSpinner = new JSpinner(new SpinnerNumberModel(clientPort, 1001, 65000, 1));
        clientSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                clientPort = ((Number) clientSpinner.getValue()).intValue();
                resetPorts();
            }
        });
        this.add(clientSpinner);
        this.add(new JButton("Join game"));
        resetPorts();
        parent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                NetworkPane.this.stopTmpThreads();
            }
        });
        setTitles();
    }

    private void resetPorts() {
        stopTmpThreads();
        tmpServer = new TmpServerThread(serverPort);
        tmpServer.start();
        tmpClient = new SayHelloThread(clientPort);
        tmpClient.start();
    }

    private void stopTmpThreads() {
        if (tmpServer != null) {
            tmpServer.cancel();
        }
        if (tmpClient != null) {
            tmpClient.cancel();
        }
    }

    @Override
    public void setTitles() {
        setName(Localization.get().getNetTitle());

    }

}
